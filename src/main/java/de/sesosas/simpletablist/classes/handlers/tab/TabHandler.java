package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class TabHandler {

    public static void UpdateTab(Player player){
        try{
                if(!CurrentConfig.getBoolean("Worlds.Use") ){
                    //TabList Header
                    if(CurrentConfig.getBoolean("Header.Use")){
                        if(CurrentConfig.getList("Header.Content") != null){
                            String headerString = "";
                            if(CurrentConfig.getList("Header.Content").size() >= 1){
                                for(Object str : CurrentConfig.getList("Header.Content")){
                                    headerString = headerString + str + "\n";
                                }
                            }
                            player.setPlayerListHeader(StringFormater.Get(headerString, player));
                        }
                    }

                    //TabList Footer
                    if(CurrentConfig.getBoolean("Footer.Use")){
                        if(CurrentConfig.getList("Header.Content") != null){
                            String footerString = "";
                            if(CurrentConfig.getList("Footer.Content").size() >= 1){
                                for(Object str : CurrentConfig.getList("Footer.Content")){
                                    footerString = footerString + "\n" + str;
                                }
                            }
                            player.setPlayerListFooter(StringFormater.Get(footerString, player));
                        }
                    }
                }
                else{
                    //TabList Header
                    if(CurrentConfig.getBoolean("Header.Use")){
                        if((boolean)TabWBHandler.GetWorldConfig(player.getWorld(), "Header.Use")){
                            if(TabWBHandler.GetWorldConfig(player.getWorld(), "Header.Content") != null){
                                String headerString = "";
                                List<String> head = (List<String>)TabWBHandler.GetWorldConfig(player.getWorld(), "Header.Content");
                                if(head.size() >= 1){
                                    for(Object str : head){
                                        headerString = headerString + str + "\n";
                                    }
                                }
                                player.setPlayerListHeader(StringFormater.Get(headerString, player));
                            }
                        }
                        else{
                            if(CurrentConfig.getList("Header.Content") != null){
                                String headerString = "";
                                if(CurrentConfig.getList("Header.Content").size() >= 1){
                                    for(Object str : CurrentConfig.getList("Header.Content")){
                                        headerString = headerString + str + "\n";
                                    }
                                }
                                player.setPlayerListHeader(StringFormater.Get(headerString, player));
                            }
                        }
                    }

                    //TabList Footer
                    if(CurrentConfig.getBoolean("Footer.Use")){
                        if((boolean)TabWBHandler.GetWorldConfig(player.getWorld(), "Footer.Use")){
                            if(TabWBHandler.GetWorldConfig(player.getWorld(), "Footer.Content") != null){
                                String footerString = "";
                                List<String> foot = (List<String>)TabWBHandler.GetWorldConfig(player.getWorld(), "Footer.Content");
                                if(foot.size() >= 1){
                                    for(Object str : foot){
                                        footerString = footerString + "\n" + str;
                                    }
                                }
                                player.setPlayerListFooter(StringFormater.Get(footerString, player));
                            }
                        }
                        else{
                            if(CurrentConfig.getList("Header.Content") != null){
                                String footerString = "";
                                if(CurrentConfig.getList("Footer.Content").size() >= 1){
                                    for(Object str : CurrentConfig.getList("Footer.Content")){
                                        footerString = footerString + "\n" + str;
                                    }
                                }
                                player.setPlayerListFooter(StringFormater.Get(footerString, player));
                            }
                        }
                    }
                }
        }
        catch (Exception e){

            System.out.println("Found an error at Header or Footer config section! Please make sure there are lists with content!");
            System.out.println(e);

        }
    }
}
