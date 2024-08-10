package com.archerwn.mapRights.hooks;

import com.archerwn.mapRights.manager.ConfigManager;
import com.archerwn.mapRights.manager.MapManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIMapRightsHook extends PlaceholderExpansion {

    private JavaPlugin plugin;

    private final ConfigManager configManager = ConfigManager.getInstance();

    private final MapManager mapManager = MapManager.getInstance();

    public PAPIMapRightsHook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return plugin.getName().toLowerCase();
    }

    @Override
    @NotNull
    public String getAuthor() {
        return plugin.getDescription().getAuthors().getFirst();
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return (plugin.getServer().getPluginManager().getPlugin("MapRights") != null);
    }

    @Override
    @Nullable
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("hold_map_author")) {
            if (player == null) {
                return null;
            }

            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (!mapManager.isFilledMap(itemStack)) {
                return null;
            }

            return mapManager.getAuthor(itemStack);
        }

        if (params.equalsIgnoreCase("sign_cost")) {
            return configManager.getConfig().getDouble("economy.sign-cost") + "";
        }

        if (params.equalsIgnoreCase("unsign_cost")) {
            return configManager.getConfig().getDouble("economy.unsign-cost") + "";
        }

        return null;
    }
}
