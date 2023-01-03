package com.msicraft.zodiacintegrated.Admin.Inventory;

import com.msicraft.zodiacintegrated.Admin.AdminUtil;
import com.msicraft.zodiacintegrated.Shop.ShopUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminInv implements InventoryHolder {

    private Inventory adminInv;
    private List<Component> basicLoreList = new ArrayList<>();
    private ItemStack itemStack;

    private final ShopUtil shopUtil = new ShopUtil();
    private final AdminUtil adminUtil = new AdminUtil();
    public HashMap<String, Integer> page = new HashMap<>();

    public HashMap<String, Integer> max_page = new HashMap<>();

    public AdminInv(Player player) {
        adminInv = Bukkit.createInventory(player, 54, Component.text("Admin"));
        setMainInv();
    }

    public void registeredItemList() {
        //shopUtil.updateShopData();
        adminInv.clear();
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", basicLoreList, "ZD-Admin-Shop-Item-DataNumber", "Back");
        adminInv.setItem(45, itemStack);
        page_button_size();
        page_book();
        basic_button();
        HashMap<ItemStack, Integer> itemMap = new HashMap<>(adminUtil.getShopDataHashMap());
        int max_size = itemMap.size();
        int page_num = 0;
        if (page.containsKey("page")) {
            page_num = page.get("page");
        }
        int gui_count = 0;
        int gui_max = 45;
        int lastCount = page_num*45;
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        for (ItemStack itemStack : itemMap.keySet()) {
            itemStacks.add(itemStack);
            values.add(itemMap.get(itemStack));
        }
        List<Component> tempLoreList = new ArrayList<>();
        for (int a = lastCount; a<max_size; a++) {
            if (!tempLoreList.isEmpty()) {
                tempLoreList.clear();
            }
            ItemStack itemStack = new ItemStack(itemStacks.get(a));
            int value = values.get(a);
            if (!tempLoreList.isEmpty()) {
                tempLoreList.clear();
            }
            int getDataNum = adminUtil.getItemDataNumber(itemStack);
            tempLoreList.add(Component.text(ChatColor.YELLOW + "Left Click: 가격 변경"));
            tempLoreList.add(Component.text(ChatColor.YELLOW + "Right Click: 아이템 제거"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin-Shop-Item-DataNumber"), PersistentDataType.STRING, String.valueOf(getDataNum));
            tempLoreList.add(Component.text(""));
            tempLoreList.add(Component.text(ChatColor.GREEN + "현재 가격: " + ChatColor.WHITE + value));
            itemMeta.lore(tempLoreList);
            itemStack.setItemMeta(itemMeta);
            hideItemFlag(itemStack, ItemFlag.HIDE_ATTRIBUTES);
            adminInv.setItem(a, itemStack);
            gui_count++;
            if (gui_count >= gui_max) {
                break;
            }
        }
    }

    private void hideItemFlag(ItemStack itemStack, ItemFlag itemFlag) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasItemFlag(itemFlag)) {
            itemMeta.addItemFlags(itemFlag);
        }
        itemStack.setItemMeta(itemMeta);
    }

    private void page_book() {
        ItemStack itemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String get_page = String.valueOf(page.get("page"));
        if (get_page.equals("null")) {
            get_page = "0";
        }
        int page = Integer.parseInt(get_page) + 1;
        itemMeta.displayName(Component.text("Page: " + page));
        itemStack.setItemMeta(itemMeta);
        adminInv.setItem(49, itemStack);
    }

    private void basic_button() {
        ItemStack itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("Next"));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin-ShopManagement"), PersistentDataType.STRING, "Next");
        itemStack.setItemMeta(itemMeta);
        adminInv.setItem(50, itemStack);
        itemMeta.displayName(Component.text("Previous"));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin-ShopManagement"), PersistentDataType.STRING, "Previous");
        itemStack.setItemMeta(itemMeta);
        adminInv.setItem(48, itemStack);
    }

    public void page_button_size() {
        int max_size = adminUtil.getShopDataHashMap().size();
        int page_count = max_size/45;
        max_page.put("max-page", page_count);
    }

    public void setShopManagementInv() {
        itemStack = createNormalItem(Material.ENDER_CHEST, ChatColor.WHITE + "등록된 아이템 목록", basicLoreList, "ZD-Admin-Shop", "Shop-Item-List");
        adminInv.setItem(10, itemStack);
        itemStack = createNormalItem(Material.CHEST, ChatColor.WHITE + "아이템 등록", basicLoreList, "ZD-Admin-Shop", "Shop-Register-Item");
        adminInv.setItem(11, itemStack);
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", basicLoreList, "ZD-Admin-Shop", "Back");
        adminInv.setItem(45, itemStack);
    }

    private void setMainInv() {
        itemStack = createNormalItem(Material.BLACK_BANNER, ChatColor.WHITE + "모든 길드 창고 데이터 확인", basicLoreList, "ZD-Admin", "Update-GuildStorage");
        adminInv.setItem(10, itemStack);
        itemStack = createNormalItem(Material.CHEST, ChatColor.WHITE + "상점 아이템 관리", basicLoreList, "ZD-Admin", "Shop-ItemManagement");
        adminInv.setItem(11, itemStack);
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
        return adminInv;
    }

}
