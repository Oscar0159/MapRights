package com.archerwn.mapRights.manager;

import com.archerwn.mapRights.MapRights;
import lombok.Getter;
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
    @Getter
    private static final MapManager instance = new MapManager();

    private static final NamespacedKey signKey = new NamespacedKey(MapRights.getInstance(), "sign");

    private MapManager() {
    }

    public static boolean isFilledMap(ItemStack itemStack) {
        return itemStack.getType() == Material.FILLED_MAP;
    }

    public static boolean isSignedMap(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.FILLED_MAP || !itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return container.has(signKey, PersistentDataType.STRING);
    }

    public static UUID getSignUUID(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return UUID.fromString(Objects.requireNonNullElse(container.get(signKey, PersistentDataType.STRING), ""));
    }

    public static boolean signMap(Player player, ItemStack map) {
        if (map == null || !map.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = map.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        UUID uuid = player.getUniqueId();
        container.set(signKey, PersistentDataType.STRING, uuid.toString());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(LangManager.getInstance().get("lore.signed-by").replace("{author}", player.getName()));
        itemMeta.setLore(lore);

        map.setItemMeta(itemMeta);
        return true;
    }

    public static boolean unSignMap(ItemStack map) {
        if (map==null || !map.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = map.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.remove(signKey);
        itemMeta.setLore(new ArrayList<>());

        map.setItemMeta(itemMeta);
        return true;
    }
}
