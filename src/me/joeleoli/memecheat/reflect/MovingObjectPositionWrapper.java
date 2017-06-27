package me.joeleoli.memecheat.reflect;

import me.joeleoli.memecheat.shape.Vector3D;
import org.bukkit.entity.Entity;

public class MovingObjectPositionWrapper {

    private Object handle;
    private Entity entity;
    private int x;
    private int y;
    private int z;
    private int face;
    private Vector3D pos;
    private HitType hitType;

    public void setHandle(Object handle) {
        this.handle = handle;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public void setPos(Vector3D pos) {
        this.pos = pos;
    }

    public void setHitType(HitType hitType) {
        this.hitType = hitType;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getFace() {
        return this.face;
    }

    public int getZ() {
        return this.z;
    }

    public Vector3D getPos() {
        return this.pos;
    }

    public HitType getHitType() {
        return this.hitType;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Object getHandle() {
        return this.handle;
    }

    @Override
    public String toString() {
        return "HitResult{type=" + this.hitType + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", f=" + this.face + ", pos=" + this.pos + ", entity=" + this.entity + '}';
    }

}
