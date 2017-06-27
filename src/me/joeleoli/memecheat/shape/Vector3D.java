package me.joeleoli.memecheat.shape;

public class Vector3D {

    private double x;
    private double y;
    private double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector3D add(double dx, double dy, double dz) {
        return new Vector3D(this.x + dx, this.y + dy, this.z + dz);
    }

    @Override
    public String toString() {
        return "Vector3D{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
    }

}