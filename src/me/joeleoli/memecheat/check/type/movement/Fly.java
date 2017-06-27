package me.joeleoli.memecheat.check.type.movement;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.event.player.PacketedMovementEvent;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Analyze;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Fly extends Check {

    public Fly(Plugin plugin) {
        super(plugin, "Fly", "Fly");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (Performance.getTps() < this.getTpsThreshold()) {
            return;
        }

        if (event.getEntity() == null) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (!player.isOnline()) {
            return;
        }

        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile == null) {
            return;
        }

        profile.setValue(this, "lastHit", System.currentTimeMillis());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PacketedMovementEvent event) {
        Player player = event.getPlayer();

        if (player.getAllowFlight()) {
            return;
        }

        if (player.getVehicle() != null) {
            return;
        }

        Vector location = player.getLocation().toVector();
        Material CURRENT_TYPE = event.getBlockAt(location).getItemType();
        Material ABOVE_TYPE = event.getBlockAt(location.clone().add(new Vector(0, 1, 0))).getItemType();
        Material UNDER_TYPE = event.getBlockAt(location.clone().subtract(new Vector(0, 1, 0))).getItemType();

        if (CURRENT_TYPE == Material.WATER || CURRENT_TYPE == Material.WEB || ABOVE_TYPE == Material.WEB) {
            return;
        }

        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile == null) {
            return;
        }

        long now = event.getNow();
        long lastHit = profile.getValueOrDefault(this, "lastHit", 0L);

        if (now - lastHit < 500L) {
            return;
        }

        if (CURRENT_TYPE != Material.AIR || UNDER_TYPE != Material.AIR || !Analyze.isJustAir(Analyze.getSurrounding(event, location.clone())) || !Analyze.isJustAir(Analyze.getSurrounding(event, location.clone().subtract(new Vector(0, 1, 0))))) {
            profile.removeValue(this, "lastFly");
        }
        else if (event.getTo().getY() != event.getFrom().getY()) {
            profile.removeValue(this, "lastFly");
        }
        else if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ()) {
            long time = profile.getValueOrDefault(this, "lastFly", now);
            long diff = System.currentTimeMillis() - time;

            if (diff > 500L) {
                profile.removeValue(this, "lastFly");

                Managers.getCheckManager().logCheat(this, player);
            }
            else {
                profile.setValue(this, "lastFly", time);
            }
        }
    }

}