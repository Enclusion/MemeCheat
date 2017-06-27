package me.joeleoli.memecheat.util;

import me.joeleoli.memecheat.check.Check;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileConfig {

    private File file;
    private FileConfiguration config;

    public FileConfig(Plugin plugin, String fileName) {
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();

            if (plugin.getResource(fileName) == null) {
                try {
                    this.file.createNewFile();
                }
                catch (IOException e) {
                    plugin.getLogger().severe("Failed to create new file " + fileName);
                }
            }
            else {
                plugin.saveResource(fileName, false);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void loadCheckValues(Check check) {
        if (!this.config.contains("checks." + check.getIdentifier().toLowerCase())) {
            System.out.println("Failed to load check values (" + check.getName() + ")");
            return;
        }

        String path = "checks." + check.getIdentifier().toLowerCase() + ".";

        if (this.config.contains(path + "bannable")) {
            check.setBannable(this.config.getBoolean(path + "bannable"));
        }

        if (this.config.contains(path + "judgement-day")) {
            check.setJudgementDay(this.config.getBoolean(path + "judgement-day"));
        }

        if (this.config.contains(path + "max-violations")) {
            check.setMaxViolations(this.config.getInt(path + "max-violations"));
        }

        if (this.config.contains(path + "violations-notify")) {
            check.setViolationsToNotify(this.config.getInt(path + "violations-notify"));
        }

        if (this.config.contains(path + "violations-reset")) {
            check.setViolationResetTime(this.config.getLong(path + "violations-reset"));
        }

        if (this.config.contains(path + "notify-time")) {
            check.setNotifyTime(this.config.getLong(path + "notify-time"));
        }

        if (this.config.contains(path + "burst-time")) {
            check.setBurstTime(this.config.getLong(path + "burst-time"));
        }

        if (this.config.contains(path + "tps-threshold")) {
            check.setTpsThreshold(this.config.getDouble(path + "tps-threshold"));
        }

        if (this.config.contains(path + "ping-threshold")) {
            check.setPingThreshold(this.config.getInt(path + "ping-threshold"));
        }
    }

    public void saveCheckValues(Check check) {
        String path = "checks." + check.getIdentifier().toLowerCase() + ".";

        this.config.set(path + "bannable", check.isBannable());
        this.config.set(path + "judgement-day", check.isJudgementDay());
        this.config.set(path + "max-violations", check.getMaxViolations());
        this.config.set(path + "violations-notify", check.getViolationsToNotify());
        this.config.set(path + "violations-reset", check.getViolationResetTime());
        this.config.set(path + "notify-time", check.getNotifyTime());
        this.config.set(path + "burst-time", check.getBurstTime());
        this.config.set(path + "tps-threshold", check.getTpsThreshold());
        this.config.set(path + "ping-threshold", check.getPingThreshold());

        this.save();
    }

    public void save() {
        try {
            this.getConfig().save(this.file);
        }
        catch (IOException e) {
            Bukkit.getLogger().severe("Could not save config file " + this.file.toString());
            e.printStackTrace();
        }
    }

}