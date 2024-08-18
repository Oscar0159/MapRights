package com.archerwn.mapRights.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.inventory.ItemStack;

public class MapCopyListener_v1_21 extends MapCopyListener {

    @EventHandler
    public void onMapCopy(CrafterCraftEvent event) {
        // For 1.21 crafter

        ItemStack itemStack = event.getResult();

        if (mapManager.isSignedMap(itemStack)) {
            event.setCancelled(true);
        }
    }
}
