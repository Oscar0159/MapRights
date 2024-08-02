package com.archerwn.mapRights.manager;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {

    @Getter
    private static final ConfigManager instance = new ConfigManager();

    @Getter
    private YamlConfiguration config;

    @Getter
    private File configFile;

    private JavaPlugin plugin;

    private ConfigManager() {
    }

    public void setup(JavaPlugin plugin) {
        this.plugin = plugin;

        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
