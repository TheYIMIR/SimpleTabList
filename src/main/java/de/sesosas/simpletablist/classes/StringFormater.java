package de.sesosas.simpletablist.classes;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormater {

    public static String Get(String text, Player player){
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if(text != null){
            String con = text
                    .replace("&", "§")
                    .replace("{player_name}", player.getDisplayName())
                    .replace("{player_health}", df.format(player.getPlayer().getHealth()))
                    .replace("{player_food}", df.format(player.getPlayer().getFoodLevel()))
                    .replace("{player_xp}", df.format(player.getPlayer().getExp()))
                    .replace("{player_lvl}", Integer.toString(player.getPlayer().getLevel()))
                    .replace("{player_gamemode}", player.getPlayer().getGameMode().toString());
            return hex(PlaceholderAPI.setPlaceholders(player, con));
        } else{
            return "";
        }
    }

    private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    //https://www.spigotmc.org/threads/hex-color-code-translate.449748/
    public static String hex(String message) {
        String translated = ChatColor.translateAlternateColorCodes('&', message);
        Matcher matcher = pattern.matcher(translated);
        while (matcher.find()) {
            String hexCode = translated.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("§").append(c);
            }

            translated = translated.replace(hexCode, builder.toString());
            matcher = pattern.matcher(translated);
        }
        return translated;
    }
}
