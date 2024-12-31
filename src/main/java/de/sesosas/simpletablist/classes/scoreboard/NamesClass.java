package de.sesosas.simpletablist.classes.scoreboard;

import de.sesosas.simpletablist.api.luckperms.Group;
import de.sesosas.simpletablist.api.luckperms.Permission;
import de.sesosas.simpletablist.api.utils.StringUtil;
import de.sesosas.simpletablist.config.CurrentConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class NamesClass {
    private static org.bukkit.scoreboard.Scoreboard mainScoreboard;
    private static org.bukkit.scoreboard.Scoreboard scoreboard;

    public static void initScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public static void updatePlayerName(Player player) {
        if(Permission.getPermissionString(player, "stl.format.") != null) {
            player.setPlayerListName(StringUtil.Convert(Permission.getPermissionString(player, "stl.format."), player));
        }
        else{
            player.setPlayerListName(StringUtil.Convert(CurrentConfig.getString("Names.Format.Default"), player));
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
            int playerWeight = Group.getPlayerGroupWeight(player);
            int sortingPrefix = isAscending ? playerWeight : Integer.MAX_VALUE - playerWeight;
            teamName = String.valueOf(sortingPrefix) + Group.getPlayerGroupName(player);
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

    public static void resetPlayerNames() {
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : playerList) {
            player.setPlayerListName(player.getName());
            player.setScoreboard(mainScoreboard);
        }
    }
}
