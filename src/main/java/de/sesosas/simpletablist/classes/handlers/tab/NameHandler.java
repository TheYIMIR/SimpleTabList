package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import de.sesosas.simpletablist.classes.handlers.lp.LPFunctionsHandler;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NameHandler {

    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    public static void Update(){
        try {
            if(CurrentConfig.getBoolean("Names.Use")) {

                List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                playerList.sort(Comparator.comparingInt(NameHandler::getPlayerGroupWeight));

                for (int i = 0; i < playerList.size(); i++) {
                    Player player = playerList.get(i);
                    String tpref = "";
                    String tsuff = "";

                    Team team = scoreboard.getTeam("sorted_" + i);
                    if (team == null) {
                        team = scoreboard.registerNewTeam("sorted_" + i);
                    }

                    if (LPFunctionsHandler.getPrefix(player) != null) {
                        tpref = LPFunctionsHandler.getPrefix(player);
                    }
                    if (LPFunctionsHandler.getSuffix(player) != null) {
                        tsuff = LPFunctionsHandler.getSuffix(player);
                    }

                    team.setPrefix(StringFormater.Get(tpref, player) + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "")); // Set the player's prefix here
                    team.setSuffix((SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(tsuff, player)); // Set the player's suffix here
                    team.addEntry(player.getName());

                    if ((boolean)TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Use") && CurrentConfig.getBoolean("Worlds.Use")) {
                        String prefix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Prefix");
                        String suffix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Suffix");
                        player.setPlayerListName(StringFormater.Get(prefix, player) + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + player.getName() + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(suffix, player));
                    } else {
                        player.setPlayerListName(player.getName());
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private static int getPlayerGroupWeight(Player player) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            String primaryGroupName = user.getPrimaryGroup();
            if (primaryGroupName != null) {
                Group group = LuckPermsProvider.get().getGroupManager().getGroup(primaryGroupName);
                if (group != null) {
                    int weight = group.getWeight().orElse(0);
                    return weight;
                }
            }
        }
        return 0;
    }
}
