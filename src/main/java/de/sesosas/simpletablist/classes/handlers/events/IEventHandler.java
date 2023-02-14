package de.sesosas.simpletablist.classes.handlers.events;

import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class IEventHandler implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event){
        NameHandler.Update();
        TabHandler.UpdateTab();
        /*
        if(CurrentConfig.getBoolean("Event.Use")){
            new UpdateHandler(SimpleTabList.getPlugin(), 101989).getVersion(version -> {
                if (Float.parseFloat(SimpleTabList.getPlugin().getDescription().getVersion()) < Float.parseFloat(version)) {
                    if(event.getPlayer().isOp()){
                        MessageHandler.Send(event.getPlayer(), ChatColor.AQUA + "There is a new update available.");
                    }
                }
            });

            if(CurrentConfig.getBoolean("Plugin.ActionbarMessage")){
                MessageHandler.Send(event.getPlayer(), CurrentConfig.getString("Event.JoinMessage"));
                event.setJoinMessage("");
            }
            else{
                String jm = StringFormater.Get(CurrentConfig.getString("Event.JoinMessage"), event.getPlayer());
                event.setJoinMessage(jm);
            }
        }
        */
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event){
        NameHandler.Update();
        /*
        if(CurrentConfig.getBoolean("Event.Use")){
            if(CurrentConfig.getBoolean("Plugin.ActionbarMessage")){
                MessageHandler.Send(event.getPlayer(), CurrentConfig.getString("Event.QuitMessage"));
                event.setQuitMessage("");
            }
            else{
                String qm = StringFormater.Get(CurrentConfig.getString("Event.QuitMessage"), event.getPlayer());
                event.setQuitMessage(qm);
            }
        }
         */
    }

    /*
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(CurrentConfig.getBoolean("Chat.Use")){
            String message = event.getMessage();
            String nametag = event.getPlayer().getDisplayName();

            CustomConfig cf = new CustomConfig().setup(event.getPlayer());
            FileConfiguration con = cf.get();

            if(PermissionsHandler.hasPermission(event.getPlayer(), "stl.chat.staff")){
                if(con.getBoolean("Chat.Staff")){
                    event.setCancelled(true);
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.sendMessage(CurrentConfig.getString("Chat.Prefix") + " | Staff | " + nametag + StringFormater.Get(CurrentConfig.getString("Chat.Separator"), event.getPlayer()) + StringFormater.Get(message, event.getPlayer()));
                    }
                }
                else{
                    if(con.getBoolean("Chat.Muted")){
                        event.setCancelled(true);
                        MessageHandler.Send(event.getPlayer(), ChatColor.YELLOW + "You are muted!");
                    }
                    else{
                        event.setCancelled(false);
                        event.setFormat(nametag + StringFormater.Get(CurrentConfig.getString("Chat.Separator"), event.getPlayer()) + StringFormater.Get(message, event.getPlayer()));
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
                    event.setFormat(nametag + StringFormater.Get(CurrentConfig.getString("Chat.Separator"), event.getPlayer()) + StringFormater.Get(MessageHandler.CheckBannedWords(MessageHandler.CheckLinks(message)), event.getPlayer()));
                }
            }
        }
    }
    */

    @EventHandler
    public void OnExitPortal(EntityPortalExitEvent event) {
        if(event.getEntity() instanceof Player){
            NameHandler.Update();
            TabHandler.UpdateTab();
        }
    }
}
