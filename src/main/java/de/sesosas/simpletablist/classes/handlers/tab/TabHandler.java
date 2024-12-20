package de.sesosas.simpletablist.classes.handlers.tab;

import de.sesosas.simpletablist.classes.CurrentConfig;
import de.sesosas.simpletablist.classes.StringFormater;
import de.sesosas.simpletablist.classes.handlers.internal.MessageHandler;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import org.bukkit.entity.Player;

import java.util.List;

import static de.sesosas.simpletablist.classes.handlers.tab.AnimationHandler.convertAnimatedText;

public class TabHandler {

    public static void UpdateTab(Player player) {
        try {
            if (!CurrentConfig.getBoolean("Worlds.Enable")) {
                updateTabForWorld(player, (List<String>) CurrentConfig.getList("Header.Content"), (List<String>) CurrentConfig.getList("Footer.Content"));
            } else {
                List<String> headerContent = (List<String>) TabWBHandler.GetWorldConfig(player.getWorld(), "Header.Content");
                List<String> footerContent = (List<String>) TabWBHandler.GetWorldConfig(player.getWorld(), "Footer.Content");

                updateTabForWorld(player, headerContent, footerContent);
            }
        } catch (Exception e) {
            MessageHandler.log("Found an error at Header or Footer config section! Please make sure there are lists with content!");
            MessageHandler.log(e);
        }
    }

    private static void updateTabForWorld(Player player, List<String> headerContent, List<String> footerContent) {
        updateTabContent(player, "Header", headerContent, true);
        updateTabContent(player, "Footer", footerContent, false);
    }

    private static void updateTabContent(Player player, String section, List<String> contentList, boolean isHeader) {
        if (CurrentConfig.getBoolean(section + ".Enable") && contentList != null) {
            String contentString = buildContentString(contentList, isHeader);
            if(isHeader){
                player.setPlayerListHeader(StringFormater.Get(convertAnimatedText(contentString), player));
            }
            else{
                player.setPlayerListFooter(StringFormater.Get(convertAnimatedText(contentString), player));
            }
        }
        else{
            if(isHeader){
                player.setPlayerListHeader("");
            }
            else{
                player.setPlayerListFooter("");
            }
        }
    }

    private static String buildContentString(List<String> contentList, boolean isHeader) {
        StringBuilder contentString = new StringBuilder();
        for (Object str : contentList) {
            if(isHeader){
                contentString.append(str).append("\n");
            }
            else{
                contentString.append("\n").append(str);
            }
        }
        return contentString.toString();
    }
}
