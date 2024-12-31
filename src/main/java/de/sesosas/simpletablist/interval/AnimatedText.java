package de.sesosas.simpletablist.interval;

import de.sesosas.simpletablist.SimpleTabList;
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
    public void Init(){
        setIntervalTime(CurrentConfig.getLong("Tab.Refresh.Interval.Time"));
    }

    @Override
    public void Run() {
        if(CurrentConfig.getBoolean("Tab.Refresh.Interval.Enable")){
            Bukkit.getScheduler().runTask(SimpleTabList.getPlugin(), () -> {
                ScoreboardClass.Update();
                AnimationClass.frameIndex++;
            });
        }
    }
}
