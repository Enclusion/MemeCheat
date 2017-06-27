package me.joeleoli.memecheat.listener;

import me.joeleoli.memecheat.listener.type.CombatListener;
import me.joeleoli.memecheat.listener.type.MovementListener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class Listeners {

    public Listeners(Plugin plugin) {
        this.registerListeners(plugin, Arrays.asList(new CombatListener(), new MovementListener()));
    }

    private void registerListeners(Plugin plugin, List<Listener> listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

}