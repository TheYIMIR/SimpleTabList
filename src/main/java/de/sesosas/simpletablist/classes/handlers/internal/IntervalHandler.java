package de.sesosas.simpletablist.classes.handlers.internal;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class IntervalHandler {
    public static void ToggleInterval(boolean use){
        if(use){
            Thread thread = new Thread(){
                public void run(){
                    SimpleTabList.interval = new BukkitRunnable() {

                        @Override
                        public void run() {
                            TabHandler.UpdateTab();
                            NameHandler.Update();
                        }
                    }.runTaskTimer(SimpleTabList.getPlugin(), 0, SimpleTabList.getPlugin().config.getLong("Plugin.Update.Interval.Time"));
                }
            };
            thread.start();
        }
        else{
            if(SimpleTabList.interval != null){
                SimpleTabList.interval.cancel();
            }
        }
    }
}
