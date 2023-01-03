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
import java.util.HashMap;
import java.util.List;

public class ShopInv implements InventoryHolder {

    private Inventory shopInv;
    private ShopUtil shopUtil = new ShopUtil();
    private List<Component> basicList = new ArrayList<>();
    private ItemStack itemStack;

    public ShopInv(Player player) {
        shopInv = Bukkit.createInventory(player, 54, Component.text("Shop"));
    }

    public void loadShopStorage(Player player) {
        setMainIcon(player);
        sellInv(player);
        HashMap<Integer, ItemStack> itemMaps = ZodiacIntegrated.shopStorageData.get(player.getUniqueId());
        List<Component> tempLoreList = new ArrayList<>();
        for (int a = 9; a<45; a++) {
            if (!tempLoreList.isEmpty()) {
                tempLoreList.clear();
            }
            ItemStack itemStack = new ItemStack(itemMaps.get(a));
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemStack.getType() != Material.AIR) {
                int getValue = shopUtil.getItemValue(itemStack, itemStack.getAmount());
                tempLoreList.add(Component.text(ChatColor.GREEN + "가격: " + ChatColor.WHITE + getValue));
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-PlayerSellShop"), PersistentDataType.STRING, String.valueOf(getValue));
                itemMeta.lore(tempLoreList);
                itemStack.setItemMeta(itemMeta);
            }
            shopInv.setItem(a, itemStack);
        }
    }

    public void sellInv(Player player) {
        setMainIcon(player);
        if (!basicList.isEmpty()) {
            basicList.clear();
        }
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", basicList, "ZD-Shop-Sell", "Back");
        addItemData(itemStack, "Shop-FixItem", "FIX");
        shopInv.setItem(45, itemStack);
        ItemStack tempItem = createNormalItem(Material.BLACK_STAINED_GLASS_PANE, "", basicList, "ZD-Shop-Sell", "null");
        addItemData(tempItem, "Shop-FixItem", "FIX");
        for (int a = 46; a<54; a++) {
            ItemStack stack = shopInv.getItem(a);
            if (stack == null) {
                shopInv.setItem(a, tempItem);
            }
        }
        List<Component> tempLoreList = new ArrayList<>();
        HashMap<Integer, ItemStack> itemMaps = ZodiacIntegrated.shopStorageData.get(player.getUniqueId());
        for (int a = 9; a<45; a++) {
            if (!tempLoreList.isEmpty()) {
                tempLoreList.clear();
            }
            ItemStack itemStack = new ItemStack(itemMaps.get(a));
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemStack.getType() != Material.AIR) {
                int getValue = shopUtil.getItemValue(itemStack, itemStack.getAmount());
                tempLoreList.add(Component.text(ChatColor.GREEN + "가격: " + ChatColor.WHITE + getValue));
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-Sell"), PersistentDataType.STRING, String.valueOf(getValue));
                itemMeta.lore(tempLoreList);
                itemStack.setItemMeta(itemMeta);
            }
            shopInv.setItem(a, itemStack);
        }
        int totalValue = shopUtil.getItemTotalValue(player);
        basicList.add(Component.text(ChatColor.GREEN + "총 가격: " + ChatColor.WHITE + totalValue));
        itemStack = createNormalItem(Material.STONE_BUTTON, "판매 확인", basicList, "ZD-Shop-Sell", "Sell");
        addItemData(itemStack, "ZD-Shop-Sell-Price", String.valueOf(totalValue));
        addItemData(itemStack, "Shop-FixItem", "FIX");
        shopInv.setItem(53, itemStack);
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
        if (!basicList.isEmpty()) {
            basicList.clear();
        }
        ItemStack tempItem = createNormalItem(Material.BLACK_STAINED_GLASS_PANE, "", basicList, "ZD-Shop-Sell", "null");
        addItemData(tempItem, "Shop-FixItem", "FIX");
        for (int a = 0; a<9; a++) {
            ItemStack stack = shopInv.getItem(a);
            if (stack == null) {
                shopInv.setItem(a, tempItem);
            }
        }
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
