package me.joeleoli.memecheat.protocol.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import me.joeleoli.memecheat.protocol.event.PacketHeldItemChangeEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HeldItemAdapter extends PacketAdapter {

    public HeldItemAdapter(Plugin plugin) {
        super(plugin, PacketType.Play.Client.HELD_ITEM_SLOT);
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        Bukkit.getServer().getPluginManager().callEvent(new PacketHeldItemChangeEvent(event, player));
    }

}