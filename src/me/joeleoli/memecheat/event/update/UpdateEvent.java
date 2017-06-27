package me.joeleoli.memecheat.event.update;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private UpdateType updateType;

    public UpdateEvent(UpdateType updateType) {
        this.updateType = updateType;
    }

    public UpdateType getUpdateType() {
        return this.updateType;
    }

    public HandlerList getHandlers() {
        return UpdateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return UpdateEvent.handlers;
    }

}