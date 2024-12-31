package de.sesosas.simpletablist.api.utils;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Map<String, Thread> threadMap = new HashMap<>();

    public static void submitTask(Runnable task) {
        if (!isShutdown()) {
            executor.submit(task);
        } else {
            // Log or handle the scenario where the executor is shut down
            Bukkit.getLogger().warning("Executor has been shut down, cannot submit new tasks.");
        }
    }

    public static void assignThread(String taskName, Runnable task) {
        Thread thread = new Thread(task);
        threadMap.put(taskName, thread);
    }

    public static void startThread(String taskName) {
        Thread thread = threadMap.get(taskName);
        if (thread != null && !thread.isAlive()) {
            executor.submit(thread);
        }
    }

    public static void stopThread(String taskName) {
        Thread thread = threadMap.get(taskName);
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    public static void startAllThreads() {
        for (Thread thread : threadMap.values()) {
            if (!thread.isAlive()) {
                executor.submit(thread);
            }
        }
    }

    public static void stopAllThreads() {
        for (Thread thread : threadMap.values()) {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
    }

    public static boolean isRunning(String taskName) {
        Thread thread = threadMap.get(taskName);
        return thread != null && thread.isAlive();
    }

    public static void shutdown() {
        executor.shutdown();
    }

    public static void shutdownNow() {
        executor.shutdownNow();
    }

    public static void close() {
        executor.shutdown();
    }

    public static boolean isShutdown() {
        return executor.isShutdown();
    }

    public static void forceShutdown() {
        executor.shutdownNow();
    }
}
