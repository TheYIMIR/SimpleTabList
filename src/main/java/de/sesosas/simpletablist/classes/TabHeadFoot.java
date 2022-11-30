package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TabHeadFoot {
    public static void Update(){
        try{

            for (Player player : Bukkit.getOnlinePlayers()) {

                if(SimpleTabList.getPlugin().config.getBoolean("Tab.Header.Use")){
                    if(SimpleTabList.getPlugin().config.getList("Tab.Header.Content") != null){
                        //TabList Header
                        String headerString = "";
                        if(SimpleTabList.getPlugin().config.getList("Tab.Header.Content").size() >= 1){
                            for(Object str : SimpleTabList.getPlugin().config.getList("Tab.Header.Content")){
                                headerString = headerString + str + "\n";
                            }
                        }
                        player.setPlayerListHeader(StringFormater.Get(headerString, player));
                    }
                }

                if(SimpleTabList.getPlugin().config.getBoolean("Tab.Footer.Use")){
                    if(SimpleTabList.getPlugin().config.getList("Tab.Header.Content") != null){
                        //TabList Footer
                        String footerString = "";
                        if(SimpleTabList.getPlugin().config.getList("Tab.Footer.Content").size() >= 1){
                            for(Object str : SimpleTabList.getPlugin().config.getList("Tab.Footer.Content")){
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
