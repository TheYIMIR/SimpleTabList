package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.SimpleTabList;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CurrentConfig {
    public static String getString(String name){
        return SimpleTabList.getPlugin().config.getString(name);
    }

    public static Boolean getBoolean(String name){
        return SimpleTabList.getPlugin().config.getBoolean(name);
    }

    public static List<?> getList(String name){
        return SimpleTabList.getPlugin().config.getList(name);
    }

    public static Integer getInt(String name){
        return SimpleTabList.getPlugin().config.getInt(name);
    }

    public static List<Float> getFloat(String name){
        return SimpleTabList.getPlugin().config.getFloatList(name);
    }

    public static Object get(String name){
        return SimpleTabList.getPlugin().config.get(name);
    }
}
