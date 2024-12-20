package de.sesosas.simpletablist.classes.handlers.events;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.TimeUnit;

public class IEventHandler implements Listener  {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        Thread.startVirtualThread(() -> {
            NameHandler.Update();
            TabHandler.UpdateTab(event.getPlayer());
        });
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event){
        Thread.startVirtualThread(() -> NameHandler.Update());
    }

    @EventHandler
    public void OnEntityPortalExitEvent(PlayerTeleportEvent event) {
        Thread.startVirtualThread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            NameHandler.Update();
            TabHandler.UpdateTab(event.getPlayer());
        });
    }
}
