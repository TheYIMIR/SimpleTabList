package de.sesosas.simpletablist.classes.commands;

import de.sesosas.simpletablist.classes.handlers.internal.IntervalHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.lp.PermissionsHandler;
import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.internal.MessageHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            File file = new File(SimpleTabList.getPlugin().getDataFolder().getAbsolutePath() + "/config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            SimpleTabList.getPlugin().config = cfg;

            NameHandler.Update();
            for(Player p : Bukkit.getOnlinePlayers()){
                TabHandler.UpdateTab(p);
            }

            SimpleTabList.interval.cancel();
            SimpleTabList.interval = new IntervalHandler(SimpleTabList.getPlugin(), SimpleTabList.getPlugin().config.getLong("Tab.Refresh.Interval.Time") * 20L);
            SimpleTabList.interval.runTaskTimer(SimpleTabList.getPlugin(), 0L, SimpleTabList.getPlugin().config.getLong("Tab.Refresh.Interval.Time") * 20L);
            SimpleTabList.interval.setEnabled(SimpleTabList.getPlugin().config.getBoolean("Tab.Refresh.Interval.Use"));

            String text = "Successfully reloaded the Config!";
            MessageHandler.Send(player, ChatColor.AQUA + text);
        }
        return false;
    }
}
