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
    public static Scoreboard scoreboard;

    public static void initScoreboard(){
        NameHandler.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public static void Update(){
        try {
            if(CurrentConfig.getBoolean("Names.Use")) {

                List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                playerList.sort(Comparator.comparingInt(NameHandler::getPlayerGroupWeight));

                for (int i = 0; i < playerList.size(); i++) {
                    Player player = playerList.get(i);
                    String spaces = (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Use") ? " " : "");
                    String gpref = "";
                    String gsuff = "";
                    String tpref = "";
                    String tsuff = "";
                    String wpref = "";
                    String wsuff = "";

                    int playerWeight = LPFunctionsHandler.getPlayerGroupWeight(player);

                    String s = "000000".substring(String.valueOf(playerWeight).length()) + playerWeight + LPFunctionsHandler.getPlayerGroupName(player);

                    Team team = scoreboard.getTeam(s);

                    if (team == null) {
                        team = scoreboard.registerNewTeam(s);
                    }

                    if (LPFunctionsHandler.getPrefix(player) != null && CurrentConfig.getBoolean("Names.LuckPerm.Prefix")) {
                        tpref = StringFormater.Get(LPFunctionsHandler.getPrefix(player), player) + spaces;
                    }

                    if (LPFunctionsHandler.getSuffix(player) != null && CurrentConfig.getBoolean("Names.LuckPerm.Suffix")) {
                        tsuff = spaces + StringFormater.Get(LPFunctionsHandler.getSuffix(player), player);
                    }

                    if (CurrentConfig.getBoolean("Names.Global.Use")) {
                        gpref = StringFormater.Get(CurrentConfig.getString("Names.Global.Prefix"), player) + spaces;
                        gsuff = spaces + StringFormater.Get(CurrentConfig.getString("Names.Global.Suffix"), player);
                    }

                    team.addEntry(player.getName());

                    if ((boolean)TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Use") && CurrentConfig.getBoolean("Worlds.Use")) {
                        String prefix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Prefix");
                        String suffix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Suffix");

                        if(!prefix.isEmpty()) {
                            wpref = StringFormater.Get(prefix, player) + spaces;
                        }

                        if(!suffix.isEmpty()){
                            wsuff = spaces + StringFormater.Get(suffix, player);
                        }
                    }

                    player.setPlayerListName(tpref + gpref +  wpref + player.getName() + wsuff + gsuff + tsuff);
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
