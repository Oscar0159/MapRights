package com.archerwn.mapRights.manager;

import com.archerwn.mapRights.MapRights;
import com.google.common.base.Splitter;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MapManager {
    public static final NamespacedKey SIGN_KEY = new NamespacedKey(MapRights.getInstance(), "sign");
    public static final NamespacedKey LORE_KEY = new NamespacedKey(MapRights.getInstance(), "lore");
    public static final NamespacedKey INFO_KEY = new NamespacedKey(MapRights.getInstance(), "info");

    @Getter
    private static final MapManager instance = new MapManager();

    private MapManager() {
    }

    public boolean isFilledMap(@NotNull ItemStack itemStack) {
        return itemStack.getType() == Material.FILLED_MAP;
    }

    public boolean isSignedMap(@NotNull ItemStack itemStack) {
        if (!isFilledMap(itemStack) || !itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return container.has(SIGN_KEY, PersistentDataType.STRING);
    }

    public UUID getSignUUID(@NotNull ItemStack itemStack) {
        if (!isFilledMap(itemStack) || !itemStack.hasItemMeta()) {
            return UUID.fromString("");
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return UUID.fromString(Objects.requireNonNullElse(container.get(SIGN_KEY, PersistentDataType.STRING), ""));
    }

    public String getAuthor(@NotNull ItemStack itemStack) {
        if (!isFilledMap(itemStack) || !itemStack.hasItemMeta()) {
            return "";
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        MapRights.getInstance().getLogger().info("ItemMeta: " + itemMeta);
        UUID uuid = getSignUUID(itemStack);
        MapRights.getInstance().getLogger().info("UUID: " + uuid);
        Player player = MapRights.getInstance().getServer().getPlayer(uuid);
        OfflinePlayer offlinePlayer = MapRights.getInstance().getServer().getOfflinePlayer(uuid);
        String name = player != null ? player.getName() : offlinePlayer.getName();
        return name != null ? name : "Unknown";
    }

    public boolean signMap(@NotNull Player player, @NotNull ItemStack itemStack) {
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

        // Add info key
        ZonedDateTime signTime = ZonedDateTime.now();
        MapMeta mapMeta = (MapMeta) itemMeta;
        MapView mapView = mapMeta.getMapView();
        assert mapView != null;
        Location mapLocation = new Location(mapView.getWorld(), mapView.getCenterX(), 0.0, mapView.getCenterZ());
        Location signLocation = player.getLocation();
        Map<String, String> info = Map.of(
                "signTime", signTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z")),
                "mapWorld", mapLocation.getWorld() != null ? mapLocation.getWorld().getName() : "",
                "mapLocation", String.format("%.3f / %.5f / %.3f", mapLocation.getX(), mapLocation.getY(), mapLocation.getZ()),
                "signWorld", Objects.requireNonNullElse(signLocation.getWorld(), player.getWorld()).getName(),
                "signLocation", String.format("%.3f / %.5f / %.3f", signLocation.getX(), signLocation.getY(), signLocation.getZ()));
        container.set(INFO_KEY, PersistentDataType.STRING, info.toString());

        itemStack.setItemMeta(itemMeta);

        return true;
    }

    public boolean unSignMap(@NotNull ItemStack itemStack) {
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

        // Remove info key
        container.remove(INFO_KEY);

        itemStack.setItemMeta(itemMeta);

        return true;
    }

    public Map<String, String> getInfo(@NotNull ItemStack itemStack) {
        if (!isFilledMap(itemStack) || !itemStack.hasItemMeta()) {
            return Map.of();
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(INFO_KEY, PersistentDataType.STRING)) {
            return Map.of();
        }

        String infoString = container.get(INFO_KEY, PersistentDataType.STRING);

        return Splitter.on(", ").withKeyValueSeparator("=").split(Objects.requireNonNullElse(infoString, "").replace(
                "{", "").replace("}", ""));
    }
}
