package me.joeleoli.memecheat.check.type.movement;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Analyze;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class Glide extends Check {

    public Glide(Plugin plugin) {
        super(plugin, "Glide", "Glide");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (Performance.getTps() < this.getTpsThreshold()) {
            return;
        }

        if (event.getEntity() != null && event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();

            if (!player.isOnline()) {
                return;
            }

            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

                if (profile == null) {
                    return;
                }

                profile.setValue(this, "lastHit", System.currentTimeMillis());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (Performance.getTps() < this.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getAllowFlight()) {
            return;
        }

        if (Analyze.isInWeb(player)) {
            return;
        }
        if (player.getVehicle() != null) {
            return;
        }

        if (Performance.getPing(player) >= this.getPingThreshold()) {
            return;
        }

        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile == null) {
            return;
        }

        long now = System.currentTimeMillis();
        long lastHit = profile.getValueOrDefault(this, "lastHit", 0L);

        if (now - lastHit < 2000L) {
            return;
        }

        if (Analyze.blocksNear(player)) {
            profile.removeValue(this, "lastFly");
            return;
        }

        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) {
            return;
        }

        double OffsetY = event.getFrom().getY() - event.getTo().getY();

        if (OffsetY <= 0.0 || OffsetY > 0.16) {
            profile.removeValue(this, "lastFly");
            return;
        }

        long time = profile.getValueOrDefault(this, "lastFly", now);
        long diff = System.currentTimeMillis() - time;

        if (diff > 1000L) {
            profile.removeValue(this, "lastFly");
            Managers.getCheckManager().logCheat(this, player, "Fall Speed");
        }
        else {
            profile.setValue(this, "lastFly", time);
        }
    }

}