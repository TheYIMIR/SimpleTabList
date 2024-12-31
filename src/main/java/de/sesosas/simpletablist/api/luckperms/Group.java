package de.sesosas.simpletablist.api.luckperms;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.OptionalInt;

public class Group {
    public static int getPlayerGroupWeight(Player player) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            String primaryGroupName = user.getPrimaryGroup();
            if (primaryGroupName != null) {
                net.luckperms.api.model.group.Group group = LuckPermsProvider.get().getGroupManager().getGroup(primaryGroupName);
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
                net.luckperms.api.model.group.Group group = LuckPermsProvider.get().getGroupManager().getGroup(primaryGroupName);
                if (group != null) {
                    return group.getName();
                }
            }
        }
        return "";
    }
}
