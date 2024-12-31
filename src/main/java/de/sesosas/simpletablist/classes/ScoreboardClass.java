package de.sesosas.simpletablist.classes;

import de.sesosas.simpletablist.api.utils.StringUtil;
import de.sesosas.simpletablist.api.utils.WorldUtil;
import de.sesosas.simpletablist.config.CurrentConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.sesosas.simpletablist.classes.scoreboard.DetailsClass;
import de.sesosas.simpletablist.classes.scoreboard.NamesClass;

public class ScoreboardClass {

    public static synchronized void Update() {
        try {
            List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

            for (Player player : playerList) {
                if (CurrentConfig.getBoolean("Names.Enable")) {
                    NamesClass.updatePlayerName(player);
                }
                else{
                    NamesClass.resetPlayerNames();
                }
                DetailsClass.updateTab(player);
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning((Supplier<String>) e);
        }
    }
}
