package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.CustomConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationHandler {
    private static final Pattern animationPattern = Pattern.compile("\\{animation:(\\d+)}");
    private static FileConfiguration animationsConfig = null;
    public static int frameIndex = 0;

    public static String convertAnimatedText(String message) {
        Matcher matcher = animationPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String animationTag = matcher.group();
            String animationId = matcher.group(1);
            String animatedText = getAnimatedText(animationId);

            matcher.appendReplacement(buffer, animatedText);
        }

        matcher.appendTail(buffer);
        String formattedMessage = buffer.toString();

        return formattedMessage;
    }

    private static String getAnimatedText(String animationId) {
        if (animationsConfig == null) {
            loadAnimationsConfig();
        }

        if (animationsConfig.contains("animations." + animationId)) {
            String[] frames = animationsConfig.getStringList("animations." + animationId).toArray(new String[0]);
            int frameCount = frames.length;
            int currentIndex = frameIndex % frameCount;
            return frames[currentIndex];
        }
        return "";
    }

    public static void loadAnimationsConfig() {
        CustomConfig cf = new CustomConfig().setup("animations");
        FileConfiguration con = cf.get();

        animationsConfig = con;
    }
}
