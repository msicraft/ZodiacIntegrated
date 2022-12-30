package com.msicraft.zodiacintegrated.Admin.Event;

import com.msicraft.zodiacintegrated.Admin.Inventory.AdminInv;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildStorageUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;

public class AdminInvEvent implements Listener {

    public HashMap<String, Integer> page_count = new HashMap<>();

    private final GuildStorageUtil guildStorageUtil = new GuildStorageUtil();

    @EventHandler
    public void onAdminInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Admin")) {
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }
            e.setCancelled(true);
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            AdminInv adminInv = new AdminInv(player);
            if (selectItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    switch (var) {
                        case "Update-GuildStorage" -> {
                            List<String> guildIdList = ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player");
                            for (String guildId: guildIdList) {
                                guildStorageUtil.storageFileDataCheck(guildId);
                            }
                            player.sendMessage(ChatColor.GREEN + "총 " + ChatColor.WHITE + guildIdList.size() + ChatColor.GREEN + "개의 길드 창고 데이터가 획인 완료 되었습니다");
                        }
                        case "Shop-ItemManagement" -> {
                            player.openInventory(adminInv.getInventory());
                            adminInv.setShopManagementInv();
                        }
                    }
                }
            } else if (selectItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin-Shop"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin-Shop"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    switch (var) {
                        case "Shop-Item-List" -> {
                            player.openInventory(adminInv.getInventory());
                            adminInv.registerItemList();
                        }
                        case "Shop-Register-Item" -> {
                            player.sendMessage(ChatColor.YELLOW + "====================");
                            player.sendMessage(ChatColor.GRAY + " 아이템을 손에들고 가격을 입력해주세요.");
                            player.sendMessage(ChatColor.GRAY + " 'cancel' 입력시 취소 ");
                            player.sendMessage(ChatColor.YELLOW + "====================");
                            player.closeInventory();
                            AdminInvChatEvent.isShopRegister.put(player.getUniqueId(), true);
                        }
                    }
                }
            } else if (selectItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin-ShopManagement"), PersistentDataType.STRING)) {
                String var = selectItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Admin-ShopManagement"), PersistentDataType.STRING);
                if (var != null && e.isLeftClick()) {
                    int max = adminInv.max_page.get("max-page");
                    if (!page_count.containsKey("page")) {
                        page_count.put("page", 0);
                    }
                    switch (var) {
                        case "Next" -> {
                            int current_page = page_count.get("page");
                            int next_page = current_page + 1;
                            if (next_page > max) {
                                next_page = 0;
                            }
                            page_count.put("page", next_page);
                            adminInv.page.put("page", next_page);
                        }
                        case "Previous" -> {
                            int current_page = page_count.get("page");
                            int next_page = current_page - 1;
                            if (next_page < 0) {
                                next_page = max;
                            }
                            page_count.put("page", next_page);
                            adminInv.page.put("page", next_page);
                        }
                    }
                }
            }
        }
    }

}