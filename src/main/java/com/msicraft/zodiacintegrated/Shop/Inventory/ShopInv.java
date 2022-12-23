package com.msicraft.zodiacintegrated.Shop.Inventory;

import com.msicraft.zodiacintegrated.Shop.ShopUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ShopInv implements InventoryHolder {

    private Inventory shopInv;
    private ShopUtil shopUtil = new ShopUtil();
    private List<Component> basicList = new ArrayList<>();
    private ItemStack itemStack;

    public ShopInv(Player player) {
        shopInv = Bukkit.createInventory(player, 54, Component.text("Shop"));
    }

    public void setMainInv(Player player) {
        setMainIcon(player);
        if (!basicList.isEmpty()) {
            basicList.clear();
        }
        itemStack = createNormalItem(Material.HOPPER, "아이템 판매", basicList, "ShopMain", "SELL");
        addItemData(itemStack, "Shop-FixItem", "FIX");
        shopInv.setItem(19, itemStack);
    }

    private void setMainIcon(Player player) {
        Economy economy = ZodiacIntegrated.getEconomy();
        if (!basicList.isEmpty()) {
            basicList.clear();
        }
        basicList.add(Component.text(ChatColor.GREEN + "플레이어 이름: " + ChatColor.WHITE + player.getName()));
        basicList.add(Component.text(ChatColor.GREEN + "보유 돈: " + ChatColor.WHITE + economy.getBalance(player)));
        itemStack = createNormalItem(Material.BOOK, "정보", basicList, "ShopMain", player.getUniqueId().toString());
        addItemData(itemStack, "Shop-FixItem", "FIX");
        shopInv.setItem(4, itemStack);
    }

    private ItemStack createNormalItem(Material material, String name, List<Component> list, String dataTag, String data) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(name));
        itemMeta.lore(list);
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), dataTag), PersistentDataType.STRING, data);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void addItemData(ItemStack itemStack, String dataTag, String data) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
        itemData.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), dataTag), PersistentDataType.STRING, data);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public Inventory getInventory() {
        return shopInv;
    }

}
