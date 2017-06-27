package me.joeleoli.memecheat;

import me.joeleoli.memecheat.util.FileConfig;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class CheatConfiguration {

    public static boolean ENABLED = true;

    public static final String PLUGIN_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "MemeCheat" + ChatColor.DARK_GRAY + "] ";

    public static String ALERT_FORMAT = "{\"text\":\"\",\"extra\":[{\"text\":\"\",\"color\":\"white\"},{\"text\":\"[\",\"color\":\"dark_gray\"},{\"text\":\"MemeCheat\",\"color\":\"red\"},{\"text\":\"] \",\"color\":\"dark_gray\"},{\"text\":\"%PLAYER%\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tp %PLAYER%\"}},{\"text\":\" has failed \",\"color\":\"gray\"},{\"text\":\"%CHEAT%\",\"color\":\"red\"},{\"text\":\" (\",\"color\":\"dark_gray\"},{\"text\":\"%VIOLATION_LEVEL%VL\",\"color\":\"red\"},{\"text\":\") \",\"color\":\"dark_gray\"},{\"text\":\"(\",\"color\":\"dark_gray\"},{\"text\":\"%DATA%\",\"color\":\"red\"},{\"text\":\")\",\"color\":\"dark_gray\"}]}";
    public static String ALERT_KICKED = "&c%PLAYER% &7has been kicked for &c%KICK_REASON%Z&7.";

    public static String BAN_KICK_MESSAGE = ChatColor.RED + "You have been suspended for cheating.\nDetails: (%CHEAT%) (%VIOLATION_LEVEL%VL)";
    public static String BAN_COMMAND = "ban %PLAYER% Cheating (%CHEAT%) (%VIOLATION_LEVEL%)";

    public static int AUTO_CLICK_CLICKS = 14;

    public static int KILL_AURA_CLICKS = 14;
    public static int KILL_AURA_HITS = 14;
    public static int KILL_AURA_PING_ACCURACY = 200;

    public static double REACH_DISTANCE = 3.3;

    private FileConfig config;

    CheatConfiguration(Plugin plugin) {
        this.config = new FileConfig(plugin, "config.yml");

        this.loadValues();
    }

    public FileConfig getFileConfig() {
        return this.config;
    }

    private void loadValues() {
        FileConfiguration config = this.config.getConfig();

        if (config.contains("alert.failed-check")) {
            ALERT_FORMAT = config.getString("alert.failed-check");
        }

        if (config.contains("alert.kicked")) {
            ALERT_KICKED = config.getString("alert.kicked");
        }

        if (config.contains("auto-ban.ban-kick-message")) {
            BAN_KICK_MESSAGE = config.getString("auto-ban.ban-kick-message");
        }

        if (config.contains("auto-ban.ban-command")) {
            BAN_COMMAND = config.getString("auto-ban.ban-command");
        }

        if (config.contains("check-data.autoclicker.clicks")) {
            AUTO_CLICK_CLICKS = config.getInt("check-data.autoclicker.clicks");
        }

        if (config.contains("check-data.killaura.clicks")) {
            KILL_AURA_CLICKS = config.getInt("check-data.killaura.clicks");
        }

        if (config.contains("check-data.killaura.hits")) {
            KILL_AURA_HITS = config.getInt("check-data.killaura.hits");
        }

        if (config.contains("check-data.killaura.ping-accuracy")) {
            KILL_AURA_PING_ACCURACY = config.getInt("check-data.killaura.ping-accuracy");
        }

        if (config.contains("check-data.reach.distance")) {
            REACH_DISTANCE = config.getDouble("check-data.reach.distance");
        }
    }

}