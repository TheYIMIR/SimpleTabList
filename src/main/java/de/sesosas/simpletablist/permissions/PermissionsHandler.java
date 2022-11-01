package de.sesosas.simpletablist.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.data.DataType;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class PermissionsHandler {

    public static boolean hasPermission(Player player, String permission) {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
        }
        else{
            return false;
        }
    }

    public static boolean hasPermissionEnabled(Player player, String permission){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            if(user.getCachedData().getPermissionData().checkPermission(permission).asBoolean()){
                return Node.builder(permission).build().getValue();
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public static void addPermission(Player player, String permission) {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            user.data().add(Node.builder(permission).build());
            api.getUserManager().saveUser(user);
        }
    }

    public static void removePermission(Player player, String permission){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            user.data().remove(Node.builder(permission).build());
            api.getUserManager().saveUser(user);
        }
    }

}
