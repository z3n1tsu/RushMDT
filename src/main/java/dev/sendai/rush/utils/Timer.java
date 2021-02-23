package dev.sendai.rush.utils;

import java.text.DecimalFormat;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import dev.sendai.rush.MDT;

public class Timer {

    private MDT plugin;
    private int time;
    private BukkitTask task;
    private boolean countdown;

    public Timer(MDT plugin, int time, boolean countdown) {
        this.plugin = plugin;
        this.time = time;
        this.countdown = countdown;
    }

    public void start() {
        task = new BukkitRunnable() {
            public void run() {
                if (countdown) {
                    time--;
                    if (time == 0) {
                        stop();
                    }
                } else {
                    time++;
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 0, 20);
    }

    public void stop() {
        task.cancel();
    }

    public String getTime() {
        int seconds = time % 60;
        int minutes = (time / 60) % 60;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String strMinutes = decimalFormat.format(minutes);
        String strSeconds = decimalFormat.format(seconds);
        return strMinutes + ":" + strSeconds;
    }

}
