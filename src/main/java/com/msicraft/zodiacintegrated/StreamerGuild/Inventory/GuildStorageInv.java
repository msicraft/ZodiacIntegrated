package com.msicraft.zodiacintegrated.StreamerGuild.Inventory;

import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import net.kyori.adventure.text.Component;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuildStorageInv implements InventoryHolder {

    private final GuildUtil guildUtil = new GuildUtil();

    private Inventory guildStorageInv;
    private final int maxSlots = 45;

    public GuildStorageInv(Player player, String guildId) {
        guildStorageInv = Bukkit.createInventory(player, 54, Component.text("Guild Storage"));
        basicSetting(player);
    }

    public void loadGuildStorage(String guildId) {
        HashMap<Integer, ItemStack> itemMaps = ZodiacIntegrated.guildStorage.get(guildId);
        for (int a = 0; a<maxSlots; a++) {
            ItemStack itemStack = itemMaps.get(a);
            guildStorageInv.setItem(a, itemStack);
        }
    }

    private void basicSetting(Player player) {
        setGuildIcon(player);
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", basicLoreList, "ZD-GuildStorage", "Back");
        addItemData(itemStack, "GuildStorage-FixItem", "FIX");
        guildStorageInv.setItem(45, itemStack);
        itemStack = createNormalItem(Material.COMPASS, ChatColor.WHITE + "창고 업데이트", basicLoreList, "ZD-GuildStorage", "Storage-Update");
        addItemData(itemStack, "GuildStorage-FixItem", "FIX");
        guildStorageInv.setItem(53, itemStack);
        ItemStack tempItem = createNormalItem(Material.BLACK_STAINED_GLASS_PANE, "", basicLoreList, "ZD-GuildStorage", "null");
        addItemData(tempItem, "GuildStorage-FixItem", "FIX");
        for (int a = 46; a<54; a++) {
            ItemStack stack = guildStorageInv.getItem(a);
            if (stack == null) {
                guildStorageInv.setItem(a, tempItem);
            }
        }
    }

    private void setGuildIcon(Player player) {
        ItemStack itemStack;
        String getContainGuildId = guildUtil.getContainGuildID(player);
        String guildName = guildUtil.getGuildName(getContainGuildId);
        double getGuildMoney = guildUtil.getGuildMoney(getContainGuildId);
        basicLoreList.add(Component.text(ChatColor.GREEN+"길드 이름: " + ChatColor.WHITE + guildName));
        basicLoreList.add(Component.text(""));
        basicLoreList.add(Component.text(ChatColor.GREEN + "길드 자금: " + ChatColor.WHITE + getGuildMoney));
        itemStack = createNormalItem(Material.BOOK, ChatColor.WHITE + "길드", basicLoreList, "ZD-GuildStorage", guildName);
        addItemData(itemStack, "GuildStorage-FixItem", "FIX");
        guildStorageInv.setItem(49, itemStack);
        if (!basicLoreList.isEmpty()) {
            basicLoreList.clear();
        }
    }

    private final List<Component> basicLoreList = new ArrayList<>();

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
    public @NotNull Inventory getInventory() {
        return guildStorageInv;
    }
}
