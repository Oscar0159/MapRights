package com.archerwn.mapRights.listeners;

import com.archerwn.mapRights.MapRights;
import com.archerwn.mapRights.manager.LangManager;
import com.archerwn.mapRights.manager.MapManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MapCopyListener implements Listener {
    private final MapRights plugin = MapRights.getInstance();

    private final LangManager langManager = LangManager.getInstance();

    private final MapManager mapManager = MapManager.getInstance();

    @EventHandler
    public void onMapCopy(CrafterCraftEvent event) {
        // For 1.21 crafter

        ItemStack itemStack = event.getResult();

        if (mapManager.isSignedMap(itemStack)) {
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
                event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY &&
                event.getAction() != InventoryAction.DROP_ONE_SLOT &&
                event.getAction() != InventoryAction.DROP_ALL_SLOT &&
                event.getAction() != InventoryAction.HOTBAR_SWAP) {
            return;
        }

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }

        if (!mapManager.isSignedMap(itemStack)) {
            return;
        }

        // If map is signed by you, return
        UUID signUUID = mapManager.getSignUUID(itemStack);
        UUID playerUUID = event.getWhoClicked().getUniqueId();
        if (signUUID.equals(playerUUID)) {
            return;
        }

        event.setCancelled(true);
        event.getWhoClicked().sendMessage(langManager.get("message.failed.map-copy-denied"));
    }
}
