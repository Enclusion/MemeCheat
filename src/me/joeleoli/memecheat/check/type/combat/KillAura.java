package me.joeleoli.memecheat.check.type.combat;

import me.joeleoli.memecheat.CheatConfiguration;
import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.event.player.PlayerClickEvaluateEvent;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.util.Performance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

public class KillAura extends Check {

    public KillAura(Plugin plugin) {
        super(plugin, "KillAura", "KillAura (High)");
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

        int eval = (ping < CheatConfiguration.KILL_AURA_PING_ACCURACY) ? 0 : ((int)Math.floor(ping / 200.0));
        int hits = event.getHits() - eval;
        int clicks = event.getClicks() - eval;

        if (clicks >= CheatConfiguration.KILL_AURA_CLICKS && hits >= CheatConfiguration.KILL_AURA_HITS) {
           Managers.getCheckManager().logCheat(this, player, "CPS: " + clicks, "Hits: " + hits);
        }
    }

}