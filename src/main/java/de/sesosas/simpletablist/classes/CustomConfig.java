package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {
    private File file;
    private FileConfiguration customFile;

    public CustomConfig setup(String path) {
        this.file = new File(Bukkit.getServer().getPluginManager().getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), path + ".yml");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Error creating file: " + path + ".yml");
                e.printStackTrace();
            }
        }
        reload();
        return this;
    }

    public FileConfiguration get() {
        if (this.customFile == null && this.file != null) {
            this.customFile = YamlConfiguration.loadConfiguration(this.file);
        }
        return this.customFile;
    }

    public boolean exist(String path) {
        this.file = new File(Bukkit.getServer().getPluginManager().getPlugin(SimpleTabList.getPlugin().getName()).getDataFolder(), path + ".yml");
        return this.file.exists();
    }

    public void save() {
        if (this.customFile != null && this.file != null) {
            try {
                this.customFile.save(this.file);
            } catch (IOException e) {
                Bukkit.getLogger().severe("Error saving file: " + this.file.getName());
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        if (this.file != null) {
            this.customFile = YamlConfiguration.loadConfiguration(this.file);
        }
    }
}
