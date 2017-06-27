package me.joeleoli.memecheat.protocol.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketKeepAliveEvent extends Event {
    
    private static HandlerList handlers = new HandlerList();

    public Player player;

    public PacketKeepAliveEvent(Player Player) {
        super(true);
        this.player = Player;
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