package de.sesosas.simpletablist.command;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.ScoreboardClass;
import de.sesosas.simpletablist.config.CurrentConfig;
import de.sesosas.simpletablist.config.SidebarConfig;
import de.sesosas.simpletablist.api.classes.AInterval;
import de.sesosas.simpletablist.api.utils.ThreadUtil;
import de.sesosas.simpletablist.utils.MessageSender;
import de.sesosas.simpletablist.classes.AnimationClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Reload main config
            File file = new File(SimpleTabList.getPlugin().getDataFolder().getAbsolutePath() + "/config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            SimpleTabList.getPlugin().config = cfg;

            // Reload sidebar config
            SidebarConfig.reloadConfig();

            // Reload animations
            AnimationClass.loadAnimationsConfig();

            // Stop and restart intervals with new configuration
            AInterval.stopAllIntervals();
            AInterval.startAllIntervals(SimpleTabList.getPlugin());

            // Update all scoreboards
            ThreadUtil.runOnMainThread(ScoreboardClass::Update);

            // Send confirmation message
            String text = "Successfully reloaded all configurations!";
            MessageSender.Send(player, ChatColor.AQUA + text);
        } else {
            // Console reload
            File file = new File(SimpleTabList.getPlugin().getDataFolder().getAbsolutePath() + "/config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            SimpleTabList.getPlugin().config = cfg;

            SidebarConfig.reloadConfig();
            AnimationClass.loadAnimationsConfig();

            AInterval.stopAllIntervals();
            AInterval.startAllIntervals(SimpleTabList.getPlugin());

            ThreadUtil.runOnMainThread(ScoreboardClass::Update);

            Bukkit.getLogger().info("[SimpleTabList] All configurations reloaded successfully!");
        }
        return true;
    }
}