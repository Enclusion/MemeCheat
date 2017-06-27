package me.joeleoli.memecheat.event.player;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.profile.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.*;

public class PlayerViolationEvent extends Event implements Cancellable {

    private static HandlerList handlerList = new HandlerList();
    private Player player;
    private PlayerProfile profile;
    private Check check;
    private boolean cancelled;

    public PlayerViolationEvent(Player player, Check check, PlayerProfile profile) {
        this.cancelled = false;
        this.player = player;
        this.check = check;
        this.profile = profile;
    }

    public Player getPlayer() {
        return this.player;
    }

    public PlayerProfile getProfile() {
        return this.profile;
    }

    public Check getCheck() {
        return this.check;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return PlayerViolationEvent.handlerList;
    }

    public static HandlerList getHandlerList() {
        return PlayerViolationEvent.handlerList;
    }

}