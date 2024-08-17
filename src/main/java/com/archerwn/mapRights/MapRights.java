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
        ConfigManager.getInstance().setup(this);
        LangManager.getInstance().setup(this, ConfigManager.getInstance().getConfig().getString("language"));
        EconomyManager.getInstance().setup(this, ConfigManager.getInstance().getConfig().getBoolean("economy.enable"));

        // Hook
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null &&
            Objects.requireNonNull(getServer().getPluginManager().getPlugin("PlaceholderAPI")).isEnabled()) {
            boolean success = new PAPIMapRightsHook(this).register();
            if (success) {
                getLogger().info("PlaceholderAPI hook has been enabled!");
            } else {
                getLogger().warning("PlaceholderAPI hook has failed to enable!");
            }
        }

        Objects.requireNonNull(getCommand("maprights")).setExecutor(new MapRightsCommand());
        Objects.requireNonNull(getCommand("maprights")).setTabCompleter(new MapRightsCommandCompleter());

        if (getServer().getVersion().contains("1.21")) {
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
