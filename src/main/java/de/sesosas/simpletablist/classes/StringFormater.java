package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class StringFormater {

    private static String ph(String text){
        return "{" + text + "}";
    }

    public static String Get(String text){
        if(SimpleTabList.getPlugin().config.getBoolean("Chat.Colors")){
            return text
                    .replace("%", "")
                    .replace("&", "§");
        }
        else{
            return text;
        }
    }

    public static String Get(String text, Player player){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if(text != null){
            if(SimpleTabList.getPlugin().config.getBoolean("Chat.Colors")){
                return text
                        .replace("%", "")
                        .replace("&", "§")
                        .replace(ph("player_name"), player.getDisplayName())
                        .replace(ph("player_health"), df.format(player.getPlayer().getHealth()))
                        .replace(ph("player_food"), df.format(player.getPlayer().getFoodLevel()))
                        .replace(ph("player_ping"), Integer.toString(player.getPlayer().getPing()))
                        .replace(ph("player_xp"), df.format(player.getPlayer().getExp()))
                        .replace(ph("player_lvl"), Integer.toString(player.getPlayer().getLevel()))
                        .replace(ph("player_gamemode"), player.getPlayer().getGameMode().toString());
            }
            else{
                return text
                        .replace("%", "")
                        .replace(ph("player_name"), player.getDisplayName())
                        .replace(ph("player_health"), df.format(player.getPlayer().getHealth()))
                        .replace(ph("player_food"), df.format(player.getPlayer().getFoodLevel()))
                        .replace(ph("player_ping"), Integer.toString(player.getPlayer().getPing()))
                        .replace(ph("player_xp"), df.format(player.getPlayer().getExp()))
                        .replace(ph("player_lvl"), Integer.toString(player.getPlayer().getLevel()))
                        .replace(ph("player_gamemode"), player.getPlayer().getGameMode().toString());
            }
        }
        else{
            return text;
        }
    }
}
