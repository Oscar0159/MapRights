package com.archerwn.mapRights.listeners;

import com.archerwn.mapRights.MapRights;
import com.archerwn.mapRights.manager.LangManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import static com.archerwn.mapRights.manager.MapManager.getSignUUID;
import static com.archerwn.mapRights.manager.MapManager.isSignedMap;

public class MapCopyListener implements Listener {
    private final MapRights plugin = MapRights.getInstance();

    private final LangManager langManager = LangManager.getInstance();

    @EventHandler
    public void onMapCopy(CrafterCraftEvent event) {
        // For 1.21 crafter

        ItemStack itemStack = event.getResult();

        if (isSignedMap(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMapCopy(InventoryClickEvent event) {
        // For any inventory

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

        ItemStack itemStack = event.getCurrentItem();

        if (!isSignedMap(itemStack)) {
            return;
        }

        // If map is signed by you, return
        UUID signUUID = getSignUUID(itemStack);
        UUID playerUUID = event.getWhoClicked().getUniqueId();
        if (signUUID.equals(playerUUID)) {
            return;
        }

        event.setCancelled(true);
        event.getWhoClicked().sendMessage(langManager.get("message.failed.map-copy-denied"));
    }
}
