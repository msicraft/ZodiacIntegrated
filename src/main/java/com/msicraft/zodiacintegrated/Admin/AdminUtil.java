package com.msicraft.zodiacintegrated.Admin;

import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Set;

public class AdminUtil {

    public HashMap<ItemStack, Integer> getShopDataHashMap() {
        HashMap<ItemStack, Integer> map = new HashMap<>();
        ConfigurationSection section = ZodiacIntegrated.shopData.getConfig().getConfigurationSection("Item");
        if (section != null) {
            Set<String> list2 = section.getKeys(false);
            for (String countS : list2) {
                ItemStack itemStack = ZodiacIntegrated.shopData.getConfig().getItemStack("Item." + countS + ".ItemStack");
                int getValue = ZodiacIntegrated.shopData.getConfig().getInt("Item." + countS + ".Value");
                map.put(itemStack, getValue);
            }
        }
        return map;
    }

    public void updateShopData() {
        HashMap<ItemStack, Integer> map = getShopDataHashMap();
        int size = map.size();
        ZodiacIntegrated.shopData.getConfig().set("LastCount", size);
        ZodiacIntegrated.shopData.getConfig().set("Item", null);
        ZodiacIntegrated.shopData.saveConfig();
        int count = 0;
        for (ItemStack itemStack : map.keySet()) {
            int value = map.get(itemStack);
            ZodiacIntegrated.shopData.getConfig().set("Item." + count + ".ItemStack", itemStack);
            ZodiacIntegrated.shopData.getConfig().set("Item." + count + ".Value", value);
            count++;
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

    public int getItemDataNumber(ItemStack itemStack) {
        int number = 0;
        HashMap<ItemStack, Integer> map = getShopDataHashMap();
        ItemStack tempStack = new ItemStack(itemStack);
        tempStack.setAmount(1);
        int count = 0;
        for (ItemStack mapItemStack : map.keySet()) {
            ItemStack tempMapStack = new ItemStack(mapItemStack);
            tempMapStack.setAmount(1);
            if (tempMapStack.equals(tempStack)) {
                number = count;
                break;
            } else {
                count++;
            }
        }
        return number;
    }

    public boolean hasSameItemStack(ItemStack itemStack) {
        boolean check = false;
        HashMap<ItemStack, Integer> map = getShopDataHashMap();
        for (ItemStack item : map.keySet()) {
            ItemStack tempItemStack = new ItemStack(item);
            ItemStack tempItemStack2 = new ItemStack(itemStack);
            tempItemStack.setAmount(1);
            tempItemStack2.setAmount(1);
            if (tempItemStack.equals(tempItemStack2)) {
                check = true;
                break;
            }
        }
        return check;
    }

}
