package com.mikey1201;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Coordinate {
    private final int x;
    private final int y;
    private final int z;
    private final String label;

    public Coordinate(String label, Player player) {
        this.label = label;
        Location location = player.getLocation();
        this.x = (int) Math.round(location.getX());
        this.y = (int) Math.round(location.getY());
        this.z = (int) Math.round(location.getZ());
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return label+" | X: "+x+", Y: "+y+", Z: "+z;
    }
}