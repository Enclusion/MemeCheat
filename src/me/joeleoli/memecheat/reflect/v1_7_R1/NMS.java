package me.joeleoli.memecheat.reflect.v1_7_R1;

import me.joeleoli.memecheat.reflect.*;
import me.joeleoli.memecheat.shape.Vector3D;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MovingObjectPosition;
import net.minecraft.server.v1_7_R4.Vec3D;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class NMS extends INMS {
    
    @Override
    public Entity getEntity(World world, int i) {
        if (world == null) {
            return null;
        }
        
        net.minecraft.server.v1_7_R4.Entity entity = ((CraftWorld)world).getHandle().getEntity(i);
        
        if (entity == null) {
            return null;
        }
        
        return entity.getBukkitEntity();
    }

    public static MovingObjectPositionWrapper fromHandle(MovingObjectPosition handle) {
        MovingObjectPositionWrapper movingObjectPositionWrapper = new MovingObjectPositionWrapper();
        movingObjectPositionWrapper.setHandle(handle);

        if (handle.entity != null) {
            movingObjectPositionWrapper.setEntity(handle.entity.getBukkitEntity());
        }

        movingObjectPositionWrapper.setX(handle.b);
        movingObjectPositionWrapper.setY(handle.c);
        movingObjectPositionWrapper.setZ(handle.d);
        movingObjectPositionWrapper.setFace(handle.face);

        if (handle.pos != null) {
            double vecX = handle.pos.a;
            double vecY = handle.pos.b;
            double vecZ = handle.pos.c;
            movingObjectPositionWrapper.setPos(new Vector3D(vecX, vecY, vecZ));
        }

        movingObjectPositionWrapper.setHitType(HitType.values()[handle.type.ordinal()]);

        return movingObjectPositionWrapper;
    }

    @Override
    public MovingObjectPositionWrapper getRay(Player player, boolean b) {
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        float pitch = entityPlayer.pitch;
        float yaw = entityPlayer.yaw;
        double locX = entityPlayer.locX;
        double locY = entityPlayer.locY;
        double locZ = entityPlayer.locZ;
        float lastPitch = entityPlayer.lastPitch;
        double lastX = entityPlayer.lastX;
        double lastY = entityPlayer.lastY;
        double lastZ = entityPlayer.lastZ;
        float lastYaw = entityPlayer.lastYaw;
        float height = entityPlayer.height;
        GameMode gameMode = player.getGameMode();
        float f = 1.0f;
        float f2 = lastPitch + (pitch - lastPitch) * f;
        float f3 = lastYaw + (yaw - lastYaw) * f;
        double d0 = lastX + (locX - lastX) * f;
        double d2 = lastY + (locY - lastY) * f + 1.62 - height;
        double d3 = lastZ + (locZ - lastZ) * f;
        Vector3D vec1 = new Vector3D(d0, d2, d3);
        float f4 = NMSMath.cos(-f3 * 0.017453292f - 3.1415927f);
        float f5 = NMSMath.sin(-f3 * 0.017453292f - 3.1415927f);
        float f6 = -NMSMath.cos(-f2 * 0.017453292f);
        float f7 = NMSMath.sin(-f2 * 0.017453292f);
        float f8 = f5 * f6;
        float f9 = f4 * f6;
        double d4 = (gameMode == GameMode.CREATIVE) ? 5.0 : 4.5;
        Vector3D vec2 = vec1.add(f8 * d4, f7 * d4, f9 * d4);
        Vec3D vec3d = Vec3D.a(vec1.getX(), vec1.getY(), vec1.getZ());
        Vec3D vec3d2 = Vec3D.a(vec2.getX(), vec2.getY(), vec2.getZ());
        net.minecraft.server.v1_7_R4.World world = entityPlayer.world;
        MovingObjectPosition movingObjectPosition = world.rayTrace(vec3d, vec3d2, b);

        if (movingObjectPosition == null) {
            return null;
        }

        return fromHandle(movingObjectPosition);
    }

}