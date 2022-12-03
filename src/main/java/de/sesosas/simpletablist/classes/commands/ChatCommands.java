package de.sesosas.simpletablist.classes.commands;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.CustomConfig;
import de.sesosas.simpletablist.classes.handlers.MessageHandler;
import de.sesosas.simpletablist.classes.handlers.PermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatCommands implements TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            if(sender instanceof Player){
                Player player = ((Player) sender).getPlayer();
                if(CurrentConfig.getBoolean("Chat.Use")){
                    if(args.length >= 1){
                        if(args[0].equalsIgnoreCase("clear")){
                            ClearChat(player, args);
                        }
                        else if(args[0].equalsIgnoreCase("staff")){
                            StaffChat(player, args);
                        }
                        else if(args[0].equalsIgnoreCase("mute")){
                            MutePlayer(player, args);
                        }
                        else if(args[0].equalsIgnoreCase("unmute")){
                            UnmutePlayer(player, args);
                        }
                        else{
                            MessageHandler.Send(player, ChatColor.DARK_RED + "This command doesn't exist!");
                        }
                    }
                    else{
                        MessageHandler.Send(player, ChatColor.DARK_RED + "You need to provide <clear/staff>!");
                    }
                }
                else{
                    MessageHandler.Send(player, ChatColor.YELLOW + "This command is currently disabled!");
                }
            }
            else{
                sender.sendMessage("You are not be able to use this command!");
            }
        }
        catch(Exception e){

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length == 1){
            List<String> arguments = new ArrayList<>();
            if(PermissionsHandler.hasPermission(player, "stl.chat.staff")){
                arguments.add("staff");
                arguments.add("mute");
                arguments.add("unmute");
            }
            if(PermissionsHandler.hasPermission(player, "stl.chat.clear")){
                arguments.add("clear");
            }
            return arguments;
        }
        else if(args.length == 2){
            if(args[0].equalsIgnoreCase("staff")){
                List<String> arguments = new ArrayList<>();
                if(PermissionsHandler.hasPermission(player, "stl.chat.staff")){
                    arguments.add("enable");
                    arguments.add("disable");
                    return arguments;
                }
            }
            else{
                List<String> arguments = new ArrayList<>();
                for(Player pl : Bukkit.getOnlinePlayers()){
                    arguments.add(pl.getName());
                }
                return arguments;
            }
        }
        else{
            return null;
        }
        return null;
    }

    private static void MutePlayer(Player player, String[] args){
        if(args.length == 2){
            if(PermissionsHandler.hasPermission(player, "stl.chat.staff")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    CustomConfig.setup(target);
                    FileConfiguration con = CustomConfig.get();
                    if(con.getBoolean("Chat.Muted")){
                        MessageHandler.Send(player, ChatColor.DARK_RED + "The Player " +target.getDisplayName()+ ChatColor.DARK_RED +" is already muted!");
                    }
                    else{
                        con.set("Chat.Muted", true);
                        CustomConfig.save();
                        CustomConfig.reload();
                        MessageHandler.Send(target, ChatColor.YELLOW + "You got muted!");
                        MessageHandler.Send(player, target.getDisplayName() + ChatColor.YELLOW + " got muted now!");
                    }
                }
                else{
                    MessageHandler.Send(player, ChatColor.DARK_RED + "The Player needs to be online!");
                }
            }
        }
    }

    private static void UnmutePlayer(Player player, String[] args){
        if(args.length == 2){
            if(PermissionsHandler.hasPermission(player, "stl.chat.staff")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    CustomConfig.setup(target);
                    FileConfiguration con = CustomConfig.get();
                    if(con.getBoolean("Chat.Muted")){
                        con.set("Chat.Muted", false);
                        CustomConfig.save();
                        CustomConfig.reload();
                        MessageHandler.Send(target, ChatColor.YELLOW + "You got unmuted!");
                        MessageHandler.Send(player, target.getDisplayName() + ChatColor.YELLOW + " got unmuted now!");
                    }
                    else{
                        MessageHandler.Send(player, ChatColor.DARK_RED + "The Player " +target.getDisplayName()+ ChatColor.DARK_RED +" is not muted!");
                    }
                }
            }
        }
    }

    private static void ClearChat(Player player, String[] args) {
        if(args.length == 2){
            if(PermissionsHandler.hasPermission(player, "stl.chat.clear.other")){
                try{
                    Player target = Bukkit.getPlayer(args[1]);
                    for(int i=0; i<200; i++)
                    {
                        target.sendMessage("");
                    }
                    MessageHandler.Send(player, ChatColor.AQUA + "Cleared the chat of "+ target.getDisplayName());
                }
                catch (Exception e){
                    MessageHandler.Send(player, ChatColor.DARK_RED + "Couldn't clear the chat of "+args[1]);
                }
            }
            else{
                MessageHandler.Send(player, ChatColor.DARK_RED + "You are not allowed to use this Command!");
            }
        }
        else{
            if(PermissionsHandler.hasPermission(player, "stl.chat.clear")){
                for(int i=0; i<200; i++)
                {
                    Bukkit.broadcastMessage("");
                }
            }
            else{
                MessageHandler.Send(player, ChatColor.DARK_RED + "You are not allowed to use this Command!");
            }
        }
    }


    private static void StaffChat(Player player, String[] args) {
        if(args.length == 2){
            if(PermissionsHandler.hasPermission(player, "stl.chat.staff")){
                CustomConfig.setup(player);
                FileConfiguration con = CustomConfig.get();
                if(args[1].equalsIgnoreCase("enable")){
                    if(con.getBoolean("Chat.Staff")){
                        MessageHandler.Send(player, ChatColor.DARK_RED + "Staff chat already enabled!");
                    }
                    else{
                        con.set("Chat.Staff", true);
                        CustomConfig.save();
                        CustomConfig.reload();
                        MessageHandler.Send(player, ChatColor.YELLOW + "Staff chat is now enabled!");
                    }
                }
                else if(args[1].equalsIgnoreCase("disable")){
                    if(con.getBoolean("Chat.Staff")){
                        con.set("Chat.Staff", false);
                        CustomConfig.save();
                        CustomConfig.reload();
                        MessageHandler.Send(player, ChatColor.YELLOW + "Staff chat is now disabled!");
                    }
                    else{
                        MessageHandler.Send(player, ChatColor.DARK_RED + "Staff chat already disabled!");
                    }
                }
                else{
                    MessageHandler.Send(player, ChatColor.DARK_RED + "This command doesn't exist!");
                }
            }
            else{
                MessageHandler.Send(player, ChatColor.DARK_RED + "You are not allowed to use this Command!");
            }
        }
        else{
            MessageHandler.Send(player, ChatColor.DARK_RED + "You need to provide <enable/disable>!");
        }
    }
}


