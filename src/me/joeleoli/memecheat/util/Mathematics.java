package me.joeleoli.memecheat.util;

import me.joeleoli.memecheat.shape.Vector2D;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class Mathematics {

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);

        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public static double trim(int degree, double d) {
        String format = "#.#";

        for (int i = 1; i < degree; ++i) {
            format = String.valueOf(format) + "#";
        }

        DecimalFormat twoDForm = new DecimalFormat(format);

        return Double.valueOf(twoDForm.format(d));
    }

    public static double offset2D(Entity a, Entity b) {
        return offset2D(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2D(Location a, Location b) {
        return offset2D(a.toVector(), b.toVector());
    }

    public static double offset2D(Vector a, Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static double offset(Entity a, Entity b) {
        return offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static Vector getHorizontalVector(Vector v) {
        v.setY(0);

        return v;
    }

    public static Vector getVerticalVector(Vector v) {
        v.setX(0);
        v.setZ(0);

        return v;
    }

    public static long averageLong(List<Long> list) {
        long add = 0L;

        for (Long listlist : list) {
            add += listlist;
        }

        return add / list.size();
    }

    public static int averageInt(List<Integer> list) {
        int add = 0;

        for (Integer listlist : list) {
            add += listlist;
        }

        return add / list.size();
    }

    public static double averageDouble(List<Double> list) {
        Double add = 0.0;

        for (Double listlist : list) {
            add += listlist;
        }

        return add / list.size();
    }

    public static Vector2D rotate(double x, double z, double degrees) {
        double radians = java.lang.Math.toRadians(degrees);
        double px = java.lang.Math.cos(radians) * x - java.lang.Math.sin(radians) * z;
        double pz = java.lang.Math.sin(radians) * x + java.lang.Math.cos(radians) * z;

        return new Vector2D(px, pz);
    }

}