package com.archerwn.mapRights.listeners;

import com.archerwn.mapRights.MapRights;
import com.archerwn.mapRights.manager.LangManager;
import com.archerwn.mapRights.manager.MapManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MapCopyListener implements Listener {
    protected final MapRights plugin = MapRights.getInstance();

    protected final LangManager langManager = LangManager.getInstance();

    protected final MapManager mapManager = MapManager.getInstance();

    @EventHandler
    public void onMapCopy(InventoryClickEvent event) {
        // For any inventory

        // If slot type is not RESULT, return
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
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
