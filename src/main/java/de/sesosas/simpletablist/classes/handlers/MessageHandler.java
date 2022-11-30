package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.Nametag;
import de.sesosas.simpletablist.classes.StringFormater;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MessageHandler {

    public static void Send(Player player, String text){
        String prefix = SimpleTabList.getPlugin().config.getString("Chat.Prefix");
        if(SimpleTabList.getPlugin().config.getBoolean("Plugin.ActionbarMessage")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(prefix + " " + StringFormater.Get(text, player)));
        }
        else{
            player.sendMessage(prefix + " " + StringFormater.Get(text, player));
        }
    }

}
