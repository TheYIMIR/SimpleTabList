package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import de.sesosas.simpletablist.classes.handlers.lp.LPFunctionsHandler;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class NameHandler {
    private static Scoreboard scoreboard;

    public static void initScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public static void Update() {
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
            System.out.println(e);
        }
    }

    private static void updatePlayerName(Player player) {
        String spaces = (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Enable") ? " " : "");

        String gpref = "", gsuff = "", tpref = "", tsuff = "", wpref = "", wsuff = "";

        if (CurrentConfig.getBoolean("Names.LuckPerms.Prefix.Enable")) {
            tpref = StringFormater.Get(LPFunctionsHandler.getPrefix(player), player) + spaces;
        }

        if (CurrentConfig.getBoolean("Names.LuckPerms.Suffix.Enable")) {
            tsuff = spaces + StringFormater.Get(LPFunctionsHandler.getSuffix(player), player);
        }

        if (CurrentConfig.getBoolean("Names.Global.Enable")) {
            gpref = StringFormater.Get(CurrentConfig.getString("Names.Global.Prefix"), player) + spaces;
            gsuff = spaces + StringFormater.Get(CurrentConfig.getString("Names.Global.Suffix"), player);
        }

        if ((boolean) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Enable")
                && CurrentConfig.getBoolean("Worlds.Enable")) {
            String prefix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Prefix");
            String suffix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Suffix");

            if (!prefix.isEmpty()) {
                wpref = StringFormater.Get(prefix, player) + spaces;
            }

            if (!suffix.isEmpty()) {
                wsuff = spaces + StringFormater.Get(suffix, player);
            }
        }

        player.setPlayerListName(tpref + gpref + wpref + player.getName() + wsuff + gsuff + tsuff);

        sortPlayer(player);
    }

    private static void assignPlayerToTeam(Player player, String sortType, boolean isAscending) {
        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
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

    private static void sortPlayer(Player player) {
        if (CurrentConfig.getBoolean("Names.Sorting.Enable")) {
            boolean isAscending = CurrentConfig.getBoolean("Names.Sorting.Ascending");
            String sortType = CurrentConfig.getString("Names.Sorting.Type");

            assignPlayerToTeam(player, sortType, isAscending);
        } else {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    private static void resetPlayerNames() {
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : playerList) {
            player.setPlayerListName(player.getName());
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }
}
