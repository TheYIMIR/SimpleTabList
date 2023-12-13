package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.naming.spi.DirectoryManager;
import java.io.File;
import java.io.IOException;

public class CustomConfig {
    public File file;
    public FileConfiguration customFile;

    public CustomConfig setup(String path){
        this.file = new File(Bukkit.getServer().getPluginManager().getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), path + ".yml");
        if (!this.file.exists()){
            try{
                this.file.createNewFile();
            }catch (IOException e){
                //owww
            }
        }
        this.customFile = YamlConfiguration.loadConfiguration(this.file);
        return this;
    }

    public FileConfiguration get(String path){
        this.file = new File(Bukkit.getServer().getPluginManager().getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), path + ".yml");
        if (!this.file.exists()){
            try{
                this.file.createNewFile();
            }catch (IOException e){
                //owww
            }
        }
        this.customFile = YamlConfiguration.loadConfiguration(this.file);
        return this.customFile;
    }

    public boolean exist(String path){
        this.file = new File(Bukkit.getServer().getPluginManager().getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), path + ".yml");
        if (this.file.exists()){
            return true;
        }
        return false;
    }

    public FileConfiguration save(String path){
        this.file = new File(Bukkit.getServer().getPluginManager().getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), path + ".yml");
        if (!this.file.exists()){
            try{
                this.file.createNewFile();
            }catch (IOException e){
                //owww
            }
        }
        try{
            this.customFile.save(this.file);
        }catch (IOException e){
            System.out.println("Couldn't save file");
        }
        return this.customFile;
    }

    public FileConfiguration get(){
        return this.customFile;
    }

    public void save(){
        try{
            this.customFile.save(this.file);
        }catch (IOException e){
            System.out.println("Couldn't save file");
        }
    }

    public void reload(){
        this.customFile = YamlConfiguration.loadConfiguration(this.file);
    }
}
