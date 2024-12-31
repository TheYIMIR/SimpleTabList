package de.sesosas.simpletablist.api.classes;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import java.util.ArrayList;
import java.util.List;

public abstract class AInterval {

    private final String name;
    private static List<AInterval> instances = new ArrayList<>();
    private BukkitTask task;
    private long intervalTime;

    public AInterval(String name) {
        this.name = name;
        instances.add(this);
        Init();
    }

    public String getName() {
        return name;
    }

    public void setIntervalTime(long intervalTime){
        this.intervalTime = intervalTime;
    }

    public long getIntervalTime(){
        return this.intervalTime;
    }

    public abstract void Init();

    public abstract void Run();

    public void startInterval(JavaPlugin plugin, long interval) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                Run();
            }
        }, 0L, interval * 20L);
    }

    public void stopInterval() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
            task = null;
        }
    }

    public static void startAllIntervals(JavaPlugin plugin) {
        for (AInterval intervalTask : instances) {
            intervalTask.startInterval(plugin, intervalTask.getIntervalTime());
        }
    }

    public static void stopAllIntervals() {
        for (AInterval intervalTask : instances) {
            intervalTask.stopInterval();
        }
    }
}
