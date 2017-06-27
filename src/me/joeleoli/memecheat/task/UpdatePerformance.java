package me.joeleoli.memecheat.task;

import me.joeleoli.memecheat.util.Performance;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class UpdatePerformance implements Runnable {

    private long currentSec;
    private int ticks;

    public UpdatePerformance(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        long sec = System.currentTimeMillis() / 1000L;

        if (this.currentSec == sec) {
            ++this.ticks;
        }
        else {
            this.currentSec = sec;

            double previous = Performance.getTps();

            Performance.setTps((previous == 0.0) ? this.ticks : ((previous + this.ticks) / 2.0));

            this.ticks = 0;
        }
    }

}