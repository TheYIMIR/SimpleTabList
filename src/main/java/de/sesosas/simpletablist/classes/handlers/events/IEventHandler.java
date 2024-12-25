package de.sesosas.simpletablist.classes.handlers.events;

import de.sesosas.simpletablist.classes.handlers.internal.ThreadHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.TimeUnit;

public class IEventHandler implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        ThreadHandler.submitTask(() -> {
            NameHandler.Update();
            TabHandler.UpdateTab(event.getPlayer());
        });
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        ThreadHandler.submitTask(NameHandler::Update);
    }

    @EventHandler
    public void OnEntityPortalExitEvent(PlayerTeleportEvent event) {
        ThreadHandler.submitTask(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NameHandler.Update();
            TabHandler.UpdateTab(event.getPlayer());
        });
    }
}
