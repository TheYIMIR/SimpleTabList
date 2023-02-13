/*
package de.sesosas.simpletablist.classes.handlers.notused;

import de.sesosas.simpletablist.classes.handlers.LPFunctionsHandler;
import de.sesosas.simpletablist.classes.handlers.NameHandler;
import de.sesosas.simpletablist.classes.handlers.PermissionsHandler;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TeamHandler {

    public static HashMap<Player, Team> playerTeam = new HashMap<>();

    public static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static Scoreboard board = manager.getNewScoreboard();

    public static void Generate(){
        for(int i = 0; i < 100; i++){
            if(board.getTeam(String.valueOf(i)) == null){
                board.registerNewTeam(String.valueOf(i));
            }
        }
    }

    public static void ApplyTeam(){
        try{
            for(Player player: Bukkit.getOnlinePlayers()){

                //Check all existing Teams.
                for(int i = 0; i < 100; i++){
                    //Leave all old teams
                    if(board.getTeam(String.valueOf(i)).hasEntry(player.getName())){
                        board.getTeam(String.valueOf(i)).removeEntry(player.getName());
                    }

                    board.getTeam(String.valueOf(i)).setAllowFriendlyFire(true);
                    board.getTeam(String.valueOf(i)).setCanSeeFriendlyInvisibles(false);

                    if(PermissionsHandler.hasPermission(player, "weight."+i)){
                        //Check if prefix is null
                        if(LPFunctionsHandler.getPrefix(player) != null){
                            board.getTeam(String.valueOf(i)).setPrefix(LPFunctionsHandler.getPrefix(player));
                        }
                        //Check if suffix is null
                        if(LPFunctionsHandler.getSuffix(player) != null) {
                            board.getTeam(String.valueOf(i)).setSuffix(LPFunctionsHandler.getSuffix(player));
                        }

                        //Add Player to team and add to HashMap to get the Team if needed somewhere else later
                        if(!board.getTeam(String.valueOf(i)).hasEntry(player.getName())){
                            board.getTeam(String.valueOf(i)).addEntry(player.getName());
                            playerTeam.put(player, board.getTeam(String.valueOf(i)));
                        }
                    }
                }
                player.setScoreboard(board);
                NameHandler.Update();
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}


 */