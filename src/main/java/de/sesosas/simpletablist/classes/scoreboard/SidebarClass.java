package de.sesosas.simpletablist.classes.scoreboard;

import de.sesosas.simpletablist.api.luckperms.Permission;
import de.sesosas.simpletablist.api.utils.StringUtil;
import de.sesosas.simpletablist.classes.AnimationClass;
import de.sesosas.simpletablist.config.SidebarConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Manages sidebar scoreboards for players with animation support
 */
public class SidebarClass {
    private static final Map<UUID, Scoreboard> playerScoreboards = new HashMap<>();
    private static final Set<UUID> disabledSidebars = new HashSet<>();
    private static boolean isPerPlayerEnabled = false;
    private static String perPlayerPermission = "stl.sidebar";

    /**
     * Initialize the sidebar manager
     */
    public static void initialize() {
        isPerPlayerEnabled = SidebarConfig.getBoolean("Sidebar.PerPlayer.Enable");
        perPlayerPermission = SidebarConfig.getString("Sidebar.PerPlayer.Permission");
        Bukkit.getLogger().info("[SimpleTabList] Sidebar manager initialized");
    }

    /**
     * Check if a player should have a sidebar
     * @param player Player to check
     * @return True if the player should have a sidebar
     */
    private static boolean shouldHaveSidebar(Player player) {
        // First check if player has manually disabled their sidebar
        if (disabledSidebars.contains(player.getUniqueId())) {
            return false;
        }

        // Master toggle check
        if (!SidebarConfig.getBoolean("Sidebar.Enable")) {
            return false;
        }

        // Per-player permission check
        if (isPerPlayerEnabled && !Permission.hasPermission(player, perPlayerPermission)) {
            return false;
        }

        // World-specific check
        String worldName = player.getWorld().getName();
        if (SidebarConfig.getBoolean("Sidebar.PerWorld.Enable")) {
            return SidebarConfig.getWorldBoolean(worldName, "Sidebar.Enable");
        }

        return true;
    }

