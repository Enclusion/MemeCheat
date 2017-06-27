package me.joeleoli.memecheat.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Performance {

    private static Method getHandleMethod;
    private static Field pingField;

    private static double tps = 20.0;

    public static double getTps() {
        return tps;
    }

    public static void setTps(double tps) {
        Performance.tps = tps;
    }

    public static int getPing(Player player) {
        try {
            if (getHandleMethod == null) {
                getHandleMethod = player.getClass().getDeclaredMethod("getHandle");
                getHandleMethod.setAccessible(true);
            }

            Object entityPlayer = getHandleMethod.invoke(player);

            if (pingField == null) {
                pingField = entityPlayer.getClass().getDeclaredField("ping");
                pingField.setAccessible(true);
            }

            int ping = pingField.getInt(entityPlayer);

            return ping > 0 ? ping : 0;
        }
        catch (Exception e) {
            return 0;
        }
    }

}