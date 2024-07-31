package com.archerwn.mapRights.config;

import com.archerwn.mapRights.MapRights;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangManager {
    private final MapRights plugin;

    @Getter
    private File langFile;

    @Getter
    private String lang;

    @Getter
    private FileConfiguration langConfig;

    public LangManager(MapRights plugin) {
        this.plugin = plugin;
    }

    public void loadLang(String lang) {
        this.lang = lang;

        langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");

        if (!langFile.exists()) {
            plugin.saveResource("lang/" + lang + ".yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    public void reloadLang() {
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }
}
