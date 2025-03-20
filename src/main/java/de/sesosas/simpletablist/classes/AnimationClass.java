package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.config.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnimationClass {
    private static final Pattern animationPattern = Pattern.compile("\\{animation:(\\d+)}");
    private static FileConfiguration animationsConfig = null;
    public static int frameIndex = 0;

    public static void GenerateAnimationExample() {
        String configPath = "animations";
        CustomConfig cf = new CustomConfig().setup(configPath);

        if (cf.isEmpty() || !cf.exist(configPath)) {
            FileConfiguration con = cf.get();

            // Basic animation example for tablist
            List<String> animArray = new ArrayList<>(Arrays.asList("Frame 1", "Frame 2", "Frame 3"));
            con.set("animations.0", animArray);

            // Time animation for sidebar (day cycle)
            List<String> timeArray = new ArrayList<>(Arrays.asList(
                    "Day", "Dusk", "Night", "Dawn"
            ));
            con.set("animations.1", timeArray);

            // Information animation for sidebar (rotating messages)
            List<String> infoArray = new ArrayList<>(Arrays.asList(
                    "&eWelcome to the server!",
                    "&aDon't forget to vote!",
                    "&bJoin our Discord!",
                    "&6Check out our website!"
            ));
            con.set("animations.2", infoArray);

            // Color animation for sidebar (changing colors)
            List<String> colorArray = new ArrayList<>(Arrays.asList(
                    "&c✦",
                    "&e✦",
                    "&a✦",
                    "&b✦",
                    "&9✦",
                    "&d✦"
            ));
            con.set("animations.3", colorArray);

            // Progress bar animation for sidebar
            List<String> progressArray = new ArrayList<>(Arrays.asList(
                    "&8[&a█&8          ]",
                    "&8[&a██&8         ]",
                    "&8[&a███&8        ]",
                    "&8[&a████&8       ]",
                    "&8[&a█████&8      ]",
                    "&8[&a██████&8     ]",
                    "&8[&a███████&8    ]",
                    "&8[&a████████&8   ]",
                    "&8[&a█████████&8  ]",
                    "&8[&a██████████&8 ]",
                    "&8[&a███████████&8]"
            ));
            con.set("animations.4", progressArray);

            try {
                con.save(new File(Bukkit.getServer().getPluginManager()
                        .getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), configPath + ".yml"));
                Bukkit.getLogger().info("[SimpleTabList] Generated animations configuration with examples");
            } catch (IOException e) {
                Bukkit.getLogger().severe("[SimpleTabList] Error saving animations configuration.");
                e.printStackTrace();
            }
        }

        // Load the config regardless of whether we just created it
        animationsConfig = cf.get();

        // Verify that the animations were properly loaded
        if (animationsConfig.getList("animations.0") == null || animationsConfig.getList("animations.0").isEmpty()) {
            Bukkit.getLogger().warning("[SimpleTabList] Animations configuration appears to be empty. Adding default animations.");

            // Add default animations if they're missing
            List<String> defaultAnim = new ArrayList<>(Arrays.asList("Frame 1", "Frame 2", "Frame 3"));
            animationsConfig.set("animations.0", defaultAnim);

            try {
                animationsConfig.save(new File(Bukkit.getServer().getPluginManager()
                        .getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), configPath + ".yml"));
                Bukkit.getLogger().info("[SimpleTabList] Default animations added");
            } catch (IOException e) {
                Bukkit.getLogger().severe("[SimpleTabList] Error saving animations configuration.");
                e.printStackTrace();
            }
        }
    }

    public static String convertAnimatedText(String message) {
        if (message == null) return "";

        Matcher matcher = animationPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String animationId = matcher.group(1);
            String animatedText = getAnimatedText(animationId);

            matcher.appendReplacement(buffer, Matcher.quoteReplacement(animatedText));
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String getAnimatedText(String animationId) {
        if (animationsConfig == null) {
            loadAnimationsConfig();
        }

        if (animationsConfig.contains("animations." + animationId)) {
            List<String> frames = animationsConfig.getStringList("animations." + animationId);

            int frameCount = frames.size();
            if (frameCount == 0) return "No frames";

            int currentIndex = frameIndex % frameCount;
            return frames.get(currentIndex);
        }
        return "Unknown animation: " + animationId;
    }

    public static void loadAnimationsConfig() {
        String configPath = "animations";
        CustomConfig cf = new CustomConfig().setup(configPath);
        animationsConfig = cf.get();

        // Verify animations are loaded
        if (animationsConfig == null || !animationsConfig.contains("animations.0")) {
            Bukkit.getLogger().warning("[SimpleTabList] Failed to load animations. Regenerating default animations.");
            GenerateAnimationExample();
        }
    }
}