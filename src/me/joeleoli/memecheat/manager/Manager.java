package me.joeleoli.memecheat.manager;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Manager implements Listener {

    private Plugin plugin;

    public Manager(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public void register() {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

}