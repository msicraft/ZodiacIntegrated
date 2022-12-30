package com.msicraft.zodiacintegrated.Shop;

import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ShopUtil {

    private final ItemStack airItemStack = new ItemStack(Material.AIR, 1);

    public void registerStorageData(Player player) {
        if (!isExistData(player)) {
            HashMap<Integer, ItemStack> tempStorage = new HashMap<>(36);
            for (int a = 9; a<45; a++) {
                tempStorage.put(a, airItemStack);
            }
            ZodiacIntegrated.shopStorageData.put(player.getUniqueId(), tempStorage);
        }
    }

    public boolean isExistData(Player player) {
        return ZodiacIntegrated.shopStorageData.containsKey(player.getUniqueId());
    }

    public int getPlayerEmptySlot(Player player) {
        int slot = -1;
        int size = 36;
        for (int a = 0; a<size; a++) {
            ItemStack itemStack = player.getInventory().getItem(a);
            if (itemStack == null) {
                slot = a;
                break;
            }

        }
        return slot;
    }

    public void sendPlayerToStorage(Player player, ItemStack itemStack, int playerSlot) {
        HashMap<Integer, ItemStack> getStorage = ZodiacIntegrated.shopStorageData.get(player.getUniqueId());
        int count = 0;
        for (int a = 9; a<45; a++) {
            ItemStack storageItem = getStorage.get(a);
            if (storageItem.getType() == Material.AIR) {
                getStorage.put(a, itemStack);
                break;
            } else {
                count++;
            }
        }
        if (count >= 36) {
            player.sendMessage(ChatColor.RED + "빈 공간이 없습니다.");
        } else {
            player.getInventory().setItem(playerSlot, airItemStack);
        }
    }

    public void sendStorageToPlayer(Player player, int storageSlot) {
        HashMap<Integer, ItemStack> getStorage = ZodiacIntegrated.shopStorageData.get(player.getUniqueId());
        ItemStack itemStack = getStorage.get(storageSlot);
        if (itemStack.getType() != Material.AIR) {
            int slot = getPlayerEmptySlot(player);
            if (slot != -1) {
                player.getInventory().addItem(itemStack);
                getStorage.put(storageSlot, airItemStack);
            } else {
                player.sendMessage(ChatColor.RED + "인벤토리에 빈 공간이 없습니다.");
            }
        }
    }

    public ArrayList<ItemStack> getShopDataItemStack() {
        ArrayList<ItemStack> list = new ArrayList<>();
        ConfigurationSection section = ZodiacIntegrated.shopData.getConfig().getConfigurationSection("Item");
        if (section != null) {
            Set<String> list2 = section.getKeys(false);
            for (String countS : list2) {
                list.add(ZodiacIntegrated.shopData.getConfig().getItemStack("Item." + countS));
            }
        }
        return list;
    }

    public void updateShopData() {
        ArrayList<ItemStack> itemStacks = getShopDataItemStack();
        int size = itemStacks.size();
        ZodiacIntegrated.shopData.getConfig().set("LastCount", size);
        HashMap<Integer, ItemStack> itemMaps = new HashMap<>();
        int count = 0;
        for (ItemStack itemStack : itemStacks) {
            itemMaps.put(count, itemStack);
            count++;
        }
        ZodiacIntegrated.shopData.getConfig().set("Item", null);
        ZodiacIntegrated.shopData.saveConfig();
        for (Integer a : itemMaps.keySet()) {
            ItemStack getItem = itemMaps.get(a);
            ZodiacIntegrated.shopData.getConfig().set("Item." + a, getItem);
        }
        ZodiacIntegrated.shopData.saveConfig();
    }

    public int getDataLastCount() {
        int count = 0;
        if (ZodiacIntegrated.shopData.getConfig().contains("LastCount")) {
            count = ZodiacIntegrated.shopData.getConfig().getInt("LastCount");
        }
        return count;
    }

}
