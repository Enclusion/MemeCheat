package me.joeleoli.memecheat.protocol.event;

import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketHeldItemChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public Player player;
    public PacketEvent event;

    public PacketHeldItemChangeEvent(final PacketEvent Event, final Player Player) {
        super(true);
        this.player = Player;
        this.event = Event;
    }

    public PacketEvent getPacketEvent() {
        return this.event;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return PacketHeldItemChangeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PacketHeldItemChangeEvent.handlers;
    }

}