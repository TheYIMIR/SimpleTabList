package de.sesosas.simpletablist.classes.handlers.worldbased;

import de.sesosas.simpletablist.classes.CustomConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class TabWBHandler {

    public static void GenerateWorldConfig() {
        for (World world : Bukkit.getWorlds()) {
            String configPath = "worlds/" + world.getName();
            CustomConfig cf = new CustomConfig().setup(configPath);
            if (!cf.exist(configPath)) {
                FileConfiguration con = cf.get();
                con.addDefault("Names.Enable", true);
                con.addDefault("Header.Enable", true);
                con.addDefault("Footer.Enable", true);
                con.addDefault("Names.Prefix", "[" + world.getName() + "]");
                con.addDefault("Names.Suffix", "");
                con.addDefault("Header.Content", new String[]{"This is a Header!", "Welcome %player_name%!"});
                con.addDefault("Footer.Content", new String[]{"This is a Footer!", "This is Footer line 2!"});
                con.options().copyDefaults(true);
                cf.save();
            }
        }
    }

    public static Object GetWorldConfig(World world, String path) {
        String configPath = "worlds/" + world.getName();
        CustomConfig cf = new CustomConfig().setup(configPath);
        FileConfiguration con = cf.get();
        return con.get(path);
    }
}