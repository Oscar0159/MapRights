package com.archerwn.mapRights.commands;

import com.archerwn.mapRights.MapRights;
import com.archerwn.mapRights.manager.ConfigManager;
import com.archerwn.mapRights.manager.EconomyManager;
import com.archerwn.mapRights.manager.LangManager;
import com.archerwn.mapRights.manager.MapManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MapRightsCommand implements CommandExecutor {

    private final MapRights plugin = MapRights.getInstance();

    private final ConfigManager configManager = ConfigManager.getInstance();

    private final LangManager langManager = LangManager.getInstance();

    private final MapManager mapManager = MapManager.getInstance();

    private final EconomyManager economyManager = EconomyManager.getInstance();

    private final Economy economy = MapRights.getEconomy();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(langManager.get("message.failed.must-be-player"));
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "sign":
                if (!player.hasPermission("maprights.sign")) {
                    player.sendMessage(langManager.get("message.failed.no-permission"));
                    return true;
                }
                onSignCommand(player);
                break;
            case "unsign":
                if (!player.hasPermission("maprights.unsign")) {
                    player.sendMessage(langManager.get("message.failed.no-permission"));
                    return true;
                }
                onUnSignCommand(player);
                break;
            case "forcesign":
                if (!player.hasPermission("maprights.forcesign")) {
                    player.sendMessage(langManager.get("message.failed.no-permission"));
                    return true;
                }
                onForceSignCommand(player);
                break;
            case "forceunsign":
                if (!player.hasPermission("maprights.forceunsign")) {
                    player.sendMessage(langManager.get("message.failed.no-permission"));
                    return true;
                }
                onForceUnSignCommand(player);
                break;
            default:
                return false;
        }

        return true;
    }

    private void onSignCommand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!mapManager.isFilledMap(itemStack)) {
            player.sendMessage(langManager.get("message.failed.must-hold-filled-map"));
            return;
        }

        if (mapManager.isSignedMap(itemStack)) {
            player.sendMessage(langManager.get("message.failed.map-already-signed"));
            return;
        }

        double signCost = configManager.getConfig().getDouble("economy.sign-cost");
        if (economyManager.isEconomyEnabled() && !economyManager.hasBalance(player, signCost)) {
            player.sendMessage(langManager.get("message.failed.not-enough-money").replace("{cost}",
                    String.valueOf(signCost)));
            return;
        }

        boolean success = mapManager.signMap(player, itemStack);
        if (success) {
            if (economyManager.isEconomyEnabled()) {
                economyManager.withdraw(player, signCost);
                player.sendMessage(langManager.get("message.success.map-sign-with-cost").replace("{cost}",
                        String.valueOf(signCost)));
            } else {
                player.sendMessage(langManager.get("message.success.map-sign"));
            }
        } else {
            player.sendMessage(langManager.get("message.failed.map-null-or-no-meta"));
        }
    }

    private void onUnSignCommand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!mapManager.isFilledMap(itemStack)) {
            player.sendMessage(langManager.get("message.failed.must-hold-filled-map"));
            return;
        }

        if (!mapManager.isSignedMap(itemStack)) {
            player.sendMessage(langManager.get("message.failed.map-not-signed"));
            return;
        }

        // If the map is signed by someone else, return
        UUID signUUID = mapManager.getSignUUID(itemStack);
        UUID playerUUID = player.getUniqueId();
        if (!signUUID.equals(playerUUID)) {
            Player signPlayer = plugin.getServer().getPlayer(signUUID);
            assert signPlayer != null;
            player.sendMessage(langManager.get("message.failed.map-unsign-denied"));
            return;
        }

        double unSignCost = configManager.getConfig().getDouble("economy.unsign-cost");
        if (economyManager.isEconomyEnabled() && !economyManager.hasBalance(player, unSignCost)) {
            player.sendMessage(langManager.get("message.failed.not-enough-money").replace("{cost}",
                    String.valueOf(unSignCost)));
            return;
        }

        boolean success = mapManager.unSignMap(itemStack);

        if (success) {
            if (economyManager.isEconomyEnabled()) {
                economyManager.withdraw(player, unSignCost);
                player.sendMessage(langManager.get("message.success.map-unsign-with-cost").replace("{cost}",
                        String.valueOf(unSignCost)));
            } else {
                player.sendMessage(langManager.get("message.success.map-unsign"));
            }
        } else {
            player.sendMessage(langManager.get("message.failed.map-null-or-no-meta"));
        }
    }

    private void onForceSignCommand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!mapManager.isFilledMap(itemStack)) {
            player.sendMessage(langManager.get("message.failed.must-hold-filled-map"));
            return;
        }

        boolean success = mapManager.signMap(player, itemStack);
        if (success) {
            player.sendMessage(langManager.get("message.success.map-sign"));
        } else {
            player.sendMessage(langManager.get("message.failed.map-null-or-no-meta"));
        }
    }

    private void onForceUnSignCommand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!mapManager.isFilledMap(itemStack)) {
            player.sendMessage(langManager.get("message.failed.must-hold-filled-map"));
            return;
        }

        boolean success = mapManager.unSignMap(itemStack);

        if (success) {
            player.sendMessage(langManager.get("message.success.map-unsign"));
        } else {
            player.sendMessage(langManager.get("message.failed.map-null-or-no-meta"));
        }
    }
}
