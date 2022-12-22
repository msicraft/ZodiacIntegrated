package com.msicraft.zodiacintegrated.Shop.Event;

import com.msicraft.zodiacintegrated.Shop.ShopUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ShopInvClickEvent implements Listener {

    private ShopUtil shopUtil = new ShopUtil();

    @EventHandler
    public void onShopMainInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Shop")) {
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) { return; }
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            if (selectItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "Shop-FixItem"), PersistentDataType.STRING)) {
                String data = selectItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "Shop-FixItem"), PersistentDataType.STRING);
                if (data != null && data.equals("FIX")) {
                    e.setCancelled(true);
                }
            }
            ItemStack mainIcon = e.getInventory().getItem(4);
            if (mainIcon != null) {
                ItemMeta mainMeta = mainIcon.getItemMeta();
                PersistentDataContainer mainData = mainMeta.getPersistentDataContainer();
                if (mainData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ShopMain"), PersistentDataType.STRING)) {
                    String mainIconId = mainData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ShopMain"), PersistentDataType.STRING);
                    if (mainIconId != null && mainIconId.equals(player.getUniqueId().toString())) {
                        ItemStack selectIcon = e.getCurrentItem();
                        ItemMeta selectMeta = selectIcon.getItemMeta();
                        PersistentDataContainer selectData = selectMeta.getPersistentDataContainer();
                        if (selectData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ShopMain"), PersistentDataType.STRING)) {
                            String selectVar = selectData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ShopMain"), PersistentDataType.STRING);
                            if (selectVar != null) {
                                switch (selectVar) {
                                    case "SELL" -> {
                                        player.sendMessage("성공: " +selectVar);
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
