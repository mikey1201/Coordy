package com.mikey1201;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class CoordyDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent e) {
        Player p = e.getEntity();
        Location loc = p.getLocation();
        p.sendMessage(Component.text("You died at | X: "+Math.round(loc.getX())+ " | Y: "+Math.round(loc.getY())+ " | Z: "+Math.round(loc.getZ()))
                .color(TextColor.color(255,0,0)));
    }
}
