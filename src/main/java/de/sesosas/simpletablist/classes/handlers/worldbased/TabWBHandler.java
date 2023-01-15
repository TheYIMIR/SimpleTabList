package de.sesosas.simpletablist.classes.handlers.worldbased;

import de.sesosas.simpletablist.classes.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class TabWBHandler {

    public static void GenerateWorldConfig(){
        for(World world: Bukkit.getWorlds()){
            java.lang.String[] headerString = new java.lang.String[]{"This is a Header!", "Welcome %player_name%!"};
            java.lang.String[] footerString = new java.lang.String[] {"This is a Footer!", "This is Footer line 2!"};

            CustomConfig cf = new CustomConfig().setup("worlds/"+world.getName());
            FileConfiguration con = cf.get();
            con.addDefault("Prefix", "["+world.getName()+"] ");
            con.addDefault("Suffix", "");
            con.addDefault("Header", headerString);
            con.addDefault("Footer", footerString);
            con.options().copyDefaults(true);
            cf.save();
        }
    }

    public static Object GetWorldConfig(World world, String Path){
        CustomConfig cf = new CustomConfig().setup("worlds/"+world.getName());
        FileConfiguration con = cf.get();
        if(con.get(Path) != null){
            return con.get(Path);
        }
        return null;
    }
}