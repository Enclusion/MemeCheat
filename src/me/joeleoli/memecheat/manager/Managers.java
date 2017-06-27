package me.joeleoli.memecheat.manager;

import me.joeleoli.memecheat.manager.type.CheckManager;
import me.joeleoli.memecheat.manager.type.ProfileManager;

import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class Managers {

    private static CheckManager checkManager;
    private static ProfileManager profileManager;

    public Managers(Plugin plugin) {
        this.register(Arrays.asList((checkManager = new CheckManager(plugin)), (profileManager = new ProfileManager(plugin))));
    }

    private void register(List<Manager> managers) {
        for (Manager manager : managers) {
            manager.register();
        }
    }

    public static CheckManager getCheckManager() {
        return checkManager;
    }

    public static ProfileManager getProfileManager() {
        return profileManager;
    }

}