package de.sesosas.simpletablist;

import de.sesosas.simpletablist.classes.TabHeadFoot;
import de.sesosas.simpletablist.classes.TabName;
import de.sesosas.simpletablist.classes.handlers.CommandHandler;
import de.sesosas.simpletablist.classes.handlers.IEventHandler;
import de.sesosas.simpletablist.classes.handlers.UpdateHandler;
import de.sesosas.simpletablist.classes.commands.ChatCommands;
import de.sesosas.simpletablist.classes.commands.HomeCommand;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.LuckPermsEvent;
import net.luckperms.api.event.log.LogPublishEvent;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.event.user.UserLoadEvent;
import net.luckperms.api.event.user.track.UserDemoteEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class SimpleTabList extends JavaPlugin implements Listener {

    public FileConfiguration config = getConfig();

    private static SimpleTabList plugin;

    public static SimpleTabList getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        java.lang.String[] headerString = new java.lang.String[]{"This is a Header!", "Welcome %player_name%!"};
        java.lang.String[] footerString = new java.lang.String[] {"This is a Footer!", "This is Footer line 2!"};
        config.addDefault("PlaceholderInfo", "Placeholder using PlaceholderAPI [Example: \"Welcome {player_name} on this Server!\"]");
        config.addDefault("Tab.Names.Use", true);
        config.addDefault("Tab.Header.Use", true);
        config.addDefault("Tab.Header.Content", headerString);
        config.addDefault("Tab.Footer.Use", true);
        config.addDefault("Tab.Footer.Content", footerString);
        config.addDefault("Event.Use", true);
        config.addDefault("Event.JoinMessage", "The Player {player_name} joined the Server!");
        config.addDefault("Event.QuitMessage", "The Player {player_name} left the Server!");
        config.addDefault("Chat.Use", true);
        config.addDefault("Chat.Prefix", "§f[§cSTL§f]");
        config.addDefault("Chat.Separator", " >> ");
        config.addDefault("Chat.Colors", true);
        config.addDefault("Homes.Use", true);
        config.addDefault("Homes.Amount", 5);
        config.addDefault("Plugin.ActionbarMessage", false);
        config.addDefault("Plugin.NoticeMe", "You need LuckPerms to get this Plugin to work!");
        config.addDefault("bstats.Use", true);
        config.options().copyDefaults(true);
        saveConfig();


        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms luckPerms = provider.getProvider();
            EventBus eventBus = luckPerms.getEventBus();

            eventBus.subscribe(this.plugin, NodeAddEvent.class, this::onNodeAddEvent);
        }

        if(config.getBoolean("bstats.Use")){
            int id = 15221;
            Metrics metrics = new Metrics(this, id);
            metrics.addCustomChart(new SingleLineChart("banned", () -> Bukkit.getBannedPlayers().size()));
        }

        new UpdateHandler(this, 101989).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is no new update available.");
            } else {
                getLogger().info("There is a new update available.");
            }
        });

        new BukkitRunnable() {

            @Override
            public void run() {
                TabHeadFoot.Update();
            }
        }.runTaskTimer(this, 0, 20L);

        getServer().getPluginManager().registerEvents(new IEventHandler(), this);
        getCommand("stl").setExecutor(new CommandHandler());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("chat").setExecutor(new ChatCommands());
        System.out.println("Simple TabList has started!");
    }

    private <T extends LuckPermsEvent> void onNodeAddEvent(T t) {
        TabName.Update();
    }
}