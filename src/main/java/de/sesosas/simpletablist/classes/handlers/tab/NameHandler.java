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
import java.util.Objects;

public class NameHandler {
    public static Scoreboard scoreboard;

    public static void initScoreboard(){
        NameHandler.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public static void Update(){
        try {
            if(CurrentConfig.getBoolean("Names.Enable")) {

                List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                playerList.sort(Comparator.comparingInt(player -> translatePlayerWeight(LPFunctionsHandler.getPlayerGroupWeight(player))));

                for (int i = 0; i < playerList.size(); i++) {
                    Player player = playerList.get(i);

                    String spaces = (SimpleTabList.getPlugin().config.getBoolean("Names.Space.Enable") ? " " : "");

                    String gpref = "", gsuff = "", tpref = "", tsuff = "", wpref = "", wsuff = "";

                    int playerWeight = translatePlayerWeight(LPFunctionsHandler.getPlayerGroupWeight(player));

                    String s = playerWeight + LPFunctionsHandler.getPlayerGroupName(player);

                    Team team = scoreboard.getTeam(s);

                    if (team == null) {
                        team = scoreboard.registerNewTeam(s);
                    }

                    if (LPFunctionsHandler.getPrefix(player) != null && CurrentConfig.getBoolean("Names.LuckPerms.Prefix.Enable")) {
                        tpref = StringFormater.Get(LPFunctionsHandler.getPrefix(player), player) + spaces;
                    }

                    if (LPFunctionsHandler.getSuffix(player) != null && CurrentConfig.getBoolean("Names.LuckPerms.Suffix.Enable")) {
                        tsuff = spaces + StringFormater.Get(LPFunctionsHandler.getSuffix(player), player);
                    }

                    if (CurrentConfig.getBoolean("Names.Global.Enable")) {
                        gpref = StringFormater.Get(CurrentConfig.getString("Names.Global.Prefix"), player) + spaces;
                        gsuff = spaces + StringFormater.Get(CurrentConfig.getString("Names.Global.Suffix"), player);
                    }

                    team.addEntry(player.getName());

                    if ((boolean)TabWBHandler.GetWorldConfig(player.getWorld(), "Names.Enable") && CurrentConfig.getBoolean("Worlds.Enable")) {
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
            else{
                List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                playerList.sort(Comparator.comparingInt(player -> translatePlayerWeight(LPFunctionsHandler.getPlayerGroupWeight(player))));
                for (int i = 0; i < playerList.size(); i++) {
                    Player player = playerList.get(i);
                    player.setPlayerListName(player.getName());
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private static int translatePlayerWeight(int weight) {
        if(weight != 0){
            return Integer.MAX_VALUE - weight;
        }
        return Integer.MAX_VALUE;
    }
}
