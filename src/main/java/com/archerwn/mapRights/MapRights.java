package com.archerwn.mapRights;

import com.archerwn.mapRights.commands.MapRightsCommand;
import com.archerwn.mapRights.commands.MapRightsCommandCompleter;
import com.archerwn.mapRights.config.ConfigManager;
import com.archerwn.mapRights.config.LangManager;
import com.archerwn.mapRights.listeners.MapCopyListener;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapRights extends JavaPlugin {

    @Getter
    private static MapRights instance;

    @Getter
    private final ConfigManager configManager = new ConfigManager(this);

    @Getter
    private final LangManager langManager = new LangManager(this);

    @Getter
    private final NamespacedKey signKey = new NamespacedKey(this, "sign");

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Load the config, if not exist, create one
        configManager.loadConfig();

        // Load the language file
        langManager.loadLang(configManager.getConfig().getString("language"));

        // if use economy, load Vault
        // TODO: Load Vault

        // Register the command executor and tab completer
        getCommand("maprights").setExecutor(new MapRightsCommand());
        getCommand("maprights").setTabCompleter(new MapRightsCommandCompleter());

        // Register the event listener
        getServer().getPluginManager().registerEvents(new MapCopyListener(), this);

        instance.getLogger().info("MapRights has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MapRights has been disabled!");
    }
}
