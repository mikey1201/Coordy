package com.mikey1201.bstats;

import org.bukkit.plugin.java.JavaPlugin;

public class MetricsManager {
    private static final int serviceId = 22585;
    public static void init(JavaPlugin plugin) {
        plugin.getLogger().info("Initializing bStats");
        Metrics metrics = new Metrics(plugin, serviceId);
    }
}
