package me.joeleoli.memecheat.util;

import org.bukkit.ChatColor;

public class Chat {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}