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

public class Speed extends Check {

    public Speed(Plugin plugin) {
        super(plugin, "Speed", "Speed");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent event) {
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

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            profile.setValue(this, "lastHit", System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPlayerMove(PacketedMovementEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getAllowFlight()) {
            return;
        }

        if (player.getVehicle() != null) {
            return;
        }

        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile == null) {
            return;
        }

        if (profile.getLastVelocityVector() != null) {
            return;
        }

        long now = event.getNow();
        int count = profile.getValueOrDefault(this, "speedCount", 0);
        long time = profile.getValueOrDefault(this, "speedTime", now);
        int fastCount = profile.getValueOrDefault(this, "fastCount", 0);
        long lastIce = profile.getValueOrDefault(this, "lastIce", 0L);

        Long fastTime = profile.getValue(this, "fastTime");

        if (fastTime != null) {
            long lastHitFly = profile.getValueOrDefault(this, "lastHit", 0L);
            long lastHitDiff = now - lastHitFly;
            double offset = Mathematics.offset(Mathematics.getHorizontalVector(event.getFrom().toVector()), Mathematics.getHorizontalVector(event.getTo().toVector()));

            Vector location = player.getLocation().toVector();

            Material BELOW = event.getBlockAt(location.clone().subtract(new Vector(0, 1, 0))).getItemType();
            Material DOUBLE_BELOW = event.getBlockAt(location.clone().subtract(new Vector(0, 2, 0))).getItemType();
            Material UPPER_EYES = event.getBlockAt(location.clone().add(new Vector(0.0, player.getEyeHeight() + 1.0, 0.0))).getItemType();
            Material LOWER_HALF = event.getBlockAt(location.clone().subtract(new Vector(0.0, 0.5, 0.0))).getItemType();

            boolean onGround = Analyze.onGround(BELOW, LOWER_HALF);
            boolean onIce = Analyze.onIce(BELOW, DOUBLE_BELOW);
            boolean onStairs = Analyze.onStairs(BELOW, DOUBLE_BELOW);
            boolean onSlabs = Analyze.onSlabs(BELOW, DOUBLE_BELOW);
            boolean clientOnGround = player.isOnGround();

            double limit = (onGround && player.getVehicle() == null) ? 0.33 : 0.4;

            if (UPPER_EYES != Material.AIR && !Analyze.canStandAt(UPPER_EYES)) {
                limit = 0.69;
            }

            float speed = player.getWalkSpeed();

            if (speed > 0.2) {
                limit += (speed - 0.2) * 3.3000000000000003;
            }

            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().getId() == PotionEffectType.SPEED.getId()) {
                    limit += (clientOnGround ? 0.06 : 0.02) * (effect.getAmplifier() + 1);
                    break;
                }
            }

            if (onIce) {
                lastIce = now;
            }

            if (lastHitDiff < 800L) {
                ++limit;
            }
            else if (lastHitDiff < 1600L) {
                limit += 0.4;
            }
            else if (lastHitDiff < 2000L) {
                limit += 0.1;
            }

            if (onStairs) {
                limit += 0.4;
            }
            else if (onIce) {
                limit += 0.4;
            }
            else if (onSlabs) {
                limit += 0.2;
            }

            if (!onIce && now - lastIce < 1500L) {
                limit += 0.2;
            }

            if (offset > limit && fastTime + 150L >= now) {
                ++fastCount;
            }
            else {
                fastCount = 0;
            }
        }

        if (fastCount >= 4) {
            fastCount = 0;
            ++count;
        }

        if (time + 5000L < now) {
            count = 0;
            time = now;
        }

        if (count >= 3) {
            count = 0;
            Managers.getCheckManager().logCheat(this, player);
        }

        profile.setValue(this, "speedCount", count);
        profile.setValue(this, "speedTime", time);
        profile.setValue(this, "fastCount", fastCount);
        profile.setValue(this, "fastTime", now);
        profile.setValue(this, "lastIce", lastIce);
    }

}