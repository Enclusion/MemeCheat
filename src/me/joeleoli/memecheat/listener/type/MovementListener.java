package me.joeleoli.memecheat.listener.type;

import me.joeleoli.memecheat.event.player.PacketedMovementEvent;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Performance.getTps() <= 18.0) {
            return;
        }

        Player player = event.getPlayer();
        PlayerProfile playerData = Managers.getProfileManager().getProfile(player.getUniqueId());

        Location to = event.getTo();
        Location from = event.getFrom();

        if (player.hasPermission("memecheat.movement.bypass")) {
            return;
        }

        if (from.getWorld() == to.getWorld() && (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())) {
            PacketedMovementEvent movementEvent = new PacketedMovementEvent(player, playerData, event, System.currentTimeMillis());
            Bukkit.getServer().getPluginManager().callEvent(movementEvent);

            if (movementEvent.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }

}