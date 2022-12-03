package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.Nametag;
import de.sesosas.simpletablist.classes.StringFormater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabHandler {

    public static void UpdateName(){
        if(!CurrentConfig.getBoolean("Tab.Names.Use")) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Nametag.setDisplayname(player);
            player.setPlayerListName(player.getDisplayName());
        }
    }

    public static void UpdateTab(){
        try{

            for (Player player : Bukkit.getOnlinePlayers()) {

                if(CurrentConfig.getBoolean("Tab.Header.Use")){
                    if(CurrentConfig.getList("Tab.Header.Content") != null){
                        //TabList Header
                        String headerString = "";
                        if(CurrentConfig.getList("Tab.Header.Content").size() >= 1){
                            for(Object str : CurrentConfig.getList("Tab.Header.Content")){
                                headerString = headerString + str + "\n";
                            }
                        }
                        player.setPlayerListHeader(StringFormater.Get(headerString, player));
                    }
                }

                if(CurrentConfig.getBoolean("Tab.Footer.Use")){
                    if(CurrentConfig.getList("Tab.Header.Content") != null){
                        //TabList Footer
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

        }
        catch (Exception e){

            System.out.println("Found an error at Header or Footer config section! Please make sure there are lists with content!");
            System.out.println(e);

        }
    }
}
