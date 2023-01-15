package de.sesosas.simpletablist.classes.commands;

import de.sesosas.simpletablist.classes.handlers.PermissionsHandler;
import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.MessageHandler;
import de.sesosas.simpletablist.classes.handlers.TabHandler;
import de.sesosas.simpletablist.classes.handlers.TeamHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ReloadCommand {

    public static void Do(Player player, String[] args) {

        if(PermissionsHandler.hasPermission(player, "stl.reload")){
            File file = new File(SimpleTabList.getPlugin().getDataFolder().getAbsolutePath() + "/config.yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            SimpleTabList.getPlugin().config = cfg;

            TabHandler.UpdateTab();
            TeamHandler.ApplyTeam();

            String text = "Successfully reloaded the Config!";
            MessageHandler.Send(player, ChatColor.AQUA + text);

        }
        else{
            String text = "You are not allowed to use this command!";
            MessageHandler.Send(player, ChatColor.YELLOW + text);
        }

    }

}
