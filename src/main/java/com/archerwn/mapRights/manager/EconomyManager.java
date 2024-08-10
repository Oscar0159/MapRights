package com.archerwn.mapRights.manager;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyManager {
    @Getter
    private static final EconomyManager instance = new EconomyManager();

    @Getter
    private Economy economy;

    private JavaPlugin plugin;

    private EconomyManager() {
    }

    public void setup(JavaPlugin plugin, boolean enable) {
        this.plugin = plugin;

        if (enable) {
            setupEconomy();
        }
    }

    public boolean isEconomyEnabled() {
        return economy != null;
    }

    public boolean hasBalance(Player player, double amount) {
        if (!isEconomyEnabled()) {
            return true;
        }

        return economy.has(player, amount);
    }

    public void withdraw(Player player, double amount) {
        if (!isEconomyEnabled()) {
            return;
        }

        economy.withdrawPlayer(player, amount);
    }

    public void deposit(Player player, double amount) {
        if (!isEconomyEnabled()) {
            return;
        }

        economy.depositPlayer(player, amount);
    }

    private void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault not found. Economy feature is disabled.");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        plugin.getLogger().info(plugin.getServer().getServicesManager().getKnownServices().toString());
        if (rsp == null) {
            plugin.getLogger().warning("There is no economy plugin installed. Economy feature is disabled.");
            return;
        }
        economy = rsp.getProvider();
        plugin.getLogger().info("Economy feature has been enabled!");
    }
}
