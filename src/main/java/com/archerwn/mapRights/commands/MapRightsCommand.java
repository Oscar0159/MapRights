package com.archerwn.mapRights.commands;

import com.archerwn.mapRights.MapRights;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class MapRightsCommand implements CommandExecutor {

    private final MapRights plugin = MapRights.getInstance();

    private final FileConfiguration langConfig = plugin.getLangManager().getLangConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(langConfig.getString("msg-must-be-player"));
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        // Handle the command
        switch (args[0].toLowerCase()) {
            case "sign":
                onSignCommand(player);
                break;
            case "unsign":
                onUnSignCommand(player);
                break;
            default:
                return false;
        }

        return true;
    }

    private void onSignCommand(Player player) {
        // If the player is not holding a filled map, return
        if (player.getInventory().getItemInMainHand().getType() != Material.FILLED_MAP) {
            player.sendMessage(langConfig.getString("msg-must-hold-filled-map"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        // If map didn't have ItemMeta, return
        if (!item.hasItemMeta()) {
            player.sendMessage(langConfig.getString("msg-map-does-not-have-metadata"));
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // If the map is already signed, return
        if (container.has(plugin.getSignKey())) {
            UUID uuid = UUID.fromString(container.get(plugin.getSignKey(), PersistentDataType.STRING));
            if (uuid.equals(player.getUniqueId())) {
                player.sendMessage(langConfig.getString("msg-map-signed-by-you"));
                return;
            }
            Player other = plugin.getServer().getPlayer(uuid);
            player.sendMessage(langConfig.getString("msg-map-signed-by-other").replace("{author}", other.getName()));
            return;
        }

        // Sign the map
        UUID uuid = player.getUniqueId();
        container.set(plugin.getSignKey(), PersistentDataType.STRING, uuid.toString());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(langConfig.getString("lore-signed-by").replace("{author}", player.getName()));
        itemMeta.setLore(lore);

        // Set the ItemMeta back to the ItemStack
        item.setItemMeta(itemMeta);

        player.sendMessage(langConfig.getString("msg-map-signed"));
    }

    private void onUnSignCommand(Player player) {
        // If the player is not holding a filled map, return
        if (player.getInventory().getItemInMainHand().getType() != Material.FILLED_MAP) {
            player.sendMessage(langConfig.getString("msg-must-hold-filled-map"));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        // If map didn't have ItemMeta, return
        if (!item.hasItemMeta()) {
            player.sendMessage(langConfig.getString("msg-map-does-not-have-metadata"));
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // If the map is not signed, return
        if (!container.has(plugin.getSignKey())) {
            player.sendMessage(langConfig.getString("msg-map-not-signed"));
            return;
        }

        // If the map is signed by someone else, return
        UUID uuid = UUID.fromString(container.get(plugin.getSignKey(), PersistentDataType.STRING));
        if (!uuid.equals(player.getUniqueId())) {
            Player other = plugin.getServer().getPlayer(uuid);
            player.sendMessage(langConfig.getString("msg-map-signed-by-other").replace("{author}", other.getName()));
            return;
        }

        // Unsign the map
        container.remove(plugin.getSignKey());
        itemMeta.setLore(new ArrayList<>());

        // Set the ItemMeta back to the ItemStack
        item.setItemMeta(itemMeta);

        player.sendMessage(langConfig.getString("msg-map-unsign"));
    }
}
