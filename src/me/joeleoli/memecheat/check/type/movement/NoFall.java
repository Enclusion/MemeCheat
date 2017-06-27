package me.joeleoli.memecheat.check.type.movement;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.event.player.PacketedMovementEvent;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Analyze;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Set;

public class NoFall extends Check {

    public NoFall(Plugin plugin) {
        super(plugin, "NoFall", "NoFall");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PacketedMovementEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.getAllowFlight()) {
            return;
        }

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        if (player.getVehicle() != null) {
            return;
        }

        if (player.getHealth() <= 0.0) {
            return;
        }

        Vector location = player.getLocation().toVector();
        Material CURRENT = event.getBlockAt(location.clone()).getItemType();

        if (CURRENT == Material.WATER || CURRENT == Material.STATIONARY_WATER || CURRENT == Material.VINE || CURRENT == Material.LADDER) {
            return;
        }

        Set<Material> CLIMBABLE = Analyze.getSurrounding(event, location.clone());

        if (CLIMBABLE.contains(Material.VINE) || CLIMBABLE.contains(Material.VINE)) {
            return;
        }

        Material BELOW = event.getBlockAt(location.clone().subtract(new Vector(0, 1, 0))).getItemType();
        Material HALF_BELOW = event.getBlockAt(location.clone().subtract(new Vector(0.0, 0.5, 0.5))).getItemType();

        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        if (profile != null) {
            double fallDistance = 0.0;

            if (!Analyze.onGround(BELOW, HALF_BELOW) && event.getFrom().getY() > event.getTo().getY()) {
                fallDistance = profile.getValueOrDefault(this, "fallDistance", fallDistance);
                fallDistance += event.getFrom().getY() - event.getTo().getY();
            }

            profile.setValue(this, "fallDistance", fallDistance);

            if (fallDistance < 2.0) {
                return;
            }

            long now = System.currentTimeMillis();
            long time = profile.getValueOrDefault(this, "time", now);
            int count = profile.getValueOrDefault(this, "count", 0);

            if (player.isOnGround() || player.getFallDistance() == 0.0F) {
                ++count;
            }
            else {
                count = 0;
            }

            if (time + 10000L < now) {
                count = 0;
                time = now;
            }

            if (count >= 3) {
                count = 0;
                profile.setValue(this, "fallDistance", 0.0);
                Managers.getCheckManager().logCheat(this, player);
            }

            profile.setValue(this, "time", time);
            profile.setValue(this, "count", count);
        }
    }

}