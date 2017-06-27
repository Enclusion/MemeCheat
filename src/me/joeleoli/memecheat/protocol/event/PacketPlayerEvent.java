package me.joeleoli.memecheat.protocol.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketPlayerEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private Player player;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private PacketPlayerType type;

    public PacketPlayerEvent(Player player, double x, double y, double z, float yaw, float pitch, PacketPlayerType type) {
        super(true);
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.type = type;
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public PacketPlayerType getType() {
        return this.type;
    }

    public HandlerList getHandlers() {
        return PacketPlayerEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PacketPlayerEvent.handlers;
    }

}