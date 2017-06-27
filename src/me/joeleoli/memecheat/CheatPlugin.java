package me.joeleoli.memecheat;

import me.joeleoli.memecheat.listener.Listeners;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.protocol.ProtocolLibHook;
import me.joeleoli.memecheat.task.UpdateChecks;
import me.joeleoli.memecheat.task.UpdatePerformance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CheatPlugin extends JavaPlugin {

    private static CheatPlugin instance;

    private CheatConfiguration configuration;
    private ProtocolLibHook protocolHook;

    public void onEnable() {
        instance = this;

        this.configuration = new CheatConfiguration(this);

        new Managers(this);
        new Listeners(this);

        try {
            this.protocolHook = new ProtocolLibHook(this);
        }
        catch (Exception e) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.hasPermission("memecheat.staff") || p.hasPermission("memecheat.admin")) {
                    p.sendMessage(ChatColor.RED + "MemeCheat failed to load while hooking into ProtocolLib.");
                }
            }

            this.getServer().getPluginManager().disablePlugin(this);
        }

        new UpdateChecks(this);
        new UpdatePerformance(this);
    }

    public void onDisable() {
        if (this.protocolHook != null) {
            this.protocolHook.getProtocolManager().removePacketListeners(this);
        }

        Managers.getCheckManager().unregister();
    }

    public static CheatPlugin getInstance() {
        return instance;
    }

    public CheatConfiguration getConfiguration() {
        return this.configuration;
    }

}