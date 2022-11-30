package de.sesosas.simpletablist.classes.handlers;

import de.sesosas.simpletablist.classes.commands.ReloadCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            if(sender instanceof Player) {
                Player player = (Player) sender;
                List<String> commands = new ArrayList<>();
                commands.add("reload");

                if(args[0].equalsIgnoreCase(commands.get(0))) {
                    ReloadCommand.Do(player, args);
                } else {
                    MessageHandler.Send(player, ChatColor.DARK_RED + "This Command doesn't exists!");
                }
            }
            else {
                sender.sendMessage("You are not be able to use this command!");
            }
            return true;
        }
        catch (Exception e){
            if(sender instanceof Player) {
                Player player = (Player) sender;
                MessageHandler.Send(player, ChatColor.AQUA + "Commands:"
                        + "\n - /chat <args>"
                        + "\n - /home <args>"
                        + "\n - /stl reload");
            }
            else{
                sender.sendMessage("You are not be able to use this command!");
            }
            System.out.println(e);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            if (PermissionsHandler.hasPermission(player, "stl.reload")) {
                arguments.add("reload");
            }
            return arguments;
        }
        else{
            return null;
        }
    }
}
