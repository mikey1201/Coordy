package com.mikey1201;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class UpdateChecker {
    public static void checkForUpdates(JavaPlugin plugin) {
        String version = plugin.getDescription().getVersion();
        Logger log = plugin.getLogger();
        try {
            log.info("Checking for a new update...");
            URL url = new URL("https://raw.githubusercontent.com/mikey1201/Coordy/main/Version");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String vers = br.readLine();
            if (!vers.equals(version)) {
                log.warning("New update available: "+vers);
            } else {
                log.info("No new update available");
            }
        } catch (IOException e) {
            log.severe("The UpdateChecker URL is invalid! Please let me know!");
        }
    }
}