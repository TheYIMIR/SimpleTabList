package de.sesosas.simpletablist.classes.commands;

import de.sesosas.simpletablist.classes.handlers.internal.IntervalHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import de.sesosas.simpletablist.classes.handlers.internal.MessageHandler;
import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.tab.AnimationHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            SimpleTabList.getPlugin().reloadConfig();
            SimpleTabList.getPlugin().config = SimpleTabList.getPlugin().getConfig();

            Bukkit.getLogger().info("Reloading animations...");
            AnimationHandler.loadAnimationsConfig();

            Bukkit.getLogger().info("Restarting interval handler...");

            if (SimpleTabList.interval != null && !SimpleTabList.interval.isRunning()) {
                SimpleTabList.interval.stopInterval();
                Bukkit.getLogger().info("Closed interval handler session...");
            }

            Runnable task = () -> {
                NameHandler.Update();
                AnimationHandler.frameIndex++;

                List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                for (Player playerr : playerList) {
                    TabHandler.UpdateTab(playerr);
                }
            };

            long intervalTicks = SimpleTabList.getPlugin().config.getLong("Tab.Refresh.Interval.Time") * 20L;

            SimpleTabList.interval = new IntervalHandler(task, intervalTicks);

            if (SimpleTabList.getPlugin().config.getBoolean("Tab.Refresh.Interval.Enable")) {
                SimpleTabList.interval.startInterval();
                Bukkit.getLogger().info("Started interval handler session...");
            } else {
                Bukkit.getLogger().info("Interval handler is disabled.");
            }

            String text = "Successfully reloaded the Config!";
            MessageHandler.Send(player, ChatColor.AQUA + text);
        }
        return true;
    }
}
