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

public class ShopInvClickEvent implements Listener {

    private ShopUtil shopUtil = new ShopUtil();

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
                    }
                    return;
                }
            }
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
