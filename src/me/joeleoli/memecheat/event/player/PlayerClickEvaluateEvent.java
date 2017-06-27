package me.joeleoli.memecheat.event.player;

import me.joeleoli.memecheat.profile.PlayerProfile;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerClickEvaluateEvent extends Event {

    private static HandlerList handlerList = new HandlerList();
    
    private Player player;
    private PlayerProfile profile;
    private int clicks;
    private int hits;

    public PlayerClickEvaluateEvent(Player player, PlayerProfile profile, int clicks, int hits) {
        this.player = player;
        this.profile = profile;
        this.clicks = clicks;
        this.hits = hits;
    }

    public PlayerProfile getProfile() {
        return this.profile;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getClicks() {
        return this.clicks;
    }

    public int getHits() {
        return this.hits;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}