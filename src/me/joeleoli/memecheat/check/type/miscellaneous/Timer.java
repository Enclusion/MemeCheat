package me.joeleoli.memecheat.check.type.miscellaneous;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.protocol.event.PacketPlayerEvent;
import me.joeleoli.memecheat.util.Mathematics;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Timer extends Check {

    public Timer(Plugin plugin) {
        super(plugin, "Timer", "Speed (Timer)");
    }

    @EventHandler
    public void onPlayerPacket(PacketPlayerEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.hasPermission("memecheat.movement.bypass") || player.hasPermission("memecheat.timer.bypass")) {
            return;
        }

        if (player.getAllowFlight()) {
            return;
        }

        if (player.getVehicle() != null) {
            return;
        }

        if (Performance.getPing(player) >= this.getPingThreshold()) {
            return;
        }

        PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());

        long lastPacket = profile.getValueOrDefault(this, "lastPacket", 0L);
        long now = System.currentTimeMillis();
        int diff = (int)(now - lastPacket);

        profile.setValue(this, "lastPacket", now);

        List<Integer> packets = profile.getValue(this, "packets");

        if (packets == null) {
            packets = new ArrayList<>();
            profile.setValue(this, "packets", packets);
            profile.setValue(this, "start", now);
        }

        packets.add(diff);

        if (packets.size() == 20) {
            if (!packets.contains(0) && Mathematics.averageInt(packets) < 45) {
                int count = profile.getValueOrDefault(this, "count", 0) + 1;

                if (count >= 4) {
                    long start = profile.getValue(this, "start");
                    long diffCount = now - start;

                    if (diffCount < 4000L) {
                        Location location = player.getLocation();
                        Block block = location.getBlock();

                        if (!block.getType().isSolid()) {
                            Managers.getCheckManager().logCheat(this, player);
                            return;
                        }
                    }

                    profile.removeValue(this, "count");
                    profile.removeValue(this, "start");
                    profile.removeValue(this, "packets");
                }
                else {
                    profile.setValue(this, "count", count);
                }
            }

            packets.clear();
        }
    }

}