    /**
     * Creates or updates a sidebar scoreboard for a player
     * @param player The player to update the scoreboard for
     */
    public static void updateSidebar(Player player) {
        if (!shouldHaveSidebar(player)) {
            removeSidebar(player);
            return;
        }

        try {
            // Get the player's existing scoreboard or create a new one
            Scoreboard scoreboard = player.getScoreboard();
            if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
                // Player has the main scoreboard, create a new one
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                player.setScoreboard(scoreboard);
                playerScoreboards.put(player.getUniqueId(), scoreboard);
            }

            // Remove any existing sidebar objective
            if (scoreboard.getObjective("stlsidebar") != null) {
                scoreboard.getObjective("stlsidebar").unregister();
            }

            // Create a new sidebar objective
            String worldName = player.getWorld().getName();
            String title;

            // Get the title from the world config if enabled, otherwise from the main config
            if (SidebarConfig.getBoolean("Sidebar.PerWorld.Enable") &&
                    SidebarConfig.getWorldValue(worldName, "Sidebar.Title") != null) {
                title = SidebarConfig.getWorldString(worldName, "Sidebar.Title");
            } else {
                title = SidebarConfig.getString("Sidebar.Title");
            }

            // Process the title for animations
            title = processAnimations(title, player);

            Objective sidebar = scoreboard.registerNewObjective("stlsidebar", "dummy",
                    StringUtil.Convert(title, player));
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

            // Get the sidebar lines
            List<String> lines;
            if (SidebarConfig.getBoolean("Sidebar.PerWorld.Enable") &&
                    SidebarConfig.getWorldValue(worldName, "Sidebar.Lines") != null) {
                lines = SidebarConfig.getWorldStringList(worldName, "Sidebar.Lines");
            } else {
                lines = SidebarConfig.getStringList("Sidebar.Lines");
            }

            if (lines != null && !lines.isEmpty()) {
                String blankLineChar = SidebarConfig.getString("Sidebar.Format.BlankLineChar");
                boolean lineSpacing = SidebarConfig.getBoolean("Sidebar.Format.LineSpacing");

                // Add each line to the scoreboard, starting from the bottom
                int score = lines.size();
                for (String line : lines) {
                    // Process the line for animations
                    String processedLine = processAnimations(line, player);

                    // Check if this is a blank line (just a color code)
                    if (processedLine.length() <= 2 && processedLine.startsWith("&")) {
                        processedLine = blankLineChar;
                    }

                    // Add spacing if enabled
                    if (lineSpacing && !processedLine.equals(blankLineChar)) {
                        processedLine = " " + processedLine + " ";
                    }

                    // Create unique line entries
                    if (processedLine.length() > 0) {
                        String uniqueLine = makeLineUnique(processedLine, score);
                        Score lineScore = sidebar.getScore(uniqueLine);
                        lineScore.setScore(score);
                    }
                    score--;
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[SimpleTabList] Error updating sidebar for " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Process animations in a string
     * @param text Text potentially containing animations
     * @param player Player for placeholders
     * @return Processed text with animations replaced
     */
    private static String processAnimations(String text, Player player) {
        if (text == null) return "";

        // First process animations
        String processedText = AnimationClass.convertAnimatedText(text);

        // Then process placeholders
        return StringUtil.Convert(processedText, player);
    }

    /**
     * Makes a scoreboard line unique by adding invisible color codes
     * @param line The line to make unique
     * @param index The index to use for uniqueness
     * @return A unique line
     */
    private static String makeLineUnique(String line, int index) {
        // Get the last color code used in the line
        ChatColor lastColor = ChatColor.WHITE; // Default
        for (int i = line.length() - 2; i >= 0; i--) {
            if (line.charAt(i) == '§' && i + 1 < line.length()) {
                char colorChar = line.charAt(i + 1);
                ChatColor color = ChatColor.getByChar(colorChar);
                if (color != null) {
                    lastColor = color;
                    break;
                }
            }
        }

        // Add invisible unique identifier
        String uniqueStr = "";
        for (int i = 0; i < index % 3; i++) {
            uniqueStr += ChatColor.RESET.toString();
        }

        // Truncate if needed (scoreboard lines have a max length)
        int maxLength = 40; // Minecraft limit is 48, but we leave some room
        if (line.length() > maxLength) {
            line = line.substring(0, maxLength - uniqueStr.length());
        }

        return line + uniqueStr + lastColor.toString();
    }

    /**
     * Removes the sidebar for a player
     * @param player The player to remove the sidebar for
     */
    public static void removeSidebar(Player player) {
        try {
            Scoreboard scoreboard = player.getScoreboard();
            if (scoreboard.getObjective("stlsidebar") != null) {
                scoreboard.getObjective("stlsidebar").unregister();
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[SimpleTabList] Error removing sidebar for " + player.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Removes all sidebars and cleans up resources
     */
    public static void removeAllSidebars() {
        for (UUID uuid : playerScoreboards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                removeSidebar(player);
            }
        }
        playerScoreboards.clear();
        disabledSidebars.clear();
    }

    /**
     * Set a player back to the main scoreboard and remove their sidebar
     * @param player The player to reset
     */
    public static void resetPlayerScoreboard(Player player) {
        removeSidebar(player);
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            player.setScoreboard(manager.getMainScoreboard());
        }
        playerScoreboards.remove(player.getUniqueId());
    }

    /**
     * Toggle sidebar visibility for a specific player
     * @param player The player to toggle sidebar for
     * @return True if the sidebar is now visible, false otherwise
     */
    public static boolean toggleSidebar(Player player) {
        UUID playerUuid = player.getUniqueId();

        if (disabledSidebars.contains(playerUuid)) {
            // Sidebar is currently disabled, enable it
            disabledSidebars.remove(playerUuid);
            updateSidebar(player);
            return true;
        } else {
            // Sidebar is currently enabled, disable it
            disabledSidebars.add(playerUuid);
            removeSidebar(player);
            return false;
        }
    }

    /**
     * Check if a player has their sidebar disabled
     * @param player The player to check
     * @return True if the player has disabled their sidebar
     */
    public static boolean isSidebarDisabled(Player player) {
        return disabledSidebars.contains(player.getUniqueId());
    }

    /**
     * Handle a player leaving the server
     * @param player The player who is leaving
     */
    public static void handlePlayerQuit(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        // We intentionally don't remove from disabledSidebars to remember their preference
    }
}