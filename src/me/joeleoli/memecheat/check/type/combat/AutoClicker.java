package me.joeleoli.memecheat.check.type.combat;

import me.joeleoli.memecheat.CheatConfiguration;
import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.event.player.PlayerClickEvaluateEvent;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

public class AutoClicker extends Check {

    public AutoClicker(Plugin plugin) {
        super(plugin, "AutoClicker", "AutoClicker (Average)");
    }

    @EventHandler
    public void onClickEvaluation(PlayerClickEvaluateEvent event) {
        if (Performance.getTps() <= this.getTpsThreshold()) {
            return;
        }

        Player player = event.getPlayer();

        int ping = Performance.getPing(player);

        if (ping > this.getPingThreshold()) {
            return;
        }

        int clicks = event.getClicks();

        if (clicks >= CheatConfiguration.AUTO_CLICK_CLICKS) {
            Managers.getCheckManager().logCheat(this, player, "CPS: " + clicks);
        }
    }

}