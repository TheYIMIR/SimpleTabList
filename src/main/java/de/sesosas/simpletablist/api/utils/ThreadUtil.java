package de.sesosas.simpletablist.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class ThreadUtil {

    private static Plugin plugin;
    private static ExecutorService executor;
    private static ScheduledExecutorService scheduledExecutor;
    private static final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private static final Map<String, CompletableFuture<?>> runningTasks = new HashMap<>();
    // Track if executors need to be lazily initialized
    private static boolean executorsShutdown = false;

    /**
     * Initialize the thread utilities with a plugin instance
     * @param pluginInstance The plugin instance
     */
    public static void initialize(Plugin pluginInstance) {
        plugin = pluginInstance;

        // Initialize thread pools immediately but they will be lazily re-initialized if needed
        createExecutors();

        Bukkit.getLogger().info("[SimpleTabList] Thread utilities initialized");
    }

    /**
     * Create executor services if they don't exist
     */
    private static synchronized void createExecutors() {
        if (executor == null || executor.isShutdown() || scheduledExecutor == null || scheduledExecutor.isShutdown()) {
            // Create thread factory with proper naming for better debugging
            ThreadFactory threadFactory = new ThreadFactory() {
                private final AtomicInteger count = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("STL-Worker-" + count.incrementAndGet());
                    thread.setDaemon(true); // Mark as daemon so they don't prevent JVM shutdown
                    return thread;
                }
            };

            if (executor == null || executor.isShutdown()) {
                executor = Executors.newCachedThreadPool(threadFactory);
            }

            if (scheduledExecutor == null || scheduledExecutor.isShutdown()) {
                scheduledExecutor = Executors.newScheduledThreadPool(2, threadFactory);
            }

            executorsShutdown = false;
            Bukkit.getLogger().info("[SimpleTabList] Thread pools initialized");
        }
    }

    /**
     * Check if executors need to be initialized and do so if necessary
     */
    private static void ensureExecutorsActive() {
        if (executorsShutdown || executor == null || executor.isShutdown() ||
                scheduledExecutor == null || scheduledExecutor.isShutdown()) {
            createExecutors();
        }
    }

    /**
     * Submit a task to be executed asynchronously
     * @param task The task to execute
     * @return CompletableFuture representing the pending completion of the task
     */
    public static CompletableFuture<Void> submitTask(Runnable task) {
        ensureExecutorsActive();

        return CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "[SimpleTabList] Error in async task", e);
            }
        }, executor);
    }

    /**
     * Submit a named task that can be tracked or cancelled later
     * @param taskName Name to identify the task
     * @param task The task to execute
     */
    public static void submitNamedTask(String taskName, Runnable task) {
        ensureExecutorsActive();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "[SimpleTabList] Error in named task: " + taskName, e);
            } finally {
                runningTasks.remove(taskName);
            }
        }, executor);

        runningTasks.put(taskName, future);
    }

    /**
     * Schedule a task to run after a delay
     * @param taskName Name to identify the task
     * @param task The task to execute
     * @param delay The delay in milliseconds
     */
    public static void scheduleTask(String taskName, Runnable task, long delay) {
        ensureExecutorsActive();

        ScheduledFuture<?> future = scheduledExecutor.schedule(() -> {
            try {
                // Check if server has players before running
                if (!Bukkit.getOnlinePlayers().isEmpty() || taskName.startsWith("system_")) {
                    // Run on the Bukkit main thread if plugin is still enabled
                    if (plugin.isEnabled()) {
                        Bukkit.getScheduler().runTask(plugin, task);
                    } else {
                        task.run();
                    }
                } else {
                    Bukkit.getLogger().fine("[SimpleTabList] Skipping scheduled task " + taskName + " - no players online");
                }
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "[SimpleTabList] Error in scheduled task: " + taskName, e);
            } finally {
                scheduledTasks.remove(taskName);
            }
        }, delay, TimeUnit.MILLISECONDS);

        scheduledTasks.put(taskName, future);
    }

    /**
     * Schedule a repeating task
     * @param taskName Name to identify the task
     * @param task The task to execute repeatedly
     * @param initialDelay Initial delay in milliseconds
     * @param period Period between executions in milliseconds
     */
    public static void scheduleRepeatingTask(String taskName, Runnable task, long initialDelay, long period) {
        ensureExecutorsActive();

        ScheduledFuture<?> future = scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                // Only run the task if there are players online or it's a system task
                if (!Bukkit.getOnlinePlayers().isEmpty() || taskName.startsWith("system_")) {
                    // Run on the Bukkit main thread if plugin is still enabled
                    if (plugin.isEnabled()) {
                        Bukkit.getScheduler().runTask(plugin, task);
                    } else {
                        // Cancel this task if plugin is no longer enabled
                        cancelTask(taskName);
                    }
                }
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "[SimpleTabList] Error in repeating task: " + taskName, e);
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);

        scheduledTasks.put(taskName, future);
    }

    /**
     * Cancel a specific scheduled or named task
     * @param taskName The name of the task to cancel
     * @return True if the task was found and cancelled
     */
    public static boolean cancelTask(String taskName) {
        boolean cancelled = false;

        // Try to cancel scheduled task
        ScheduledFuture<?> scheduledFuture = scheduledTasks.remove(taskName);
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            scheduledFuture.cancel(true);
            cancelled = true;
        }

        // Try to cancel named task
        CompletableFuture<?> runningTask = runningTasks.remove(taskName);
        if (runningTask != null && !runningTask.isDone()) {
            runningTask.cancel(true);
            cancelled = true;
        }

        return cancelled;
    }

    /**
     * Run a task on the Bukkit main thread
     * @param task The task to run on the main thread
     */
    public static void runOnMainThread(Runnable task) {
        if (plugin.isEnabled()) {
            Bukkit.getScheduler().runTask(plugin, task);
        } else {
            Bukkit.getLogger().warning("[SimpleTabList] Cannot run task on main thread, plugin is disabled");
        }
    }

    /**
     * Run a task on the Bukkit main thread after a delay
     * @param task The task to run
     * @param delayTicks Delay in server ticks
     */
    public static void runLaterOnMainThread(Runnable task, long delayTicks) {
        if (plugin.isEnabled()) {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        } else {
            Bukkit.getLogger().warning("[SimpleTabList] Cannot run delayed task on main thread, plugin is disabled");
        }
    }

    /**
     * Check if there are any running tasks
     * @return True if any tasks are running
     */
    public static boolean hasRunningTasks() {
        return !scheduledTasks.isEmpty() || !runningTasks.isEmpty();
    }

    /**
     * Shutdown executors if no tasks are running and no players are online
     * This conserves resources when the server is idle
     */
    public static synchronized void shutdownIfIdle() {
        if (Bukkit.getOnlinePlayers().isEmpty() && !hasRunningTasks()) {
            if (executor != null && !executor.isShutdown()) {
                executor.shutdown();
            }

            if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
                scheduledExecutor.shutdown();
            }

            executorsShutdown = true;
            Bukkit.getLogger().info("[SimpleTabList] Thread pools shut down due to server being idle");
        }
    }

    /**
     * Cancel all tasks and perform graceful shutdown
     */
    public static void shutdown() {
        // Cancel all scheduled tasks
        for (ScheduledFuture<?> future : scheduledTasks.values()) {
            if (!future.isDone()) {
                future.cancel(false);
            }
        }
        scheduledTasks.clear();

        // Cancel all named tasks
        for (CompletableFuture<?> future : runningTasks.values()) {
            if (!future.isDone()) {
                future.cancel(true);
            }
        }
        runningTasks.clear();

        // Shutdown executors gracefully
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }

        if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
            scheduledExecutor.shutdown();
        }

        executorsShutdown = true;
        Bukkit.getLogger().info("[SimpleTabList] Thread utilities shutdown gracefully");
    }

    /**
     * Force immediate shutdown of all executors
     */
    public static void forceShutdown() {
        // Cancel all tasks
        for (ScheduledFuture<?> future : scheduledTasks.values()) {
            future.cancel(true);
        }
        scheduledTasks.clear();

        for (CompletableFuture<?> future : runningTasks.values()) {
            future.cancel(true);
        }
        runningTasks.clear();

        // Force shutdown of executors
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }

        if (scheduledExecutor != null && !scheduledExecutor.isShutdown()) {
            scheduledExecutor.shutdownNow();
        }

        executorsShutdown = true;
        Bukkit.getLogger().info("[SimpleTabList] Thread utilities force shutdown complete");
    }

    /**
     * Check if the executor service is shutdown
     * @return True if the executor is shutdown
     */
    public static boolean isShutdown() {
        return executor == null || executor.isShutdown();
    }
}