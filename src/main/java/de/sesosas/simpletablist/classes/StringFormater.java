package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormater {

    private static String ph(String text){
        return "{" + text + "}";
    }

    public static String Get(String text, Player player){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if(text != null){
            String con = PlaceholderAPI.setPlaceholders(player, text);
            con = hex(con);
            return con
                    .replace("&", "§")
                    .replace(ph("player_name"), player.getDisplayName())
                    .replace(ph("player_health"), df.format(player.getPlayer().getHealth()))
                    .replace(ph("player_food"), df.format(player.getPlayer().getFoodLevel()))
                    .replace(ph("player_xp"), df.format(player.getPlayer().getExp()))
                    .replace(ph("player_lvl"), Integer.toString(player.getPlayer().getLevel()))
                    .replace(ph("player_gamemode"), player.getPlayer().getGameMode().toString());
        }
        else{
            return text;
        }
    }

    //https://www.spigotmc.org/threads/hex-color-code-translate.449748/
    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
