package me.joeleoli.memecheat.reflect;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class INMS {
    
    private static INMS nms;

    public static INMS get() {
        if (nms == null) {
            String[] split = Bukkit.getBukkitVersion().split("\\.");
            String serverVersion = split[0] + "_" + split[1] + "_R" + split[3].split("\\-")[0];

            try {
                Class<? extends INMS> providerClass = Class.forName(INMS.class.getPackage().getName() + ".v" + serverVersion + ".NMS").asSubclass(INMS.class);
                nms = providerClass.newInstance();
            }
            catch (Exception e) {
                throw new RuntimeException("Could not find version provider ", e);
            }
        }

        return INMS.nms;
    }

    public abstract Entity getEntity(World p0, int p1);

    public abstract MovingObjectPositionWrapper getRay(Player p0, boolean p1);

}
