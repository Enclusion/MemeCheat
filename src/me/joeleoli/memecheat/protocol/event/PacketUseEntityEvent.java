package me.joeleoli.memecheat.protocol.event;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketUseEntityEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    public EnumWrappers.EntityUseAction action;
    public Player attacker;
    public Entity attacked;
    private PacketEvent packetEvent;

    public PacketUseEntityEvent(PacketEvent packetEvent, EnumWrappers.EntityUseAction Action, Player Attacker, Entity Attacked) {
        super(true);
        this.packetEvent = packetEvent;
        this.action = Action;
        this.attacker = Attacker;
        this.attacked = Attacked;
    }

    public PacketEvent getPacketEvent() {
        return this.packetEvent;
    }

    public EnumWrappers.EntityUseAction getAction() {
        return this.action;
    }

    public Player getAttacker() {
        return this.attacker;
    }

    public Entity getAttacked() {
        return this.attacked;
    }

    public HandlerList getHandlers() {
        return PacketUseEntityEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PacketUseEntityEvent.handlers;
    }

}