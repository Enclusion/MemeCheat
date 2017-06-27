package me.joeleoli.memecheat.protocol.event;

import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PacketSwingArmEvent extends org.bukkit.event.Event {

    private static HandlerList handlers = new HandlerList();

    public Player player;
    public PacketEvent event;

    public PacketSwingArmEvent(PacketEvent event, Player player) {
        super(true);
        this.player = player;
        this.event = event;
    }

    public PacketEvent getPacketEvent() {
        return this.event;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}