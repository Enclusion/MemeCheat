package me.joeleoli.memecheat.manager.type;

import me.joeleoli.memecheat.CheatConfiguration;
import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.check.CheckInformation;
import me.joeleoli.memecheat.check.type.combat.AutoClicker;
import me.joeleoli.memecheat.check.type.combat.FastBow;
import me.joeleoli.memecheat.check.type.combat.KillAura;
import me.joeleoli.memecheat.check.type.combat.Reach;
import me.joeleoli.memecheat.check.type.miscellaneous.Miscellaneous;
import me.joeleoli.memecheat.check.type.miscellaneous.Timer;
import me.joeleoli.memecheat.check.type.movement.*;
import me.joeleoli.memecheat.event.player.PlayerViolationEvent;
import me.joeleoli.memecheat.manager.Manager;
import me.joeleoli.memecheat.manager.Managers;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Performance;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CheckManager extends Manager {

    private Map<String, Check> checks = new HashMap<>();

    public CheckManager(Plugin plugin) {
        super(plugin);

        List<Check> checks = new ArrayList<>();

        checks.add(new Glide(plugin));
        checks.add(new Ascension(plugin));
        checks.add(new Speed(plugin));
        checks.add(new Fly(plugin));
        checks.add(new NoFall(plugin));
        checks.add(new VClip(plugin));
        checks.add(new AutoClicker(plugin));
        checks.add(new KillAura(plugin));
        checks.add(new Timer(plugin));
        checks.add(new FastBow(plugin));
        checks.add(new Reach(plugin));
        checks.add(new Miscellaneous(plugin));

        this.registerChecks(checks);
    }

    private void registerChecks(List<Check> checks) {
        for (Check check : checks) {
            this.checks.put(check.getIdentifier(), check);
            check.register();
        }
    }

    public void registerCheck(String checkIdentifier) {
        if (this.checks.containsKey(checkIdentifier)) {
            this.checks.get(checkIdentifier).register();
        }
    }

    public void unregisterCheck(String checkIdentifier) {
        if (this.checks.containsKey(checkIdentifier)) {
            this.checks.get(checkIdentifier).unregister();
        }
    }

    public Check getCheck(String checkIdentifier) {
        return this.checks.get(checkIdentifier);
    }

    public void logCheat(Check check, Player player, String... data) {
        long now = System.currentTimeMillis();

        PlayerProfile playerData = Managers.getProfileManager().getProfile(player.getUniqueId());

        PlayerViolationEvent playerViolationEvent = new PlayerViolationEvent(player, check, playerData);
        Bukkit.getServer().getPluginManager().callEvent(playerViolationEvent);

        if (playerViolationEvent.isCancelled()) {
            return;
        }

        CheckInformation checkInformation = playerData.getInformation(check);

        int violationLevel = checkInformation.getVl() + 1;

        if (checkInformation.getLastAdded() + check.getBurstTime() > now) {
            return;
        }

        checkInformation.setLastAdded(now);
        checkInformation.setViolationLevel(violationLevel);
        checkInformation.setResetTime(System.currentTimeMillis() + check.getViolationResetTime());

        Bukkit.getServer().getLogger().log(Level.INFO, player.getName() + " failed " + check.getName());

        StringBuilder dataBind = new StringBuilder();

        if (data != null && data.length > 0) {
            int i = 0;

            for (String bind : data) {
                i++;

                dataBind.append(bind);

                if (i != data.length) {
                    dataBind.append(" - ");
                }
            }
        }

        String json = CheatConfiguration.ALERT_FORMAT
                .replace("%PLAYER%", player.getName())
                .replace("%CHEAT%", check.getName())
                .replace("%VIOLATION_LEVEL%", violationLevel + "")
                .replace("%DATA%", data == null || data.length == 0 ? "" : dataBind.toString())
                .replace("%PING%", Performance.getPing(player) + "")
                .replace("%TPS%", Performance.getTps() + "");

        FancyMessage jsonAlert = FancyMessage.deserialize(json);

        if (violationLevel >= check.getMaxViolations() && check.isBannable()) {
            String command = CheatConfiguration.BAN_COMMAND
                    .replace("%PLAYER%", player.getName())
                    .replace("%CHEAT%", check.getName())
                    .replace("%VIOLATION_LEVEL%", violationLevel + "")
                    .replace("%DATA%", data == null || data.length == 0 ? "" : dataBind.toString())
                    .replace("%PING%", Performance.getPing(player) + "")
                    .replace("%TPS%", Performance.getTps() + "");

            String kick = CheatConfiguration.BAN_KICK_MESSAGE
                    .replace("%PLAYER%", player.getName())
                    .replace("%CHEAT%", check.getName())
                    .replace("%VIOLATION_LEVEL%", violationLevel + "")
                    .replace("%DATA%", data == null || data.length == 0 ? "" : dataBind.toString())
                    .replace("%PING%", Performance.getPing(player) + "")
                    .replace("%TPS%", Performance.getTps() + "");

            player.kickPlayer(kick);

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

            Managers.getProfileManager().alert(jsonAlert);
        }
        else if (violationLevel >= check.getViolationsToNotify()) {
            long lastNotified = checkInformation.getLastNotified();

            if (lastNotified + check.getNotifyTime() < now) {
                checkInformation.setLastNotified(now);

                Managers.getProfileManager().alert(jsonAlert);
            }
        }
    }

}