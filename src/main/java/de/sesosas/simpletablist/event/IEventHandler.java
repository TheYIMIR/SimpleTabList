package de.sesosas.simpletablist.event;

import de.sesosas.simpletablist.api.utils.ThreadUtil;
import de.sesosas.simpletablist.classes.ScoreboardClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.TimeUnit;

public class IEventHandler implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        ThreadUtil.submitTask(ScoreboardClass::Update);
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
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
