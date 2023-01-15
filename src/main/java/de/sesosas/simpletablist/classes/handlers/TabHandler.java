package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class TabHandler {

    public static void UpdateTab(){
        try{
            for (Player player : Bukkit.getOnlinePlayers()) {

                if(!CurrentConfig.getBoolean("Tab.Worlds.Use")){
                    //TabList Header
                    if(CurrentConfig.getBoolean("Tab.Header.Use")){
                        if(CurrentConfig.getList("Tab.Header.Content") != null){
                            String headerString = "";
                            if(CurrentConfig.getList("Tab.Header.Content").size() >= 1){
                                for(Object str : CurrentConfig.getList("Tab.Header.Content")){
                                    headerString = headerString + str + "\n";
                                }
                            }
                            player.setPlayerListHeader(StringFormater.Get(headerString, player));
                        }
                    }

                    //TabList Footer
                    if(CurrentConfig.getBoolean("Tab.Footer.Use")){
                        if(CurrentConfig.getList("Tab.Header.Content") != null){
                            String footerString = "";
                            if(CurrentConfig.getList("Tab.Footer.Content").size() >= 1){
                                for(Object str : CurrentConfig.getList("Tab.Footer.Content")){
                                    footerString = footerString + "\n" + str;
                                }
                            }
                            player.setPlayerListFooter(StringFormater.Get(footerString, player));
                        }
                    }
                }
                else{
                    //TabList Header
                    if(CurrentConfig.getBoolean("Tab.Footer.Use")){
                        if(TabWBHandler.GetWorldConfig(player.getWorld(), "Header") != null){
                            String headerString = "";
                            List<String> head = (List<String>)TabWBHandler.GetWorldConfig(player.getWorld(), "Header");
                            if(head.size() >= 1){
                                for(Object str : head){
                                    headerString = headerString + str + "\n";
                                }
                            }
                            player.setPlayerListHeader(StringFormater.Get(headerString, player));
                        }
                    }

                    //TabList Footer
                    if(CurrentConfig.getBoolean("Tab.Footer.Use")){
                        if(TabWBHandler.GetWorldConfig(player.getWorld(), "Footer") != null){
                            String footerString = "";
                            List<String> foot = (List<String>)TabWBHandler.GetWorldConfig(player.getWorld(), "Footer");
                            if(foot.size() >= 1){
                                for(Object str : foot){
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
