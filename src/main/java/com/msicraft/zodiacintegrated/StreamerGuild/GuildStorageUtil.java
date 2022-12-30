package com.msicraft.zodiacintegrated.StreamerGuild;

import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GuildStorageUtil {

    private final GuildUtil guildUtil = new GuildUtil();
    private final int maxSlotSize = 45;

    private final ItemStack airItemStack = new ItemStack(Material.AIR, 1);

    public void removeStorageFileData(String guildId) {
        if (ZodiacIntegrated.guildStorageData.getConfig().contains("Guild." + guildId)) {
            ZodiacIntegrated.guildStorageData.getConfig().set("Guild." + guildId, null);
            ZodiacIntegrated.guildStorageData.saveConfig();
        }
    }

    public void storageFileDataCheck(String guildId) {
        for (int a = 0; a<maxSlotSize; a++) {
            if (!ZodiacIntegrated.guildStorageData.getConfig().contains("Guild." + guildId + "." + a)) {
                ZodiacIntegrated.guildStorageData.getConfig().set("Guild." + guildId + "." + a, airItemStack);
            }
        }
        ZodiacIntegrated.guildStorageData.saveConfig();
    }

    public void loadYamlToStorageMap(String guildId) {
        HashMap<Integer, ItemStack> itemMap = new HashMap<>(45);
        for (int a = 0; a<maxSlotSize; a++) {
            ItemStack itemStack = ZodiacIntegrated.guildStorageData.getConfig().getItemStack("Guild." + guildId + "." + a);
            if (itemStack != null) {
                if (itemStack.getType() == Material.AIR) {
                    itemMap.put(a, airItemStack);
                } else {
                    itemMap.put(a, itemStack);
                }
            } else {
                storageFileDataCheck(guildId);
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "길드 창고 로드중 에러발생: " + ChatColor.WHITE + guildId);
            }
        }
        ZodiacIntegrated.guildStorage.put(guildId, itemMap);
    }

    public void saveStorageMapToYaml(String guildId) {
        HashMap<Integer, ItemStack> itemMap;
        if (ZodiacIntegrated.guildStorage.containsKey(guildId)) {
            itemMap = ZodiacIntegrated.guildStorage.get(guildId);
            for (Integer key : itemMap.keySet()) {
                ItemStack itemStack = itemMap.get(key);
                ZodiacIntegrated.guildStorageData.getConfig().set("Guild." + guildId + "." + key, itemStack);
            }
            ZodiacIntegrated.guildStorageData.saveConfig();
        }
    }

    public HashMap<Integer, ItemStack> getYamlDataToHashMap(String guildId) {
        HashMap<Integer, ItemStack> itemMap = new HashMap<>(45);
        for (int a = 0; a<maxSlotSize; a++) {
            ItemStack itemStack = ZodiacIntegrated.guildStorageData.getConfig().getItemStack("Guild." + guildId + "." + a);
            if (itemStack != null) {
                if (itemStack.getType() == Material.AIR) {
                    itemMap.put(a, airItemStack);
                } else {
                    itemMap.put(a, itemStack);
                }
            }
        }
        return itemMap;
    }

    public void loadYamlGuildStorage(String guildId) {
        if (!ZodiacIntegrated.guildStorage.containsKey(guildId)) {
            HashMap<Integer, ItemStack> itemMap = getYamlDataToHashMap(guildId);
            ZodiacIntegrated.guildStorage.put(guildId, itemMap);
        }
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

    public boolean isExistItemStack(String guildId, int storageSlot, ItemStack clickItem) {
        boolean check = false;
        HashMap<Integer, ItemStack> getStorage = ZodiacIntegrated.guildStorage.get(guildId);
        ItemStack storageItem = getStorage.get(storageSlot);
        if (storageItem.equals(clickItem)) {
            check = true;
        }
        return check;
    }

    public void sendPlayerToStorage(String guildId, ItemStack itemStack, Player player, int playerSlot) {
        HashMap<Integer, ItemStack> getStorage = ZodiacIntegrated.guildStorage.get(guildId);
        int count = 0;
        for (int a = 0; a<maxSlotSize; a++) {
            ItemStack storageItem = getStorage.get(a);
            if (storageItem.getType() == Material.AIR) {
                getStorage.put(a, itemStack);
                break;
            } else {
                count++;
            }
        }
        if (count >= maxSlotSize) {
            player.sendMessage(ChatColor.RED + "길드 창고에 빈 공간이 없습니다.");
        } else {
            player.getInventory().setItem(playerSlot, airItemStack);
        }
    }

    public void sendStorageToPlayer(String guildId, Player player, int storageSlot) {
        HashMap<Integer, ItemStack> getStorage = ZodiacIntegrated.guildStorage.get(guildId);
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

}
