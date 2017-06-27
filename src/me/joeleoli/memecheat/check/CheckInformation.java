package me.joeleoli.memecheat.check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CheckInformation {

    private List<String> dumpLogs;
    private ConcurrentMap<String, Object> stored;
    private int vl;
    private long resetTime;
    private long lastNotified;
    private long lastAdded;

    public void reset() {
        this.vl = 0;
        this.resetTime = 0L;
        this.lastAdded = 0L;
        this.lastNotified = 0L;
    }

    public CheckInformation() {
        this.dumpLogs = Collections.synchronizedList(new ArrayList<>());
        this.stored = new ConcurrentHashMap<>();
        this.vl = 0;
        this.resetTime = 0L;
    }

    public long getLastAdded() {
        return this.lastAdded;
    }

    public void setLastAdded(long lastAdded) {
        this.lastAdded = lastAdded;
    }

    public long getLastNotified() {
        return this.lastNotified;
    }

    public void setLastNotified(long lastNotified) {
        this.lastNotified = lastNotified;
    }

    public int getVl() {
        return this.vl;
    }

    public void setViolationLevel(int vl) {
        this.vl = vl;
    }

    public long getResetTime() {
        return this.resetTime;
    }

    public void setResetTime(long resetTime) {
        this.resetTime = resetTime;
    }

    public List<String> getDumpLogs() {
        return this.dumpLogs;
    }

    public void setDumpLogs(List<String> dumpLogs) {
        this.dumpLogs = dumpLogs;
    }

    public ConcurrentMap<String, Object> getStored() {
        return this.stored;
    }

    public void setStored(ConcurrentMap<String, Object> stored) {
        this.stored = stored;
    }

}