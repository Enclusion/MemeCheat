package me.joeleoli.memecheat.protocol.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEntityActionEvent extends Event {

    public Player player;
    public int action;
    private static HandlerList handlers = new HandlerList();

    public PacketEntityActionEvent(Player player, int action) {
        super(true);
        this.player = player;
        this.action = action;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getAction() {
        return this.action;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}