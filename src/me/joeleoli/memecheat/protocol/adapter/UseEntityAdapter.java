package me.joeleoli.memecheat.protocol.adapter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;

import me.joeleoli.memecheat.reflect.INMS;
import me.joeleoli.memecheat.protocol.event.PacketUseEntityEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class UseEntityAdapter extends PacketAdapter {

    private static boolean ENABLED = true;

    public UseEntityAdapter(Plugin plugin) {
        super(plugin, PacketType.Play.Client.USE_ENTITY);
    }

    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        EnumWrappers.EntityUseAction type;

        try {
            type = packet.getEntityUseActions().read(0);
        }
        catch (Exception ex) {
            ENABLED = false;

            return;
        }

        int entityId = packet.getIntegers().read(0);

        Entity entity = INMS.get().getEntity(player.getWorld(), entityId);

        PacketUseEntityEvent useEntityEvent = new PacketUseEntityEvent(event, type, player, entity);
        Bukkit.getServer().getPluginManager().callEvent(useEntityEvent);
    }

}