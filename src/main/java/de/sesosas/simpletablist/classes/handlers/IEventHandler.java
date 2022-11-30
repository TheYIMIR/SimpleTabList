package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.SimpleTabList;
import de.sesosas.simpletablist.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class IEventHandler implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        TabName.Update();
        if(SimpleTabList.getPlugin().config.getBoolean("Event.Use")){
            new UpdateHandler(SimpleTabList.getPlugin(), 101989).getVersion(version -> {
                if (!SimpleTabList.getPlugin().getDescription().getVersion().equals(version)) {
                    if(event.getPlayer().isOp()){
                        MessageHandler.Send(event.getPlayer(), ChatColor.AQUA + "There is a new update available.");
                    }
                }
            });

            if(SimpleTabList.getPlugin().config.getBoolean("Plugin.ActionbarMessage")){
                MessageHandler.Send(event.getPlayer(), SimpleTabList.getPlugin().config.getString("Event.JoinMessage"));
                event.setJoinMessage("");
            }
            else{
                String jm = StringFormater.Get(SimpleTabList.getPlugin().config.getString("Event.JoinMessage"), event.getPlayer());
                event.setJoinMessage(jm);
            }
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event){
        if(SimpleTabList.getPlugin().config.getBoolean("Event.Use")){
            if(SimpleTabList.getPlugin().config.getBoolean("Plugin.ActionbarMessage")){
                MessageHandler.Send(event.getPlayer(), SimpleTabList.getPlugin().config.getString("Event.QuitMessage"));
                event.setQuitMessage("");
            }
            else{
                String qm = StringFormater.Get(SimpleTabList.getPlugin().config.getString("Event.QuitMessage"), event.getPlayer());
                event.setQuitMessage(qm);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(SimpleTabList.getPlugin().config.getBoolean("Chat.Use")){
            String message = event.getMessage();
            String nametag = event.getPlayer().getDisplayName();

            CustomConfig.setup(event.getPlayer());
            FileConfiguration con = CustomConfig.get();

            if(PermissionsHandler.hasPermission(event.getPlayer(), "stl.chat.staff")){
                if(con.getBoolean("Chat.Staff")){
                    event.setCancelled(true);
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.sendMessage(SimpleTabList.getPlugin().config.getString("Chat.Prefix") + " | Staff | " + nametag + StringFormater.Get(SimpleTabList.getPlugin().config.getString("Chat.Separator"), event.getPlayer()) + StringFormater.Get(message, event.getPlayer()));
                    }
                }
                else{
                    if(con.getBoolean("Chat.Muted")){
                        event.setCancelled(true);
                        MessageHandler.Send(event.getPlayer(), ChatColor.YELLOW + "You are muted!");
                    }
                    else{
                        event.setCancelled(false);
                        event.setFormat(nametag + StringFormater.Get(SimpleTabList.getPlugin().config.getString("Chat.Separator"), event.getPlayer()) + StringFormater.Get(message, event.getPlayer()));
                    }
                }
            }
            else{
                if(con.getBoolean("Chat.Muted")){
                    event.setCancelled(true);
                    MessageHandler.Send(event.getPlayer(), ChatColor.YELLOW + "You are muted!");
                }
                else{
                    event.setCancelled(false);
                    event.setFormat(nametag + StringFormater.Get(SimpleTabList.getPlugin().config.getString("Chat.Separator"), event.getPlayer()) + StringFormater.Get(message, event.getPlayer()));
                }
            }
        }
    }
}
