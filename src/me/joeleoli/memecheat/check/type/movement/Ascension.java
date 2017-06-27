package me.joeleoli.memecheat.check.type.movement;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.event.player.PacketedMovementEvent;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Analyze;
import me.joeleoli.memecheat.util.Mathematics;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class Ascension extends Check {

    public Ascension(Plugin plugin) {
        super(plugin, "Ascension", "Flight (Ascension)");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        if (event.getEntity() != null && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!player.isOnline()) {
                return;
            }

            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

                if (profile != null) {
                    profile.setValue(this, "lastHit", System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PacketedMovementEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();

        if (event.getFrom().getY() >= event.getTo().getY() || player.getAllowFlight() || player.getVehicle() != null) {
            return;
        }

        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile.getLastVelocityVector() != null) {
            return;
        }

        long now = System.currentTimeMillis();
        long lastHit = profile.getValueOrDefault(this, "lastHit", 0L);

        if (now - lastHit < 1000L) {
            return;
        }

        long time = profile.getValueOrDefault(this, "time", now);
        long diff = now - time;

        double totalBlocks = profile.getValueOrDefault(this, "totalBlocks", 0.0);
        double offsetY = Mathematics.offset(Mathematics.getVerticalVector(event.getFrom().toVector()), Mathematics.getVerticalVector(event.getTo().toVector()));

        if (offsetY > 0.0) {
            totalBlocks += offsetY;
        }

        Vector location = player.getLocation().toVector();

        Material CURRENT_TYPE = event.getBlockAt(location.clone()).getItemType();
        Material UNDER_TYPE = event.getBlockAt(location.clone().subtract(new Vector(0, 1, 0))).getItemType();
        Material HALF_UNDER_TYPE = event.getBlockAt(location.clone().subtract(new Vector(0.0, 0.5, 0.0))).getItemType();

        boolean near = false;

        if (Analyze.isFence(HALF_UNDER_TYPE)) {
            near = true;
        }
        else if (Analyze.isBlocked(CURRENT_TYPE) || Analyze.isBlocked(UNDER_TYPE)) {
            near = true;
        }
        else {
            Set<Material> blockSet = new HashSet<>();

            blockSet.addAll(Analyze.getSurrounding(event, location.clone()));
            blockSet.addAll(Analyze.getSurrounding(event, location.clone().subtract(new Vector(0, 1, 0))));

            for (Material material : blockSet) {
                if (material != Material.AIR) {
                    near = true;
                    break;
                }
            }
        }

        if (near) {
            totalBlocks = 0.0;
        }

        double limit = 1.5;

        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.JUMP)) {
                    int level = effect.getAmplifier() + 1;
                    limit += Math.pow(level + 4.2, 2.0) / 8.0;
                    break;
                }
            }
        }

        if (totalBlocks > limit) {
            if (diff > 500L) {
                Managers.getCheckManager().logCheat(this, player, new DecimalFormat("##.#").format(totalBlocks - limit) + " Blocks");
                time = now;
            }
        }
        else {
            time = now;
        }

        profile.setValue(this, "time", time);
        profile.setValue(this, "totalBlocks", totalBlocks);
    }

}