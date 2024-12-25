package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormater {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    private static String ph(String text) {
        return "{" + text + "}";
    }

    public static String Get(String text, Player player) {
        if (text == null) return null;

        String result = text;

        // Replace placeholders using PlaceholderAPI if available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            result = PlaceholderAPI.setPlaceholders(player, result);
        }

        // Replace custom placeholders
        result = result.replace(ph("player_name"), player.getDisplayName());
        result = result.replace("stl.format.", "");

        if (CurrentConfig.getBoolean("Worlds.Enable")) {
            String worldPrefix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Prefix");
            String worldSuffix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Suffix");
            result = result.replace(ph("world_prefix"), worldPrefix != null ? worldPrefix : "");
            result = result.replace(ph("world_suffix"), worldSuffix != null ? worldSuffix : "");
        }

        if (CurrentConfig.getBoolean("Names.Global.Enable")) {
            String globalPrefix = CurrentConfig.getString("Names.Global.Prefix");
            String globalSuffix = CurrentConfig.getString("Names.Global.Suffix");
            result = result.replace(ph("global_prefix"), globalPrefix != null ? globalPrefix : "");
            result = result.replace(ph("global_suffix"), globalSuffix != null ? globalSuffix : "");
        }

        // Replace player stats placeholders
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        result = result.replace(ph("player_health"), df.format(Objects.requireNonNull(player).getHealth()));
        result = result.replace(ph("player_food"), df.format(player.getFoodLevel()));
        result = result.replace(ph("player_xp"), df.format(player.getExp()));
        result = result.replace(ph("player_lvl"), Integer.toString(player.getLevel()));
        result = result.replace(ph("player_gamemode"), player.getGameMode().toString());

        // Process HEX codes
        result = hex(result);

        return result;
    }

    // Process HEX color codes
    public static String hex(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group();
            String replaceSharp = hexCode.replace('#', 'x');

            StringBuilder builder = new StringBuilder();
            for (char c : replaceSharp.toCharArray()) {
                builder.append("&").append(c);
            }

            matcher.appendReplacement(buffer, builder.toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}