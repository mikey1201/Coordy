package com.mikey1201;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ComponentManager {
    public static Component noPermission(String rawArg) {
        return Component.text("You do not have permission to use "+rawArg);
    }
    public static Component coordinate(Coordinate coord) {
        return Component.text(coord.toString());
    }
    public static Component coordListHeading(Player player) {
        return Component.text(player.getName()+"'s coordinates:");
    }
    public static Component noCoords() {
        return Component.text("You have no coordinates saved.");
    }
    public static Component addSuccess(String label) {
        return Component.text("Successfully added "+label+".");
    }
    public static Component addFailure(String label) {
        return Component.text(label+" already exists.");
    }
    public static Component removeSuccess(String label) {
        return Component.text("Successfully removed "+label+".");
    }
    public static Component removeFailure(String label) {
        return Component.text(label+" not found.");
    }
    public static Component playerNotFound() {
        return Component.text("Player not found.");
    }
}
