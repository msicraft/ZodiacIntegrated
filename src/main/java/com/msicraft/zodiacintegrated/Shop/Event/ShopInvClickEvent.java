package com.msicraft.zodiacintegrated.Shop.Event;

import com.msicraft.zodiacintegrated.Shop.Inventory.ShopInv;
import com.msicraft.zodiacintegrated.Shop.ShopUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class ShopInvClickEvent implements Listener {

    private ShopUtil shopUtil = new ShopUtil();

    public static HashMap<UUID, String> itemPricePageCount = new HashMap<>(); //"page:<count>"

    private final static int shopSellSlots[] = {9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44};
    private final Economy economy = ZodiacIntegrated.getEconomy();

    @EventHandler
    public void onShopMainInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Shop")) {
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) { return; }
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            ShopInv shopInv = new ShopInv(player);
            if (selectItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "Shop-FixItem"), PersistentDataType.STRING)) {
                String fixVar = selectItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "Shop-FixItem"), PersistentDataType.STRING);
                if (fixVar != null && fixVar.equals("FIX")) {
                    e.setCancelled(true);
                    ItemStack itemStack = e.getCurrentItem();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                    if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ShopMain"), PersistentDataType.STRING)) {
                        String var = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ShopMain"), PersistentDataType.STRING);
                        if (var != null) {
                            switch (var) {
                                case "SELL" -> {
                                    player.openInventory(shopInv.getInventory());
                                    shopInv.sellInv(player);
                                }
                                case "ITEM-PRICE" -> {
                                    player.openInventory(shopInv.getInventory());
                                    shopInv.checkItemPrice(player);
                                }
                            }
                        }
                    } else if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-Sell"), PersistentDataType.STRING)) {
                        String var = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-Sell"), PersistentDataType.STRING);
                        if (var != null) {
                            switch (var) {
                                case "Back" -> {
                                    player.openInventory(shopInv.getInventory());
                                    shopInv.setMainInv(player);
                                }
                                case "Sell" -> {
                                    if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-Sell-Price"), PersistentDataType.STRING)) {
                                        String valueS = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-Sell-Price"), PersistentDataType.STRING);
                                        if (valueS != null) {
                                            int totalValue = Integer.parseInt(valueS);
                                            shopUtil.replacePlayerShopData(player);
                                            economy.depositPlayer(player, totalValue);
                                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "+ " + totalValue);
                                            player.closeInventory();
                                        }
                                    } else {
                                        player.closeInventory();
                                    }
                                }
                            }
                        }
                    } else if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-ItemList"), PersistentDataType.STRING)) {
                        String var = data.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-ItemList"), PersistentDataType.STRING);
                        if (var != null) {
                            String maxPageObject = ShopInv.shopItemPrice_maxPage.get(player.getUniqueId());
                            int maxPageCount = 0;
                            if (maxPageObject != null) {
                                String[] a = maxPageObject.split(":");
                                maxPageCount = Integer.parseInt(a[1]);
                            }
                            itemPricePageCount.putIfAbsent(player.getUniqueId(), "page:0");
                            switch (var) {
                                case "Back" -> {
                                    player.openInventory(shopInv.getInventory());
                                    shopInv.setMainInv(player);
                                }
                                case "Next" -> {
                                    String currentPageObject = itemPricePageCount.get(player.getUniqueId());
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
                                    itemPricePageCount.put(player.getUniqueId(), value);
                                    ShopInv.shopItemPrice_page.put(player.getUniqueId(), value);
                                    player.openInventory(shopInv.getInventory());
                                    shopInv.checkItemPrice(player);
                                }
                                case "Previous" -> {
                                    String currentPageObject = itemPricePageCount.get(player.getUniqueId());
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
                                    itemPricePageCount.put(player.getUniqueId(), value);
                                    ShopInv.shopItemPrice_page.put(player.getUniqueId(), value);
                                    player.openInventory(shopInv.getInventory());
                                    shopInv.checkItemPrice(player);
                                }
                            }
                        }
                    }
                    return;
                }
            }
            e.setCancelled(true);
            ItemStack backItem = e.getInventory().getItem(45);
            if (backItem != null) {
                ItemMeta itemMeta = backItem.getItemMeta();
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                if (data.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-Sell"), PersistentDataType.STRING)) {
                    InventoryType clickInventoryType = e.getClickedInventory().getType();
                    e.setCancelled(true);
                    switch (clickInventoryType) {
                        case PLAYER -> {
                            int slot = e.getSlot();
                            shopUtil.sendPlayerToStorage(player, selectItem, slot);
                            player.openInventory(shopInv.getInventory());
                            shopInv.loadShopStorage(player);
                        }
                        case CHEST -> {
                            int slot = e.getSlot();
                            shopUtil.sendStorageToPlayer(player, slot);
                            player.openInventory(shopInv.getInventory());
                            shopInv.loadShopStorage(player);
                        }
                    }
                }
            }
        }
    }

}
