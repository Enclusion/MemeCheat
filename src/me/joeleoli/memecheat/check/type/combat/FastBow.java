package me.joeleoli.memecheat.check.type.combat;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;

public class FastBow extends Check {

    public FastBow(Plugin plugin) {
        super(plugin, "FastBow", "FastBow");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBow(EntityShootBowEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!player.hasPermission("memecheat.combat.bypass")) {
                PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

                long lastShoot = profile.getValueOrDefault(this, "lastShoot", 0L);
                long now = System.currentTimeMillis();
                long diff = now - lastShoot;
                long expectedDuration = (long)(1000.0f * event.getForce() - Math.max(100.0, Performance.getPing(player) / 10.0));

                if (diff < expectedDuration) {
                    Managers.getCheckManager().logCheat(this, player, "Time: +" + (expectedDuration - diff) + "ms");
                }

                profile.setValue(this, "lastShoot", now);
            }
        }
    }

}