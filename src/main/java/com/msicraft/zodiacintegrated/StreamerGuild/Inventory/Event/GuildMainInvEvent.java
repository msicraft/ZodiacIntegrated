package com.msicraft.zodiacintegrated.StreamerGuild.Inventory.Event;

import com.msicraft.zodiacintegrated.StreamerGuild.Event.PrefixChatEditEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.GuildMainInv;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class GuildMainInvEvent implements Listener {

    private GuildUtil guildUtil = new GuildUtil();

    public static HashMap<UUID, String> memberPageCount = new HashMap<>(); //"page:<count>"

    @EventHandler
    public void onClickGuildMainMenu(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Guild")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) { return; }
            String guildId = guildUtil.getContainGuildID(player);
            String guildName = guildUtil.getGuildName(guildId);
            ItemStack guildMainMenuItem = e.getInventory().getItem(4);
            GuildMainInv guildMainInv = new GuildMainInv(player);
            if (guildMainMenuItem != null) {
                ItemMeta guildMainMenuItemMeta = guildMainMenuItem.getItemMeta();
                PersistentDataContainer guildMainMenuItemData = guildMainMenuItemMeta.getPersistentDataContainer();
                if (guildMainMenuItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMainMenu"), PersistentDataType.STRING)) {
                    String guildMainMenuItemName = guildMainMenuItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMainMenu"), PersistentDataType.STRING);
                    if (guildMainMenuItemName != null && guildMainMenuItemName.equals(guildName)) {
                        ItemStack itemStack = e.getCurrentItem();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                        if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMainMenu"), PersistentDataType.STRING)) {
                            String var = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMainMenu"), PersistentDataType.STRING);
                            if (var != null && e.isLeftClick()) {
                                switch (var) {
                                    case "ZD-Guild-MemberList" -> {
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.setGuildMemberList(player);
                                    }
                                    case "ZD-Guild-Management" -> {
                                        if (guildUtil.hasGuildPermission(player.getUniqueId(), guildId)) {
                                            player.openInventory(guildMainInv.getInventory());
                                            guildMainInv.setGuildManagement(player);
                                        } else {
                                            player.sendMessage(ChatColor.RED + "길드 관리 권한이 없습니다");
                                            player.closeInventory();
                                        }
                                    }
                                }
                            }
                        } else if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMemberList"), PersistentDataType.STRING)) {
                            String var = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMemberList"), PersistentDataType.STRING);
                            if (var != null && e.isLeftClick()) {
                                String maxPageObject = GuildMainInv.maxPage.get(player.getUniqueId());
                                int maxPageCount = 0;
                                if (maxPageObject != null) {
                                    String[] a = maxPageObject.split(":");
                                    maxPageCount = Integer.parseInt(a[1]);
                                }
                                memberPageCount.putIfAbsent(player.getUniqueId(), "page:0");
                                switch (var) {
                                    case "ZD-GuildMemberList-Back" -> {
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.openMainMenu(player);
                                    }
                                    case "ZD-GuildMemberList-Next" -> {
                                        String currentPageObject = memberPageCount.get(player.getUniqueId());
                                        int currentPage = 0;
                                        if (currentPageObject != null) {
                                            String[] a = currentPageObject.split(":");
                                            currentPage = Integer.parseInt(a[1]);
                                        }
                                        int nextPage = currentPage + 1;
                                        if (nextPage > maxPageCount) {
                                            nextPage = 0;
                                        }
                                        String value = "page:" + nextPage;
                                        memberPageCount.put(player.getUniqueId(), value);
                                        GuildMainInv.page.put(player.getUniqueId(), value);
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.setGuildMemberList(player);
                                    }
                                    case "ZD-GuildMemberList-Previous" -> {
                                        String currentPageObject = memberPageCount.get(player.getUniqueId());
                                        int currentPage = 0;
                                        if (currentPageObject != null) {
                                            String[] a = currentPageObject.split(":");
                                            currentPage = Integer.parseInt(a[1]);
                                        }
                                        int nextPage = currentPage - 1;
                                        if (nextPage < 0) {
                                            nextPage = maxPageCount;
                                        }
                                        String value = "page:" + nextPage;
                                        memberPageCount.put(player.getUniqueId(), value);
                                        GuildMainInv.page.put(player.getUniqueId(), value);
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.setGuildMemberList(player);
                                    }
                                }
                            }
                        } else if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildManagement"), PersistentDataType.STRING)) {
                            String var = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildManagement"), PersistentDataType.STRING);
                            if (var != null && e.isLeftClick()) {
                                switch (var) {
                                    case "Back" -> {
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.openMainMenu(player);
                                    }
                                    case "Change_Name" -> {
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.setGuildNameChangeMenu(player);
                                    }
                                }
                            }
                        } else if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildPrefixEdit"), PersistentDataType.STRING)) {
                            String var = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildPrefixEdit"), PersistentDataType.STRING);
                            if (var != null && e.isLeftClick()) {
                                switch (var) {
                                    case "Back" -> {
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.setGuildManagement(player);
                                    }
                                    case "Change_Prefix" -> {
                                        player.closeInventory();
                                        PrefixChatEditEvent.isPrefixChatEdit.put(player.getUniqueId(), true);
                                        PrefixChatEditEvent.prefixEditVar.put(player.getUniqueId(), var);
                                        player.sendMessage(ChatColor.YELLOW + "====================");
                                        player.sendMessage(ChatColor.GRAY + " 길드 칭호 이름을 입력해주세요 ");
                                        player.sendMessage(ChatColor.GRAY + " 'cancel' 입력시 취소 ");
                                        player.sendMessage(ChatColor.YELLOW + "====================");
                                    }
                                    case "Change_Color" -> {
                                        player.closeInventory();
                                        player.sendMessage(ChatColor.YELLOW + "====================");
                                        player.sendMessage(ChatColor.GRAY + " 길드 칭호 색상을 입력해주세요 ");
                                        player.sendMessage(ChatColor.GRAY + " 사용 가능한 색: " + PrefixChatEditEvent.getAvailableColorName());
                                        player.sendMessage(ChatColor.GRAY + " 'cancel' 입력시 취소 ");
                                        player.sendMessage(ChatColor.YELLOW + "====================");
                                        PrefixChatEditEvent.isPrefixChatColorEdit.put(player.getUniqueId(), true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
