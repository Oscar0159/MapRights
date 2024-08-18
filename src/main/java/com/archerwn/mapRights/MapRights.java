package com.archerwn.mapRights;

import com.archerwn.mapRights.commands.MapRightsCommand;
import com.archerwn.mapRights.commands.MapRightsCommandCompleter;
import com.archerwn.mapRights.hooks.PAPIMapRightsHook;
import com.archerwn.mapRights.listeners.MapCopyListener;
import com.archerwn.mapRights.listeners.MapCopyListener_v1_21;
import com.archerwn.mapRights.manager.ConfigManager;
import com.archerwn.mapRights.manager.EconomyManager;
import com.archerwn.mapRights.manager.LangManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MapRights extends JavaPlugin {

    @Getter
    private static MapRights instance;

    @Getter
    private static Economy economy;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Get server version
        Pattern versionPattern = Pattern.compile("(1)\\.(\\d+)(\\.\\d+)?");
        Matcher matcher = versionPattern.matcher(getServer().getVersion());
        int[] version = new int[2];
        if (matcher.find()) {
            version[0] = Integer.parseInt(matcher.group(1));
            version[1] = Integer.parseInt(matcher.group(2));
        } else {
            getLogger().warning("Failed to get server version! Disabling MapRights...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Setup managers
        ConfigManager.getInstance().setup(this);
        LangManager.getInstance().setup(this, ConfigManager.getInstance().getConfig().getString("language"));
        EconomyManager.getInstance().setup(this, ConfigManager.getInstance().getConfig().getBoolean("economy.enable"));

        // Hook [PlaceholderAPI]
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null &&
            Objects.requireNonNull(getServer().getPluginManager().getPlugin("PlaceholderAPI")).isEnabled()) {
            boolean success = new PAPIMapRightsHook(this).register();
            if (success) {
                getLogger().info("PlaceholderAPI hook has been enabled!");
            } else {
                getLogger().warning("PlaceholderAPI hook has failed to enable!");
            }
        }

        // Register commands and listeners
        Objects.requireNonNull(getCommand("maprights")).setExecutor(new MapRightsCommand());
        Objects.requireNonNull(getCommand("maprights")).setTabCompleter(new MapRightsCommandCompleter());

        // Register listeners
        if (version[0] == 1 && version[1] >= 21) {
            getServer().getPluginManager().registerEvents(new MapCopyListener_v1_21(), this);
        } else {
            getServer().getPluginManager().registerEvents(new MapCopyListener(), this);
        }

        getLogger().info("MapRights has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MapRights has been disabled!");
    }
}
