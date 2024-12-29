package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import de.sesosas.simpletablist.classes.handlers.lp.LPFunctionsHandler;
import de.sesosas.simpletablist.classes.handlers.lp.PermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NameHandler {
    private static Scoreboard mainScoreboard;
    private static Scoreboard scoreboard;

    public static void initScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public static synchronized void Update() {
        try {
            if (CurrentConfig.getBoolean("Names.Enable")) {
                List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

                for (Player player : playerList) {
                    updatePlayerName(player);
                }
            } else {
                resetPlayerNames();
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning((Supplier<String>) e);
        }
    }

    private static void updatePlayerName(Player player) {
        if(PermissionsHandler.getPermissionString(player, "stl.format.") != null) {
            player.setPlayerListName(StringFormater.Get(PermissionsHandler.getPermissionString(player, "stl.format."), player));
        }
        else{
            player.setPlayerListName(StringFormater.Get(CurrentConfig.getString("Names.Format.Default"), player));
        }

        sortPlayer(player);
    }

    private static void sortPlayer(Player player) {
        if (CurrentConfig.getBoolean("Names.Sorting.Enable")) {
            boolean isAscending = CurrentConfig.getBoolean("Names.Sorting.Ascending");
            String sortType = CurrentConfig.getString("Names.Sorting.Type");

            assignPlayerToTeam(player, sortType, isAscending);
        } else {
            player.setScoreboard(mainScoreboard);
        }
    }

    private static void assignPlayerToTeam(Player player, String sortType, boolean isAscending) {
        Team mainTeam = mainScoreboard.getEntryTeam(player.getName());
        String teamName = "";

        if (sortType.equalsIgnoreCase("weight")) {
            int playerWeight = LPFunctionsHandler.getPlayerGroupWeight(player);
            int sortingPrefix = isAscending ? playerWeight : Integer.MAX_VALUE - playerWeight;
            teamName = String.valueOf(sortingPrefix) + LPFunctionsHandler.getPlayerGroupName(player);
        }

        if (!teamName.isEmpty()) {
            if (mainTeam != null) {
                teamName += "_" + player.getName();
            }

            Team team = scoreboard.getTeam(teamName);

            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
            }

            team.addEntry(player.getName());

            if (mainTeam != null) {
                team.setAllowFriendlyFire(mainTeam.allowFriendlyFire());
                team.setCanSeeFriendlyInvisibles(mainTeam.canSeeFriendlyInvisibles());
                team.setColor(mainTeam.getColor());
                team.setPrefix(mainTeam.getPrefix());
                team.setSuffix(mainTeam.getSuffix());

                team.setOption(Team.Option.COLLISION_RULE, mainTeam.getOption(Team.Option.COLLISION_RULE));
                team.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, mainTeam.getOption(Team.Option.DEATH_MESSAGE_VISIBILITY));
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, mainTeam.getOption(Team.Option.NAME_TAG_VISIBILITY));
            }

            player.setScoreboard(scoreboard);
        }

    }

    private static void resetPlayerNames() {
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : playerList) {
            player.setPlayerListName(player.getName());
            player.setScoreboard(mainScoreboard);
        }
    }
}
