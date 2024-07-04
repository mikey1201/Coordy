package com.mikey1201;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPILogger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Coordy extends JavaPlugin implements CommandExecutor {
    private static Coordy plugin;
    public Coordy() {
    }
    public static Coordy getPlugin() {
        return plugin;
    }
    @Override
    public void onLoad() {
        CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true).dispatcherFile(new File(getDataFolder(),"command_registration.json")));
    }
    @Override
    public void onEnable() {
        plugin = this;

        CommandAPI.onEnable();

        new CoordsCommand().registerCoordsCommand();

        initDeathListener();

    }
    private void initDeathListener() {
        if (getConfig().getBoolean("death-coords-enabled")) {
            this.getServer().getPluginManager().registerEvents(new CoordyDeathListener(), this);
        }
    }
}
