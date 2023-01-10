package com.msicraft.zodiacintegrated.Shop;

import com.msicraft.zodiacintegrated.Admin.AdminUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ShopUtil {

    private final ItemStack airItemStack = new ItemStack(Material.AIR, 1);
    private final AdminUtil adminUtil = new AdminUtil();

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

    public int getItemValue(ItemStack itemStack, int amount) {
        int itemValue = 0;
        if (adminUtil.hasSameItemStack(itemStack)) {
            int dataNum = adminUtil.getItemDataNumber(itemStack);
            ItemStack getDataItem = ZodiacIntegrated.shopData.getConfig().getItemStack("Item." + dataNum + ".ItemStack");
            if (getDataItem != null) {
                int getStackAmount = getDataItem.getAmount();
                double value = ZodiacIntegrated.shopData.getConfig().getDouble("Item." + dataNum + ".Value");
                double perStackValue = value/getStackAmount;
                double resultValue = perStackValue * amount;
                if (resultValue < 1) {
                    itemValue = 0;
                } else {
                    itemValue = (int) Math.floor(resultValue);
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("null" + " | " + dataNum);
            }
        }
        return itemValue;
    }

    public int getItemTotalValue(Player player) {
        int total = 0;
        if (isExistData(player)) {
            HashMap<Integer, ItemStack> shopMap = ZodiacIntegrated.shopStorageData.get(player.getUniqueId());
            for (Integer slot : shopMap.keySet()) {
                ItemStack getMapItem = shopMap.get(slot);
                if (getMapItem.getType() != Material.AIR) {
                    int value = getItemValue(getMapItem, getMapItem.getAmount());
                    total += value;
                }
            }
        }
        return total;
    }

    public void replacePlayerShopData(Player player) {
        if (isExistData(player)) {
            HashMap<Integer, ItemStack> shopMap = ZodiacIntegrated.shopStorageData.get(player.getUniqueId());
            shopMap.replaceAll((s, v) -> airItemStack);
        }
    }

}
