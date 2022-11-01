package de.sesosas.simpletablist.classes;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Nametag {

    public static void luckpermsName(Player player){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(player);

            String name = player.getName();
            String prefix = "";
            String suffix = "";

            if(metaData.getPrefix() != null && !metaData.getPrefix().equalsIgnoreCase("") && !metaData.getPrefix().equalsIgnoreCase("null")){
                prefix = StringFormater.Get(metaData.getPrefix());
            }
            if(metaData.getSuffix() != null && !metaData.getSuffix().equalsIgnoreCase("") && !metaData.getPrefix().equalsIgnoreCase("null")){
                suffix = StringFormater.Get(metaData.getSuffix());
            }

            player.setDisplayName(prefix + name + suffix);
            player.setCustomName(prefix + name + suffix);
            player.setCustomNameVisible(true);
        }
        else{
            System.out.println("Didn't found LuckPerms which is necessary for this Plugin!");
        }
    }

}
