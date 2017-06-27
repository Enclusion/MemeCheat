package me.joeleoli.memecheat.protocol.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import me.joeleoli.memecheat.protocol.event.PacketEntityActionEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class InteractAdapter extends PacketAdapter {

    public InteractAdapter(Plugin plugin) {
        super(plugin, PacketType.Play.Client.ENTITY_ACTION);
    }

    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        Bukkit.getServer().getPluginManager().callEvent(new PacketEntityActionEvent(player, packet.getIntegers().read(1)));
    }

}
