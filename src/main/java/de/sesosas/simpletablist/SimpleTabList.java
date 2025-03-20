package de.sesosas.simpletablist;

import de.sesosas.simpletablist.api.classes.AInterval;
import de.sesosas.simpletablist.api.utils.ThreadUtil;
import de.sesosas.simpletablist.api.utils.WorldUtil;
import de.sesosas.simpletablist.classes.UpdateClass;
import de.sesosas.simpletablist.classes.scoreboard.NamesClass;
import de.sesosas.simpletablist.classes.scoreboard.SidebarClass;
import de.sesosas.simpletablist.command.ReloadCommand;
import de.sesosas.simpletablist.command.SidebarCommand;
import de.sesosas.simpletablist.config.SidebarConfig;
import de.sesosas.simpletablist.event.IEventHandler;
import de.sesosas.simpletablist.classes.ScoreboardClass;
import de.sesosas.simpletablist.interval.AnimatedText;
import de.sesosas.simpletablist.interval.SidebarInterval;
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

import de.sesosas.simpletablist.classes.AnimationClass;

public final class SimpleTabList extends JavaPlugin implements Listener {

    public FileConfiguration config = getConfig();

    private static SimpleTabList plugin;

    public static SimpleTabList getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // Initialize thread utility
        ThreadUtil.initialize(this);

        // Initialize scoreboard
        NamesClass.initScoreboard();

        // Set up configuration defaults
        setupDefaultConfig();

        // Load sidebar configuration
        SidebarConfig.loadConfig();
        SidebarClass.initialize();

        // Generate world and animation configs
        WorldUtil.GenerateWorldConfig();
        AnimationClass.GenerateAnimationExample();

        // Set up LuckPerms integration
        setupLuckPerms();

        // Set up metrics if enabled
        if(config.getBoolean("bstats.Enable")){
            setupMetrics();
        }

        // Check for updates
        checkForUpdates();

        // Initialize animated text and sidebar intervals
        new AnimatedText();
        new SidebarInterval();

        // Start all intervals
        AInterval.startAllIntervals(this);

        // Register event handlers and commands
        getServer().getPluginManager().registerEvents(new IEventHandler(), this);
        getCommand("stl-reload").setExecutor(new ReloadCommand());
        getCommand("sidebar").setExecutor(new SidebarCommand());

        Bukkit.getLogger().info("Simple TabList has started!");
    }

    private void setupDefaultConfig() {
        java.lang.String[] headerString = new java.lang.String[]{"This is a header and animation {animation:0}!", "You: %player_name%!"};
        java.lang.String[] footerString = new java.lang.String[] {"This is a footer!", "This is footer line 2!"};

        config.addDefault("Names.Enable", true);
        config.addDefault("Names.Format.Default", "%luckperms_prefix% &f[player_name] %luckperms_suffix%");
        config.addDefault("Names.Global.Enable", false);
        config.addDefault("Names.Global.Prefix", "");
        config.addDefault("Names.Global.Suffix", "");
        config.addDefault("Names.Sorting.Enable", true);
        config.addDefault("Names.Sorting.Type", "weight");
        config.addDefault("Names.Sorting.Ascending", true);
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
        config.addDefault("Performance.AsyncThreads", true);
        config.options().copyDefaults(true);

        List<String> headerComment = new ArrayList<>();
        headerComment.add("Worlds\n");
        headerComment.add("    Enable\n");
        headerComment.add("Does enable/disable the worlds function which overrides the current Header and Footer content.\n");
        headerComment.add("You need LuckPerms and PlaceholderAPI to make this plugin work!\n");
        headerComment.add("Tab Refresh Interval Time is calculated in seconds.\n");
        headerComment.add("Performance.AsyncThreads: Set to true to run operations asynchronously for better performance.\n");
        config.options().header(headerComment.toString().replace("[", "").replace("]", "").replace(", ", ""));
        saveConfig();
    }

    private void setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms luckPerms = provider.getProvider();
            EventBus eventBus = luckPerms.getEventBus();

            eventBus.subscribe(plugin, NodeAddEvent.class, this::onNodeAddEvent);
        } else {
            Bukkit.getLogger().warning("LuckPerms not found! Some features will not work properly.");
        }
    }

    private void setupMetrics() {
        int id = 15221;
        Metrics metrics = new Metrics(this, id);
        metrics.addCustomChart(new SingleLineChart("banned", () -> Bukkit.getBannedPlayers().size()));
        Bukkit.getLogger().info("bStats metrics enabled");
    }

    private void checkForUpdates() {
        new UpdateClass(this, 101989).getVersion(version -> {
            if (Float.parseFloat(this.getDescription().getVersion()) < Float.parseFloat(version)) {
                getLogger().info("There is a new update available: v" + version);
            } else {
                getLogger().info("You are running the latest version");
            }
        });
    }

    @Override
    public void onDisable(){
        // Clean up player scoreboards
        NamesClass.resetPlayerNames();

        // Remove all sidebars
        SidebarClass.removeAllSidebars();

        // Stop all intervals
        AInterval.stopAllIntervals();

        // Graceful shutdown of thread pools
        ThreadUtil.shutdown();

        // Give threads a moment to terminate gracefully
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
            // Ignore interruption during shutdown
        }

        // Force shutdown any remaining threads
        ThreadUtil.forceShutdown();

        Bukkit.getLogger().info("Simple TabList has been disabled");
    }

    private <T extends LuckPermsEvent> void onNodeAddEvent(T t) {
        // Run scoreboard update asynchronously
        ThreadUtil.submitTask(ScoreboardClass::Update);
    }
}