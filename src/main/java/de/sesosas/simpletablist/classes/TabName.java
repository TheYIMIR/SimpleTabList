package de.sesosas.simpletablist.classes;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TabName {
    public static void Update(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListName(player.getDisplayName());
        }
    }
}
