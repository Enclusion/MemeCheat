package me.joeleoli.memecheat.check.type.combat;

import me.joeleoli.memecheat.CheatConfiguration;
import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.protocol.event.PacketUseEntityEvent;
import me.joeleoli.memecheat.util.Mathematics;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

public class Reach extends Check {

    public Reach(Plugin plugin) {
        super(plugin, "Reach", "Reach");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());
        
        if (profile != null) {
            profile.setValue(this, "teleported", System.currentTimeMillis());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            profile.setValue(this, "dead", System.currentTimeMillis());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            profile.setValue(this, "dead", System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();
        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getWorld() == to.getWorld()) {
            Vector diff = from.clone().subtract(to.clone()).toVector().divide(new Vector(1, 2, 1)).normalize();

            if (Double.isNaN(diff.getX()) || Double.isNaN(diff.getY()) || Double.isNaN(diff.getZ())) {
                return;
            }

            Vector currentDelta = profile.getValue(this, "currentDelta");

            if (currentDelta == null) {
                currentDelta = new Vector();
                profile.setValue(this, "currentDelta", currentDelta);
            }

            currentDelta.add(diff).multiply(0.5);
        }
    }

    @EventHandler
    public void onEntityUse(PacketUseEntityEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        if (event.getAttacked() != null && event.getAttacked() instanceof Player) {
            Player attacker = event.getAttacker();

            int attackerPing = Performance.getPing(attacker);

            if (attackerPing >= this.getPingThreshold()) {
                return;
            }

            if (!attacker.isDead() && attacker.isOnline() && attacker.getGameMode() != GameMode.CREATIVE) {
                Player attacked = (Player) event.getAttacked();

                if (attacked.isOnline() && !attacked.isDead()) {
                    PlayerProfile attackedProfile = Managers.getProfileManager().getProfile(attacked.getUniqueId());

                    long teleported = attackedProfile.getValueOrDefault(this, "teleported", 0L);
                    long dead = attackedProfile.getValueOrDefault(this, "dead", 0L);
                    long now = System.currentTimeMillis();
                    long teleportAttacked = attackedProfile.getValueOrDefault(this, "teleported", 0L);
                    long lastKockbackAttacked = attackedProfile.getValueOrDefault(this, "lastKockback", 0L);
                    long lastKockbackDiffAttacked = now - lastKockbackAttacked;

                    Vector lastKnockbackVelocityAttacked = attackedProfile.getValue(this, "lastKnockbackVelocity");

                    if (lastKnockbackVelocityAttacked == null) {
                        lastKnockbackVelocityAttacked = new Vector();
                    }

                    lastKnockbackVelocityAttacked.multiply(Math.min(0L, 1000L - lastKockbackDiffAttacked) / 1000.0);
                    lastKnockbackVelocityAttacked.multiply(0.25);

                    long lastKockbackAttacker = attackedProfile.getValueOrDefault(this, "lastKockback", 0L);
                    long lastKockbackDiffAttacker = now - lastKockbackAttacker;

                    Vector lastKnockbackVelocityAttacker = attackedProfile.getValue(this, "lastKnockbackVelocity");

                    if (lastKnockbackVelocityAttacker == null) {
                        lastKnockbackVelocityAttacker = new Vector();
                    }

                    lastKnockbackVelocityAttacker.multiply(Math.min(0L, 1000L - lastKockbackDiffAttacker) / 1000.0);
                    lastKnockbackVelocityAttacker.multiply(0.125);

                    Vector currentDeltaAttacker = attackedProfile.getValue(this, "currentDelta");

                    if (currentDeltaAttacker == null) {
                        currentDeltaAttacker = new Vector();
                    }

                    currentDeltaAttacker.multiply(0.75);

                    Vector currentDeltaAttacked = attackedProfile.getValue(this, "currentDelta");

                    if (currentDeltaAttacked == null) {
                        currentDeltaAttacked = new Vector();
                    }

                    currentDeltaAttacked.multiply(0.75);

                    if (now - teleported > 750L && now - teleportAttacked > 750L && now - dead > 2000L && lastKockbackDiffAttacked > 150L && lastKockbackDiffAttacker > 150L && attacked.getWorld() == attacker.getWorld()) {
                        double max = CheatConfiguration.REACH_DISTANCE;

                        max += 0.5;
                        max += Math.min(0.0, Math.abs(1.0 - Performance.getTps() / 20.0)) * 5.0;
                        max += attackerPing / 375.0;
                        max += (attacker.getLocation().getDirection().dot(attacked.getLocation().getDirection()) + 1.0) / 2.0 * 0.25;

                        if (attacker.isSprinting()) {
                            max += 0.1;
                        }

                        if (attacked.isSprinting()) {
                            max += 0.1;
                        }

                        if (!attacked.isOnGround()) {
                            max += 0.05;
                        }

                        for (PotionEffect potionEffect : attacked.getActivePotionEffects()) {
                            if (potionEffect.getType().getId() == PotionEffectType.SPEED.getId()) {
                                int amplifier = potionEffect.getAmplifier() + 1;
                                max += 0.15 * amplifier;
                                break;
                            }
                        }

                        for (PotionEffect potionEffect : attacker.getActivePotionEffects()) {
                            if (potionEffect.getType().getId() == PotionEffectType.SPEED.getId()) {
                                int amplifier = potionEffect.getAmplifier() + 1;
                                max += 0.15 * amplifier;
                                break;
                            }
                        }

                        Vector loc1 = Mathematics.getHorizontalVector(attacker.getLocation().toVector().subtract(currentDeltaAttacker).subtract(lastKnockbackVelocityAttacker));
                        Vector loc2 = Mathematics.getHorizontalVector(attacked.getLocation().toVector().add(currentDeltaAttacked).add(lastKnockbackVelocityAttacked));

                        double distance = loc1.distance(loc2);
                        double perc = distance / max;

                        if (distance > max) {
                            Managers.getCheckManager().logCheat(this, attacker, "Experimental", "Reach: " + new DecimalFormat("#.##").format(distance - max), "Percentage: " + (int)(perc * 100.0) + "%)");
                        }
                    }
                }
            }
        }
    }

}