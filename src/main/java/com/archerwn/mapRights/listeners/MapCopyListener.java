package com.archerwn.mapRights.listeners;

import com.archerwn.mapRights.MapRights;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MapCopyListener implements Listener {
    private static final MapRights plugin = MapRights.getInstance();

    // for 1.21 crafter
    @EventHandler
    public void onMapCopy(CrafterCraftEvent event) {
        ItemStack item = event.getResult();

        // If item is not a filled map, return
        if (item.getType() != Material.FILLED_MAP) {
            return;
        }

        // If map doesn't have ItemMeta, return
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // If map didn't sign, return
        if (!container.has(plugin.getSignKey())) {
            return;
        }

        // Cancel the signed map copy
        event.setCancelled(true);
    }

    @EventHandler
    public void onMapCopy(InventoryClickEvent event) {
        // If slot type is not RESULT, return
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }

        // If inventory action is not PICKUP, return
        if (event.getAction() != InventoryAction.PICKUP_ALL &&
            event.getAction() != InventoryAction.PICKUP_HALF &&
            event.getAction() != InventoryAction.PICKUP_ONE &&
            event.getAction() != InventoryAction.PICKUP_SOME &&
            event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        // If item is empty, return
        if (item == null) {
            return;
        }

        // If item is not a filled map, return
        if (item.getType() != Material.FILLED_MAP) {
            return;
        }

        // If map doesn't have ItemMeta, return
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // If map didn't sign, return
        if (!container.has(plugin.getSignKey())) {
            return;
        }

        // If map is signed by you, return
        UUID uuid = UUID.fromString(container.get(plugin.getSignKey(), PersistentDataType.STRING));
        if (uuid.equals(event.getWhoClicked().getUniqueId())) {
            return;
        }

        // Cancel the event
        event.setCancelled(true);

        // Send a message to the player
        event.getWhoClicked().sendMessage("You can't copy a map that is signed by someone else.");
    }
}
