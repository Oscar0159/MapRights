package com.archerwn.mapRights;

import com.archerwn.mapRights.commands.MapRightsCommand;
import com.archerwn.mapRights.commands.MapRightsCommandCompleter;
import com.archerwn.mapRights.listeners.MapCopyListener;
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

        Objects.requireNonNull(getCommand("maprights")).setExecutor(new MapRightsCommand());
        Objects.requireNonNull(getCommand("maprights")).setTabCompleter(new MapRightsCommandCompleter());

        getServer().getPluginManager().registerEvents(new MapCopyListener(), this);

        getLogger().info("MapRights has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MapRights has been disabled!");
    }
}
