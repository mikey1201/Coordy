package com.mikey1201;

public class Coordinate {
    private final int x;
    private final int y;
    private final int z;
    private final String name;

    public Coordinate(double x, double y, double z, String name) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
        this.z = (int) Math.round(z);
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
    public String toString() {
        return name+" | X: "+x+", Y: "+y+", Z: "+z;
    }
}