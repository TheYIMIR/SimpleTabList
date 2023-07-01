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

                    int playerWeight = LPFunctionsHandler.getPlayerGroupWeight(player);

                    String s = "000000".substring(String.valueOf(playerWeight).length()) + playerWeight + LPFunctionsHandler.getPlayerGroupName(player);

                    Team team = scoreboard.getTeam(s);
                    if (team == null) {
                        team = scoreboard.registerNewTeam(s);
                    }

                    if (LPFunctionsHandler.getPrefix(player) != null) {
                        tpref = LPFunctionsHandler.getPrefix(player);
                    }

                    if (LPFunctionsHandler.getSuffix(player) != null) {
                        tsuff = LPFunctionsHandler.getSuffix(player);
                    }

                    team.addEntry(player.getName());

                    if ((boolean)TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Use") && CurrentConfig.getBoolean("Worlds.Use")) {
                        String prefix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Prefix");
                        String suffix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Suffix");

                        String cprefix = "";
                        String csuffix = "";

                        if(!prefix.isEmpty()) {
                            cprefix = StringFormater.Get(tpref, player) + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(prefix, player) + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "");
                        }
                        else {
                            cprefix = StringFormater.Get(tpref, player) + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(prefix, player);
                        }

                        if(!suffix.isEmpty()){
                            csuffix = (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(suffix, player) + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(tsuff, player);
                        }
                        else {
                            csuffix = (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(tsuff, player);
                        }

                        player.setPlayerListName(cprefix + player.getName() + csuffix);
                    } else {
                        player.setPlayerListName(StringFormater.Get(tpref, player) + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + player.getName() + (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "") + StringFormater.Get(tsuff, player));
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
