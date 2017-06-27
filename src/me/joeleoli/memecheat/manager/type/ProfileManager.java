package me.joeleoli.memecheat.manager.type;

import me.joeleoli.memecheat.CheatConfiguration;
import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.check.CheckInformation;
import me.joeleoli.memecheat.event.update.UpdateEvent;
import me.joeleoli.memecheat.event.update.UpdateType;
import me.joeleoli.memecheat.manager.Manager;
import me.joeleoli.memecheat.profile.PlayerProfile;
import me.joeleoli.memecheat.util.Chat;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager extends Manager {

    private Map<UUID, PlayerProfile> profiles;

    public ProfileManager(Plugin plugin) {
        super(plugin);

        this.profiles = new HashMap<>();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            this.profiles.put(player.getUniqueId(), new PlayerProfile(player.getUniqueId()));
        }
    }

    public PlayerProfile getProfile(UUID uuid) {
        return this.profiles.get(uuid);
    }

    public Collection<PlayerProfile> getProfiles() {
        return this.profiles.values();
    }

    public void alert(String message) {
        message = CheatConfiguration.PLUGIN_PREFIX + message;
        message = Chat.color(message);

        System.out.println(ChatColor.stripColor(message));

        for (PlayerProfile profile : this.profiles.values()) {
            if (profile.isAlert()) {
                profile.getPlayer().sendMessage(message);
            }
        }
    }

    public void alert(FancyMessage message) {
        System.out.println(ChatColor.stripColor(message.toOldMessageFormat()));

        for (PlayerProfile profile : this.profiles.values()) {
            if (profile.isAlert()) {
                message.send(profile.getPlayer());
            }
        }
    }

    public void handleDisconnect(Player player, PlayerProfile playerData, boolean kicked) {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = new PlayerProfile(player.getUniqueId());
        profile.setAlert(player.isOp() || player.hasPermission("memecheat.staff") || player.hasPermission("memecheat.admin"));
        this.profiles.put(player.getUniqueId(), profile);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.handleDisconnect(event.getPlayer(), this.profiles.remove(event.getPlayer().getUniqueId()), false);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        this.handleDisconnect(event.getPlayer(), this.profiles.remove(event.getPlayer().getUniqueId()), true);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        PlayerProfile data = this.profiles.get(player.getUniqueId());

        if (data != null) {
            data.setLastVelocityTime(System.currentTimeMillis());
            data.setLastVelocityVector(event.getVelocity());
        }
    }

    @EventHandler
    public void onPlayerKickFly(PlayerKickEvent event) {
        if (event.getReason().toLowerCase().contains("flying")) {
            String alert = CheatConfiguration.ALERT_KICKED
                    .replace("%PLAYER%", event.getPlayer().getName())
                    .replace("%KICK_REASON%", "flying");

            this.alert(alert);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpdate(UpdateEvent event) {
        long now = System.currentTimeMillis();

        if (event.getUpdateType() == UpdateType.TICK) {
            for (PlayerProfile profile : this.profiles.values()) {
                Player player = profile.getPlayer();
                Vector velocity = profile.getLastVelocityVector();

                if (velocity != null) {
                    long time = profile.getLastVelocityTime();

                    if (time + 500L > now) {
                        continue;
                    }

                    double velY = velocity.getY() * velocity.getY();
                    double Y = player.getVelocity().getY() * player.getVelocity().getY();

                    if (Y >= 0.02 && Y <= velY * 3.0) {
                        continue;
                    }

                    profile.resetVelocity();
                }
            }
        }
        else if (event.getUpdateType() == UpdateType.SEC) {
            for (PlayerProfile profile : this.profiles.values()) {
                for (Map.Entry<Check, CheckInformation> checkEntry : profile.getChecks().entrySet()) {
                    CheckInformation checkInformation = checkEntry.getValue();

                    if (checkInformation.getVl() > 0) {
                        long resetTime = checkInformation.getResetTime();

                        if (now < resetTime) {
                            continue;
                        }

                        checkInformation.reset();
                    }
                }
            }
        }
        else if (event.getUpdateType() == UpdateType.MIN_01) {
            for (PlayerProfile profile : this.profiles.values()) {
                Player player = profile.getPlayer();
                profile.setAlert(player.isOp() || player.hasPermission("memecheat.staff") || player.hasPermission("memecheat.admin"));
            }
        }
    }

}