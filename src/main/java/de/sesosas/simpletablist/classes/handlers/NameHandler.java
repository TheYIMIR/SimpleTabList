package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class NameHandler {
    public static void Update(){
        try {
            for(Player player : Bukkit.getOnlinePlayers()){
                String tpref = "";
                String tsuff = "";
                if(TeamHandler.playerTeam.get(player) != null){
                    tpref = TeamHandler.board.getTeam(TeamHandler.playerTeam.get(player).getName()).getPrefix();
                    tsuff = TeamHandler.board.getTeam(TeamHandler.playerTeam.get(player).getName()).getSuffix();
                }

                if(CurrentConfig.getBoolean("Tab.Names.Worlds.Use")){
                    String prefix = (String)TabWBHandler.GetWorldConfig(player.getWorld(), "Prefix");
                    String suffix = (String)TabWBHandler.GetWorldConfig(player.getWorld(), "Suffix");
                    player.setPlayerListName(tpref + prefix + player.getName() + suffix + tsuff);
                }
                else{
                    player.setPlayerListName(tpref + player.getName() + tsuff);
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
