package de.sesosas.simpletablist;

import de.sesosas.simpletablist.classes.handlers.commands.CommandHandler;
import de.sesosas.simpletablist.classes.handlers.events.IEventHandler;
import de.sesosas.simpletablist.classes.handlers.internal.IntervalHandler;
import de.sesosas.simpletablist.classes.handlers.spigot.UpdateHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
import de.sesosas.simpletablist.classes.handlers.tab.TabHandler;
import de.sesosas.simpletablist.classes.handlers.worldbased.TabWBHandler;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.LuckPermsEvent;
import net.luckperms.api.event.node.NodeAddEvent;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public final class SimpleTabList extends JavaPlugin implements Listener {

    public FileConfiguration config = getConfig();

    public static BukkitTask interval;

    private static SimpleTabList plugin;

    public static SimpleTabList getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        java.lang.String[] headerString = new java.lang.String[]{"This is a Header!", "Welcome %player_name%!"};
        java.lang.String[] footerString = new java.lang.String[] {"This is a Footer!", "This is Footer line 2!"};

        config.addDefault("Names.Use", true);
        config.addDefault("Worlds.Names.Use", true);
        config.addDefault("Worlds.HeaderFooter.Use", true);
        config.addDefault("Header.Use", true);
        config.addDefault("Header.Content", headerString);
        config.addDefault("Footer.Use", true);
        config.addDefault("Footer.Content", footerString);
        config.addDefault("Chat.Prefix", "§f[§cSTL§f]");
        config.addDefault("Chat.ActionbarMessage", false);
        config.addDefault("Plugin.Update.Interval.Use", true);
        List<String> intervalTime = new ArrayList<>();
        intervalTime.add("The default time is 20L which is equal to 2 seconds.");
        config.setComments("Plugin.Update.Interval", intervalTime);
        config.addDefault("Plugin.Update.Interval.Time", 20L);
        config.addDefault("Plugin.NoticeMe", "You need LuckPerms to get this Plugin to work!");
        config.addDefault("bstats.Use", true);
        config.options().copyDefaults(true);
        saveConfig();
        TabWBHandler.GenerateWorldConfig();

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



        getServer().getPluginManager().registerEvents(new IEventHandler(), this);
        getCommand("stl").setExecutor(new CommandHandler());
        System.out.println("Simple TabList has started!");
    }

    private <T extends LuckPermsEvent> void onNodeAddEvent(T t) {
        NameHandler.Update();
    }
}