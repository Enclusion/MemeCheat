package me.joeleoli.memecheat.event.player;

import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.shape.Vector3i;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PacketedMovementEvent extends Event implements Cancellable {

    private static HandlerList handlerList = new HandlerList();

    private boolean cancelled;
    private long now;
    private Player player;
    private PlayerProfile profile;
    private PlayerMoveEvent event;
    private Map<Vector3i, Optional<MaterialData>> relativeBlockMap;

    public PacketedMovementEvent(Player player, PlayerProfile profile, PlayerMoveEvent event, long now) {
        this.cancelled = false;
        this.relativeBlockMap = new HashMap<>();
        this.player = player;
        this.profile = profile;
        this.event = event;
        this.now = now;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return this.player;
    }

    public PlayerProfile getProfile() {
        return this.profile;
    }

    public PlayerMoveEvent getEvent() {
        return this.event;
    }

    public Location getTo() {
        return this.event.getTo();
    }

    public Location getFrom() {
        return this.event.getFrom();
    }

    public long getNow() {
        return this.now;
    }

    public MaterialData getBlockAt(Vector location) {
        Vector3i relative = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Optional<MaterialData> mapped = this.relativeBlockMap.get(relative);

        if (mapped == null) {
            MaterialData materialData = null;
            Block block = location.toLocation(this.player.getWorld()).getBlock();

            if (block != null) {
                BlockState blockState = block.getState();

                if (blockState != null) {
                    materialData = blockState.getData();
                }
            }

            mapped = Optional.ofNullable(materialData);

            this.relativeBlockMap.put(relative, mapped);
        }

        return mapped.orElse(null);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}