package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.classes.StringFormater;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LPFunctionsHandler {

    public static String getPrefix(Player player){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(player);

            if(metaData.getPrefix() != null && !metaData.getPrefix().equalsIgnoreCase("") && !metaData.getPrefix().equalsIgnoreCase("null")) {
                return StringFormater.Get(metaData.getPrefix());
            }
        }
        else{
            System.out.println("Didn't found LuckPerms which is necessary for this Plugin!");
        }
        return null;
    }

    public static String getSuffix(Player player) {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            CachedMetaData metaData = api.getPlayerAdapter(Player.class).getMetaData(player);

            if(metaData.getSuffix() != null && !metaData.getSuffix().equalsIgnoreCase("") && !metaData.getPrefix().equalsIgnoreCase("null")){
                return StringFormater.Get(metaData.getSuffix());
            }
        }
        else{
            System.out.println("Didn't found LuckPerms which is necessary for this Plugin!");
        }
        return null;
    }
}
