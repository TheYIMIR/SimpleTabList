package de.sesosas.simpletablist.event;

import de.sesosas.simpletablist.api.utils.ThreadUtil;
import de.sesosas.simpletablist.classes.ScoreboardClass;
import de.sesosas.simpletablist.classes.scoreboard.SidebarClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.TimeUnit;

public class IEventHandler implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        // Update all scoreboard features
        ThreadUtil.submitTask(ScoreboardClass::Update);
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        // Properly handle player quit for sidebar management
        SidebarClass.handlePlayerQuit(event.getPlayer());

        // Update for remaining players
        ThreadUtil.submitTask(ScoreboardClass::Update);
    }

    @EventHandler
    public void OnEntityPortalExitEvent(PlayerTeleportEvent event) {
        ThreadUtil.submitTask(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ScoreboardClass.Update();
        });
    }
}