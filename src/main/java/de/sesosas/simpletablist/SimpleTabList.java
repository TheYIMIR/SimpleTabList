package de.sesosas.simpletablist;

import de.sesosas.simpletablist.classes.commands.ReloadCommand;
import de.sesosas.simpletablist.classes.handlers.events.IEventHandler;
import de.sesosas.simpletablist.classes.handlers.internal.IntervalHandler;
import de.sesosas.simpletablist.classes.handlers.spigot.UpdateHandler;
import de.sesosas.simpletablist.classes.handlers.tab.NameHandler;
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

import java.util.ArrayList;
import java.util.List;

import de.sesosas.simpletablist.classes.handlers.tab.AnimationHandler;

public final class SimpleTabList extends JavaPlugin implements Listener {

    public FileConfiguration config = getConfig();

    private static SimpleTabList plugin;

    public static IntervalHandler interval;

    public static SimpleTabList getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        NameHandler.initScoreboard();

        java.lang.String[] headerString = new java.lang.String[]{"This is a header and animation {animation:0}!", "You: %player_name%!"};
        java.lang.String[] footerString = new java.lang.String[] {"This is a footer!", "This is footer line 2!"};

        config.addDefault("Names.Enable", true);
        config.addDefault("Names.LuckPerms.Prefix.Enable", true);
        config.addDefault("Names.LuckPerms.Suffix.Enable", true);
        config.addDefault("Names.Global.Enable", false);
        config.addDefault("Names.Global.Prefix", "");
        config.addDefault("Names.Global.Suffix", "");
        config.addDefault("Names.Sorting.Enable", true);
        config.addDefault("Names.Sorting.Type", "weight");
        config.addDefault("Names.Sorting.Ascending", true);
        config.addDefault("Names.Space.Enable", false);
        config.addDefault("Worlds.Enable", false);
        config.addDefault("Header.Enable", true);
        config.addDefault("Header.Content", headerString);
        config.addDefault("Footer.Enable", true);
        config.addDefault("Footer.Content", footerString);
        config.addDefault("Chat.Prefix", "§f[§cSTL§f]");
        config.addDefault("Chat.ActionbarMessage", false);
        config.addDefault("Tab.Refresh.Interval.Enable", false);
        config.addDefault("Tab.Refresh.Interval.Time", 1L);
        config.addDefault("bstats.Enable", true);
        config.options().copyDefaults(true);
        List<String> headerComment = new ArrayList<>();
        headerComment.add("Worlds\n");
        headerComment.add("    Enable\n");
        headerComment.add("Does enable/disable the worlds function which overrides the current Header and Footer content.\n");
        headerComment.add("You need LuckPerms and PlaceholderAPI to make this plugin work!\n");
        headerComment.add("Tab Refresh Interval Time is calculated in seconds.\n");
        config.options().header(headerComment.toString().replace("[", "").replace("]", "").replace(", ", ""));
        saveConfig();
        TabWBHandler.GenerateWorldConfig();
        AnimationHandler.GenerateAnimationExample();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms luckPerms = provider.getProvider();
            EventBus eventBus = luckPerms.getEventBus();

            eventBus.subscribe(this.plugin, NodeAddEvent.class, this::onNodeAddEvent);
        }

        if(config.getBoolean("bstats.Enable")){
            int id = 15221;
            Metrics metrics = new Metrics(this, id);
            metrics.addCustomChart(new SingleLineChart("banned", () -> Bukkit.getBannedPlayers().size()));
        }

        new UpdateHandler(this, 101989).getVersion(version -> {
            if (Float.parseFloat(this.getDescription().getVersion()) < Float.parseFloat(version)) {
                getLogger().info("There is a new update available.");
            } else {
                getLogger().info("There is no a new update available.");
            }
        });

        interval = new IntervalHandler(this, config.getLong("Tab.Refresh.Interval.Time") * 20L);
        interval.runTaskTimerAsynchronously(this, 0L, SimpleTabList.getPlugin().config.getLong("Tab.Refresh.Interval.Time") * 20L);
        interval.setEnabled(config.getBoolean("Tab.Refresh.Interval.Enable"));

        getServer().getPluginManager().registerEvents(new IEventHandler(), this);
        getCommand("stl-reload").setExecutor(new ReloadCommand());
        System.out.println("Simple TabList has started!");
    }

    private <T extends LuckPermsEvent> void onNodeAddEvent(T t) {
        NameHandler.Update();
    }
}