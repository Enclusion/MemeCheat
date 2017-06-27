package me.joeleoli.memecheat.check;

import me.joeleoli.memecheat.CheatPlugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Check implements Listener {

    private Plugin plugin;
    private String identifier;
    private String name;
    private boolean registered;
    private boolean bannable;
    private boolean judgementDay;
    private int maxViolations;
    private int violationsToNotify;
    private long violationResetTime;
    private long notifyTime;
    private long burstTime;
    private double tpsThreshold;
    private int pingThreshold;

    public Check(Plugin plugin, String identifier, String name) {
        this.plugin = plugin;
        this.name = name;
        this.identifier = identifier;
        this.registered = false;
        this.bannable = true;
        this.judgementDay = false;
        this.maxViolations = 5;
        this.violationsToNotify = 2;
        this.violationResetTime = 600000L;
        this.notifyTime = 2000L;
        this.burstTime = 250L;
        this.tpsThreshold = 18.0;
        this.pingThreshold = 300;

        this.loadValues();
    }

    private void loadValues() {
        CheatPlugin.getInstance().getConfiguration().getFileConfig().loadCheckValues(this);
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public void register() {
        if (!this.registered) {
            this.registered = true;
            Bukkit.getServer().getPluginManager().registerEvents(this, this.plugin);
        }
    }

    public void unregister() {
        if (this.registered) {
            this.registered = false;
            HandlerList.unregisterAll(this);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean isBannable() {
        return this.bannable;
    }

    public void setBannable(boolean bannable) {
        this.bannable = bannable;
    }

    public boolean isJudgementDay() {
        return this.judgementDay;
    }

    public void setJudgementDay(boolean judgementDay) {
        this.judgementDay = judgementDay;
    }

    public int getMaxViolations() {
        return this.maxViolations;
    }

    public void setMaxViolations(int maxViolations) {
        this.maxViolations = maxViolations;
    }

    public int getViolationsToNotify() {
        return this.violationsToNotify;
    }

    public void setViolationsToNotify(int violationsToNotify) {
        this.violationsToNotify = violationsToNotify;
    }

    public long getViolationResetTime() {
        return this.violationResetTime;
    }

    public void setViolationResetTime(long violationResetTime) {
        this.violationResetTime = violationResetTime;
    }

    public long getNotifyTime() {
        return this.notifyTime;
    }

    public void setNotifyTime(long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public long getBurstTime() {
        return this.burstTime;
    }

    public void setBurstTime(long burstTime) {
        this.burstTime = burstTime;
    }

    public double getTpsThreshold() {
        return this.tpsThreshold;
    }

    public void setTpsThreshold(double threshold) {
        this.tpsThreshold = threshold;
    }

    public int getPingThreshold() {
        return this.pingThreshold;
    }

    public void setPingThreshold(int threshold) {
        this.pingThreshold = threshold;
    }

}