package com.archerwn.mapRights.manager;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;

public class LangManager {
    @Getter
    private static final LangManager instance = new LangManager();

    @Getter
    private static final String DEFAULT_LANGUAGE = "en_US";

    @Getter
    private static final String[] LANGUAGES = new String[]{"en_US", "zh_TW", "zh_CN"};

    @Getter
    private YamlConfiguration lang;

    @Getter
    private File langFile;

    @Getter
    private String language;

    private JavaPlugin plugin;

    private LangManager() {
    }

    public String get(String key) {
        String messagePrefix = lang.getString("message.prefix");
        String message = getLang().getString(key);

        if (message == null) {
            return key;
        }

        if (key.startsWith("message.")) {
            return messagePrefix + message;
        }

        return message;
    }

    public void setup(JavaPlugin plugin, String language) {
        this.plugin = plugin;

        // Check if the language files exist, if not, create them, otherwise, update them
        for (String lang : LANGUAGES) {
            File langFile = new File(plugin.getDataFolder(), "lang/" + lang + ".yml");
            if (!langFile.exists()) {
                plugin.saveResource("lang/" + lang + ".yml", false);
            }

            // Update the language file
            YamlConfiguration resourceLang = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(Objects.requireNonNull(plugin.getResource("lang/" + lang + ".yml"))));
            YamlConfiguration targetLang = YamlConfiguration.loadConfiguration(langFile);

            for (String key : targetLang.getKeys(true)) {
                if (resourceLang.contains(key)) {
                    resourceLang.set(key, targetLang.get(key));
                }
            }

            try {
                resourceLang.save(langFile);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to update language file: " + lang + ".yml");
            }
        }

        // Load the language file
        langFile = new File(plugin.getDataFolder(), "lang/" + language + ".yml");
        if (!langFile.exists()) {
            this.language = DEFAULT_LANGUAGE;
            langFile = new File(plugin.getDataFolder(), "lang/" + language + ".yml");
        } else {
            this.language = language;
        }
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public void reload() {
        lang = YamlConfiguration.loadConfiguration(langFile);
    }
}
