package com.archerwn.mapRights.manager;

import com.archerwn.mapRights.MapRights;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MapManager {
    public static final NamespacedKey SIGN_KEY = new NamespacedKey(MapRights.getInstance(), "sign");
    public static final NamespacedKey LORE_KEY = new NamespacedKey(MapRights.getInstance(), "lore");

    @Getter
    private static final MapManager instance = new MapManager();

    private MapManager() {
    }

    public boolean isFilledMap(@NonNull ItemStack itemStack) {
        return itemStack.getType() == Material.FILLED_MAP;
    }

    public boolean isSignedMap(@NonNull ItemStack itemStack) {
        if (!isFilledMap(itemStack) || !itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return container.has(SIGN_KEY, PersistentDataType.STRING);
    }

    public UUID getSignUUID(@NonNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return UUID.fromString(Objects.requireNonNullElse(container.get(SIGN_KEY, PersistentDataType.STRING), ""));
    }

    public boolean signMap(@NonNull Player player, @NonNull ItemStack itemStack) {
        if (!isFilledMap(itemStack) || !itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // Set sign key
        UUID uuid = player.getUniqueId();
        container.set(SIGN_KEY, PersistentDataType.STRING, uuid.toString());

        // Get current lore
        ArrayList<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            lore.addAll(itemMeta.getLore());
        }

        // Remove old sign lore
        if (container.has(LORE_KEY, PersistentDataType.STRING)) {
            String oldSign = container.get(LORE_KEY, PersistentDataType.STRING);
            lore.remove(oldSign);
        }

        // Add new sign lore
        String newLore = LangManager.getInstance().get("lore.signed-by").replace("{author}", player.getName());
        lore.add(newLore);
        container.set(LORE_KEY, PersistentDataType.STRING, newLore);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return true;
    }

    public boolean unSignMap(@NonNull ItemStack itemStack) {
        if (!isFilledMap(itemStack) || !itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // Remove sign key
        container.remove(SIGN_KEY);

        // Get current lore
        ArrayList<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            lore.addAll(itemMeta.getLore());
        }

        // Remove old sign lore
        if (container.has(LORE_KEY, PersistentDataType.STRING)) {
            String oldSign = container.get(LORE_KEY, PersistentDataType.STRING);
            lore.remove(oldSign);
            container.remove(LORE_KEY);
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return true;
    }
}
