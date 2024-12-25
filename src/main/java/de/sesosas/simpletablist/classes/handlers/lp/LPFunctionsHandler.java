package de.sesosas.simpletablist.classes.handlers.lp;

import de.sesosas.simpletablist.classes.StringFormater;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;
import java.util.OptionalInt;

public class LPFunctionsHandler {

    public static int getPlayerGroupWeight(Player player) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            String primaryGroupName = user.getPrimaryGroup();
            if (primaryGroupName != null) {
                Group group = LuckPermsProvider.get().getGroupManager().getGroup(primaryGroupName);
                if (group != null) {
                    OptionalInt weight = group.getWeight();
                    if (weight.isPresent()) {
                        return weight.getAsInt();
                    }
                }
            }
        }
        return 0;
    }

    public static String getPlayerGroupName(Player player) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            String primaryGroupName = user.getPrimaryGroup();
            if (primaryGroupName != null) {
                Group group = LuckPermsProvider.get().getGroupManager().getGroup(primaryGroupName);
                if (group != null) {
                    return group.getName();
                }
            }
        }
        return "";
    }
}
