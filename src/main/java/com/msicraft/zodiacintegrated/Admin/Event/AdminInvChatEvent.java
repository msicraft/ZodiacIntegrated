package com.msicraft.zodiacintegrated.Admin.Event;

import com.msicraft.zodiacintegrated.Admin.Inventory.AdminInv;
import com.msicraft.zodiacintegrated.Shop.ShopUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class AdminInvChatEvent implements Listener {

    private final ShopUtil shopUtil = new ShopUtil();
    public static HashMap<UUID, Boolean> isShopRegister = new HashMap<>();

    @EventHandler
    public void onShopRegisterItem(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isShopRegister.containsKey(uuid)) {
            boolean check = isShopRegister.get(uuid);
            if (check) {
                e.setCancelled(true);
                Component component = e.message();
                PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
                String getChat = plainText.serialize(component);
                if (!getChat.equals("cancel")) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack.getType() == Material.AIR) {
                        player.sendMessage(ChatColor.RED + "아이템을 들고 사용해주세요.");
                    } else {
                        try {
                            String replace = getChat.replaceAll("[^0-9]", "");
                            int value = Integer.parseInt(replace);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                            data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-Shop-Item"), PersistentDataType.INTEGER, value);
                            itemStack.setItemMeta(itemMeta);
                            int count = shopUtil.getDataLastCount();
                            ZodiacIntegrated.shopData.getConfig().set("Item." + count, itemStack);
                            int last = count +1;
                            ZodiacIntegrated.shopData.getConfig().set("LastCount", last);
                            ZodiacIntegrated.shopData.saveConfig();
                            player.sendMessage(ChatColor.GREEN + "아이템이 등록되었습니다");
                        } catch (NumberFormatException ex) {
                            Bukkit.getConsoleSender().sendMessage("zd admin shop-register <value> : 잘못된 숫자 | " + getChat);
                        }
                    }
                }
                isShopRegister.put(uuid, false);
                AdminInv adminInv = new AdminInv(player);
                Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                    player.openInventory(adminInv.getInventory());
                    adminInv.setShopManagementInv();
                }, 1L);
            }
        }
    }

}
