package com.msicraft.zodiacintegrated.StreamerGuild.Inventory.Event;

import com.msicraft.zodiacintegrated.StreamerGuild.GuildStorageUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.GuildMainInv;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.GuildStorageInv;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
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

public class GuildStorageEvent implements Listener {

    private final GuildUtil guildUtil = new GuildUtil();
    private final GuildStorageUtil guildStorageUtil = new GuildStorageUtil();

    @EventHandler
    public void guildStorageClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) { return; }
        if (e.getView().getTitle().equalsIgnoreCase("Guild Storage")) {
            Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) {
                return;
            }
            ItemStack selectItem = e.getCurrentItem();
            ItemMeta selectItemMeta = selectItem.getItemMeta();
            PersistentDataContainer selectItemData = selectItemMeta.getPersistentDataContainer();
            String guildId = guildUtil.getContainGuildID(player);
            GuildMainInv guildMainInv = new GuildMainInv(player);
            GuildStorageInv guildStorageInv = new GuildStorageInv(player, guildId);
            if (selectItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "GuildStorage-FixItem"), PersistentDataType.STRING)) {
                String data = selectItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "GuildStorage-FixItem"), PersistentDataType.STRING);
                if (data != null && data.equals("FIX")) {
                    e.setCancelled(true);
                    if (selectItemData.has(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildStorage"), PersistentDataType.STRING)) {
                        String data2 = selectItemData.get(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildStorage"), PersistentDataType.STRING);
                        if (data2 != null) {
                            switch (data2) {
                                case "Back" -> {
                                    player.openInventory(guildMainInv.getInventory());
                                    guildMainInv.openMainMenu(player);
                                }
                                case "Storage-Update" -> {
                                    player.openInventory(guildStorageInv.getInventory());
                                    guildStorageInv.loadGuildStorage(guildId);
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
                    guildStorageUtil.sendPlayerToStorage(guildId, selectItem, player, slot);
                    player.openInventory(guildStorageInv.getInventory());
                    guildStorageInv.loadGuildStorage(guildId);
                }
                case CHEST -> {
                    int slot = e.getSlot();
                    if (guildStorageUtil.isExistItemStack(guildId, slot, selectItem)) {
                        guildStorageUtil.sendStorageToPlayer(guildId, player, slot);
                    } else {
                        player.sendMessage(ChatColor.RED + "해당 아이템이 존재하지 않습니다.");
                    }
                    player.openInventory(guildStorageInv.getInventory());
                    guildStorageInv.loadGuildStorage(guildId);
                }
            }
        }
    }

}
