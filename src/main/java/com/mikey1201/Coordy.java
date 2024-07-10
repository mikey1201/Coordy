package com.mikey1201;

import com.mikey1201.bstats.MetricsManager;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPILogger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Coordy extends JavaPlugin implements CommandExecutor {
    private static Coordy plugin;

    public Coordy() {}

    public static Coordy getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true).dispatcherFile(new File(getDataFolder(),"command_registration.json")));
    }
    /*TODO:
        add configuration options
        overhaul permissions
        work on text formatting
        help commands
        bstats custom chart?
        auto update
     */
    @Override
    public void onEnable() {
        //TODO: More robust logging
        plugin = this;

        UpdateChecker.checkForUpdates(this, "https://raw.githubusercontent.com/mikey1201/Coordy/main/Version");

        saveDefaultConfig();

        PermissionsManager.loadPermissions(this);

        MetricsManager.init(this);
        //TODO: Custom charts?
        CommandAPI.onEnable();

        new CoordsCommand().registerCoordsCommand();

        CoordyDeathListener.registerDeathListener(getConfig(), this);
    }


}
