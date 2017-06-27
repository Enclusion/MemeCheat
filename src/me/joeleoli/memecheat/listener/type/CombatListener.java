package me.joeleoli.memecheat.listener.type;

import com.comphenix.protocol.wrappers.EnumWrappers;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.event.player.PlayerClickEvaluateEvent;
import me.joeleoli.memecheat.event.update.UpdateEvent;
import me.joeleoli.memecheat.event.update.UpdateType;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.reflect.HitType;
import me.joeleoli.memecheat.reflect.MovingObjectPositionWrapper;
import me.joeleoli.memecheat.reflect.INMS;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.protocol.event.PacketSwingArmEvent;
import me.joeleoli.memecheat.protocol.event.PacketUseEntityEvent;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

public class CombatListener implements Listener {

    private Check miscellaneous;

    public CombatListener() {
        this.miscellaneous = Managers.getCheckManager().getCheck("Miscellaneous");
    }

    @EventHandler
    public void onPlayerPacket(PacketSwingArmEvent event) {
        if (Performance.getTps() <= this.miscellaneous.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();

        if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            return;
        }

        MovingObjectPositionWrapper position = INMS.get().getRay(player, false);

        if (position == null || (position.getHitType() != HitType.BLOCK && (position.getEntity() == null || (position.getEntity() instanceof Player && position.getEntity().getLocation().distance(player.getLocation()) > 0.5)))) {
            PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());
            profile.setValue(this.miscellaneous, "clicks", profile.getValueOrDefault(this.miscellaneous, "clicks", 0) + 1);
        }
    }

    @EventHandler
    public void onPlayerPacket(PacketUseEntityEvent event) {
        if (Performance.getTps() < 18.0) {
            return;
        }

        Player player = event.getAttacker();

        if (event.getAttacked() instanceof Player) {
            EnumWrappers.EntityUseAction action = event.getAction();

            if (action == EnumWrappers.EntityUseAction.ATTACK) {
                PlayerProfile profile = Managers.getProfileManager().getProfile(player.getUniqueId());
                profile.setValue(this.miscellaneous, "hits", profile.getValueOrDefault(this.miscellaneous, "hits", 0) + 1);
            }
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getUpdateType() == UpdateType.SEC) {
            for (PlayerProfile profile : Managers.getProfileManager().getProfiles()) {
                Player player = profile.getPlayer();

                int clicks = profile.getValueOrDefault(this.miscellaneous, "clicks", 0);
                int hits = profile.getValueOrDefault(this.miscellaneous, "hits", 0);

                Bukkit.getPluginManager().callEvent(new PlayerClickEvaluateEvent(player, profile, clicks, hits));

                profile.removeValue(this.miscellaneous, "clicks");
                profile.removeValue(this.miscellaneous, "hits");
            }
        }
    }

}