package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import de.sesosas.simpletablist.classes.handlers.lp.LPFunctionsHandler;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NameHandler {
    public static void Update(){
        try {
            if(CurrentConfig.getBoolean("Names.Use")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String tpref = "";
                    String tsuff = "";

                    if (LPFunctionsHandler.getPrefix(player) != null) {
                        tpref = LPFunctionsHandler.getPrefix(player);
                    }
                    if (LPFunctionsHandler.getSuffix(player) != null) {
                        tsuff = LPFunctionsHandler.getSuffix(player);
                    }

                    if (CurrentConfig.getBoolean("Worlds.Names.Use")) {
                        String prefix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Prefix");
                        String suffix = (String) TabWBHandler.GetWorldConfig(player.getWorld(), "Suffix");
                        player.setPlayerListName(StringFormater.Get(tpref, player) + " " + StringFormater.Get(prefix, player) + " " + player.getName() + " " + StringFormater.Get(suffix, player) + " " + StringFormater.Get(tsuff, player));
                    } else {
                        player.setPlayerListName(StringFormater.Get(tpref, player) + " " + player.getName() + " " + StringFormater.Get(tsuff, player));
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
