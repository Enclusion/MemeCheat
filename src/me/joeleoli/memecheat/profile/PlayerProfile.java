package me.joeleoli.memecheat.profile;

import me.joeleoli.memecheat.check.Check;
import me.joeleoli.memecheat.check.CheckInformation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PlayerProfile {

    private UUID playerUuid;
    private ConcurrentMap<Check, CheckInformation> checkInformation;
    private Vector lastVelocityVector;
    private long lastVelocityTime;
    private boolean alert;
    private List<String> mods;

    public PlayerProfile(UUID uuid) {
        this.playerUuid = uuid;
        this.checkInformation = new ConcurrentHashMap<>();
        this.mods = new ArrayList<>();
        this.lastVelocityVector = null;
        this.lastVelocityTime = 0L;
        this.alert = false;
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(this.playerUuid);
    }

    public void resetViolations() {
        this.checkInformation.clear();
    }

    public void resetViolations(Check check) {
        this.checkInformation.remove(check);
    }

    public void resetVelocity() {
        this.lastVelocityVector = null;
        this.lastVelocityTime = 0L;
    }

    public CheckInformation getInformation(Check check) {
        CheckInformation checkInformation = this.checkInformation.get(check);

        if (checkInformation == null) {
            checkInformation = new CheckInformation();
            this.checkInformation.put(check, checkInformation);
        }

        return checkInformation;
    }

    public ConcurrentMap<Check, CheckInformation> getChecks() {
        return this.checkInformation;
    }

    public long getLastVelocityTime() {
        return this.lastVelocityTime;
    }

    public void setLastVelocityTime(long lastVelocityTime) {
        this.lastVelocityTime = lastVelocityTime;
    }

    public Vector getLastVelocityVector() {
        return this.lastVelocityVector;
    }

    public void setLastVelocityVector(Vector lastVelocityVector) {
        this.lastVelocityVector = lastVelocityVector;
    }

    public boolean isAlert() {
        return this.alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public <T> void setValue(Check check, String s, T o) {
        this.getInformation(check).getStored().put(s, o);
    }

    public <T> void setValueIfAbsent(Check check, String s, T o) {
        this.getInformation(check).getStored().putIfAbsent(s, o);
    }

    public <T> T getValue(Check check, String s) {
        CheckInformation checkInformation = this.getInformation(check);
        Object o = checkInformation.getStored().get(s);

        if (o == null) {
            return null;
        }

        return (T)o;
    }

    public <T> T getValueOrDefault(Check check, String s, T def) {
        CheckInformation checkInformation = this.getInformation(check);
        Object o = checkInformation.getStored().getOrDefault(s, def);

        return (T)o;
    }

    public <T> T removeValue(Check check, String s) {
        return (T)this.getInformation(check).getStored().remove(s);
    }

    public List<String> getMods() {
        return this.mods;
    }

    public void setMods(List<String> mods) {
        this.mods = mods;
    }

}