package com.archerwn.mapRights.config;

import com.archerwn.mapRights.MapRights;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {
    private final MapRights plugin;

    @Getter
    private final File configFile;

    @Getter
    private FileConfiguration config;

    public ConfigManager(MapRights plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");

        loadConfig();
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public void saveConfig() {
        plugin.saveConfig();
    }
}
