package de.sesosas.simpletablist.classes.handlers.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHandler {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void submitTask(Runnable task) {
        executor.submit(task);
    }

    public static void shutdown() {
        executor.shutdown();
    }

    public static boolean isShutdown() {
        return executor.isShutdown();
    }

    public static void forceShutdown() {
        executor.shutdownNow();
    }
}
