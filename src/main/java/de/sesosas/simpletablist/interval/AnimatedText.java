package de.sesosas.simpletablist.interval;

import de.sesosas.simpletablist.classes.AnimationClass;
import de.sesosas.simpletablist.classes.ScoreboardClass;
import de.sesosas.simpletablist.api.classes.AInterval;
import de.sesosas.simpletablist.config.CurrentConfig;
import org.bukkit.Bukkit;

public class AnimatedText extends AInterval {

    public AnimatedText() {
        super("AnimatedText");
    }

    @Override
    public void Init() {
        setIntervalTime(CurrentConfig.getLong("Tab.Refresh.Interval.Time"));

        // Animations must run on the main thread for safety
        setUseMainThread(true);
    }

    @Override
    public void Run() {
        if (CurrentConfig.getBoolean("Tab.Refresh.Interval.Enable")) {
            // Update frame index first
            AnimationClass.frameIndex++;

            // Update scoreboard - this must be on the main thread
            // because it modifies Bukkit entities
            ScoreboardClass.Update();
        }
    }

    @Override
    public void onStart() {
        Bukkit.getLogger().info("[SimpleTabList] Animation interval started with period: "
                + getIntervalTime() + " seconds");
    }

    @Override
    public void onStop() {
        Bukkit.getLogger().info("[SimpleTabList] Animation interval stopped");
    }
}