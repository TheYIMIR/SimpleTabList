package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TabName {
    public static void Update(){
        if(!SimpleTabList.getPlugin().config.getBoolean("Tab.Names.Use")) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Nametag.luckpermsName(player);
            player.setPlayerListName(player.getDisplayName());
        }
    }
}
