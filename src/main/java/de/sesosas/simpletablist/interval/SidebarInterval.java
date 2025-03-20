package de.sesosas.simpletablist.interval;

import de.sesosas.simpletablist.api.classes.AInterval;
import de.sesosas.simpletablist.classes.AnimationClass;
import de.sesosas.simpletablist.classes.scoreboard.SidebarClass;
import de.sesosas.simpletablist.config.SidebarConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Interval task for updating sidebars
 */
public class SidebarInterval extends AInterval {
    private boolean syncWithTablistAnimation;

    public SidebarInterval() {
        super("SidebarUpdate");
    }

    @Override
    public void Init() {
        // Get the update interval from config, default to 2 seconds
        setIntervalTime(SidebarConfig.getInt("Sidebar.Refresh.Interval"));

        // Check if sidebar animations should sync with tablist animations
        syncWithTablistAnimation = SidebarConfig.getBoolean("Sidebar.Animations.SyncWithTablist");

        // Sidebar updates need to be on the main thread as they modify Bukkit entities
        setUseMainThread(true);
    }

    @Override
    public void Run() {
        if (!SidebarConfig.getBoolean("Sidebar.Enable")) {
            return;
        }

        // If animations are enabled, we advance the frame index
        if (SidebarConfig.getBoolean("Sidebar.Animations.Enable") && !syncWithTablistAnimation) {
            // Only increment the frame index if we're not syncing with tablist
            // (otherwise the tablist interval will handle it)
            AnimationClass.frameIndex++;
        }

        // Only update sidebars if refresh is enabled or if animations are enabled
        if (SidebarConfig.getBoolean("Sidebar.Refresh.Enable") ||
                SidebarConfig.getBoolean("Sidebar.Animations.Enable")) {

            // Update sidebar for all online players
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
            for (Player player : playerList) {
                SidebarClass.updateSidebar(player);
            }
        }
    }

    @Override
    public void onStart() {
        Bukkit.getLogger().info("[SimpleTabList] Sidebar update interval started with period: "
                + getIntervalTime() + " seconds");

        if (syncWithTablistAnimation) {
            Bukkit.getLogger().info("[SimpleTabList] Sidebar animations will sync with tablist animations");
        } else if (SidebarConfig.getBoolean("Sidebar.Animations.Enable")) {
            Bukkit.getLogger().info("[SimpleTabList] Sidebar animations enabled with independent timing");
        }
    }

    @Override
    public void onStop() {
        Bukkit.getLogger().info("[SimpleTabList] Sidebar update interval stopped");
    }
}