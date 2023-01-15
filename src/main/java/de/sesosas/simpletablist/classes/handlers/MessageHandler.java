package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MessageHandler {

    public static void Send(Player player, String text){
        String prefix = CurrentConfig.getString("Chat.Prefix");
        if(CurrentConfig.getBoolean("Plugin.ActionbarMessage")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(prefix + " " + StringFormater.Get(text, player)));
        }
        else{
            player.sendMessage(prefix + " " + StringFormater.Get(text, player));
        }
    }

    public static String CheckBannedWords(String text){
        String message = text;
        if(CurrentConfig.getList("Chat.Moderation.WordBlacklist") != null){
            String[] msg = text.split(" ");
            for(String s : msg) {
                if (CurrentConfig.getList("Chat.Moderation.WordBlacklist").contains(s.toLowerCase())) {
                    message = message.replace(s, "***");
                }
            }
        }
        return message;
    }

    public static String CheckLinks(String text){
        String message = text;
        if(CurrentConfig.getList("Chat.Moderation.LinkWhitelist") != null){
            String[] msg = text.split(" ");
            for(String s : msg) {
                if ((s.startsWith("https://") || s.startsWith("http://")) && !CurrentConfig.getList("Chat.Moderation.LinkWhitelist").contains(s.toLowerCase())) {
                    message = message.replace(s, "***");
                }
            }
        }
        return message;
    }

}
