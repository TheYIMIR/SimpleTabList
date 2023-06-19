package de.sesosas.simpletablist.classes.handlers.internal;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class IntervalHandler extends BukkitRunnable {
    private final JavaPlugin plugin;
    private boolean enabled;
    private long intervalTicks;

    public IntervalHandler(JavaPlugin plugin, long intervalTicks) {
        this.plugin = plugin;
        this.enabled = false;
        this.intervalTicks = intervalTicks;
    }

    public void setIntervalTicks(long intervalTicks) {
        this.intervalTicks = intervalTicks;
    }

    public long getIntervalTicks() {
        return this.intervalTicks;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void run() {
        if (enabled) {
            NameHandler.Update();
            for(Player player : Bukkit.getOnlinePlayers()){
                TabHandler.UpdateTab(player);
            }
        }
    }
}
