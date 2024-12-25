package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.classes.CustomConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationHandler {
    private static final Pattern animationPattern = Pattern.compile("\\{animation:(\\d+)}");
    private static FileConfiguration animationsConfig = null;
    public static int frameIndex = 0;

    public static void GenerateAnimationExample() {
        String configPath = "animations";
        CustomConfig cf = new CustomConfig().setup(configPath);
        if (!cf.exist(configPath)) {
            FileConfiguration con = cf.get();
            String[] animArray = new String[]{"Frame 1", "Frame 2", "Frame 3"};
            con.addDefault("animations.0", animArray);
            con.options().copyDefaults(true);
            cf.save();
        }
        animationsConfig = cf.get();
    }

    public static String convertAnimatedText(String message) {
        Matcher matcher = animationPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String animationId = matcher.group(1);
            String animatedText = getAnimatedText(animationId);

            matcher.appendReplacement(buffer, animatedText);
        }

        matcher.appendTail(buffer);
        return buffer.toString();
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
        String configPath = "animations";
        CustomConfig cf = new CustomConfig().setup(configPath);
        animationsConfig = cf.get();
    }
}