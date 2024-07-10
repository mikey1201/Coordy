package com.mikey1201;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PermissionsManager {
    public static void loadPermissions(JavaPlugin plugin) {
        plugin.getLogger().info("Loading permissions from config.yml");
        FileConfiguration config = plugin.getConfig();
        String defaultPermission = Objects.requireNonNull(config.getString("share-coords-with-all")).toLowerCase();
        Permission permission = new Permission("coordy.commands.share.@a");
        switch (defaultPermission) {
            case "true" -> permission.setDefault(PermissionDefault.TRUE);
            case "op" -> permission.setDefault(PermissionDefault.OP);
            case "not op" -> permission.setDefault(PermissionDefault.NOT_OP);
            default -> permission.setDefault(PermissionDefault.FALSE);
        }
        plugin.getServer().getPluginManager().addPermission(permission);
    }
}
