package de.sesosas.simpletablist.classes.handlers.internal;

import java.util.concurrent.TimeUnit;

public class IntervalHandler {
    private final Runnable task;
    private final long intervalMillis;
    private boolean running;

    public IntervalHandler(Runnable task, long intervalMillis) {
        this.task = task;
        this.intervalMillis = intervalMillis;
        this.running = false;
    }

    public void startInterval() {
        if (!running) {
            running = true;
            ThreadHandler.submitTask(() -> {
                while (running) {
                    task.run();
                    try {
                        TimeUnit.MILLISECONDS.sleep(intervalMillis);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

    public void stopInterval() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
