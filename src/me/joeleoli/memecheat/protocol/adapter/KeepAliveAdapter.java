package me.joeleoli.memecheat.protocol.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import me.joeleoli.memecheat.protocol.event.PacketKeepAliveEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class KeepAliveAdapter extends PacketAdapter {
    
    public KeepAliveAdapter(Plugin plugin) {
        super(plugin, PacketType.Play.Client.KEEP_ALIVE);
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        Bukkit.getServer().getPluginManager().callEvent(new PacketKeepAliveEvent(player));
    }

}