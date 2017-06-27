package me.joeleoli.memecheat.task;

import me.joeleoli.memecheat.CheatConfiguration;
import me.joeleoli.memecheat.event.update.UpdateEvent;
import me.joeleoli.memecheat.event.update.UpdateType;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class UpdateChecks implements Runnable {

    public UpdateChecks(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        if (!CheatConfiguration.ENABLED) {
            return;
        }

        long now = System.currentTimeMillis();

        for (UpdateType updateType : UpdateType.values()) {
            if (updateType.elapsed(now)) {
                try {
                    UpdateEvent event = new UpdateEvent(updateType);
                    Bukkit.getPluginManager().callEvent(event);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}