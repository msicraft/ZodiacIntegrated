package com.msicraft.zodiacintegrated.Admin.Event;

import com.msicraft.zodiacintegrated.Admin.AdminUtil;
import com.msicraft.zodiacintegrated.Admin.Inventory.AdminInv;
import com.msicraft.zodiacintegrated.Shop.ShopUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class AdminInvChatEvent implements Listener {

    private final ShopUtil shopUtil = new ShopUtil();
    private final AdminUtil adminUtil = new AdminUtil();
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
                        if (adminUtil.hasSameItemStack(itemStack)) {
                            player.sendMessage(ChatColor.RED + "이미 같은 아이템이 등록되어있습니다.");
                        } else {
                            try {
                                String replace = getChat.replaceAll("[^0-9]", "");
                                int value = Integer.parseInt(replace);
                                int count = adminUtil.getDataLastCount();
                                ZodiacIntegrated.shopData.getConfig().set("Item." + count + ".ItemStack", itemStack);
                                ZodiacIntegrated.shopData.getConfig().set("Item." + count + ".Value", value);
                                int last = count +1;
                                ZodiacIntegrated.shopData.getConfig().set("LastCount", last);
                                ZodiacIntegrated.shopData.saveConfig();
                                player.sendMessage(ChatColor.GREEN + "아이템이 등록되었습니다");
                            } catch (NumberFormatException ex) {
                                Bukkit.getConsoleSender().sendMessage("zd admin shop-register <value> : 잘못된 숫자 | " + getChat);
                            }
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

    public static HashMap<UUID, Boolean> isReplaceValue = new HashMap<>();
    public static HashMap<UUID, Integer> replaceItemStack = new HashMap<>();

    @EventHandler
    public void onReplaceItemValue(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isReplaceValue.containsKey(uuid)) {
            boolean check = isReplaceValue.get(uuid);
            if (check) {
                e.setCancelled(true);
                Component component = e.message();
                PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
                String getChat = plainText.serialize(component);
                if (!getChat.equals("cancel")) {
                    if (replaceItemStack.containsKey(uuid)) {
                        int dataNum = replaceItemStack.get(uuid);
                        ItemStack itemStack = ZodiacIntegrated.shopData.getConfig().getItemStack("Item." + dataNum + ".ItemStack");
                        if (itemStack != null) {
                            try {
                                String replace = getChat.replaceAll("[^0-9]", "");
                                int value = Integer.parseInt(replace);
                                ZodiacIntegrated.shopData.getConfig().set("Item." + dataNum + ".ItemStack", itemStack);
                                ZodiacIntegrated.shopData.getConfig().set("Item." + dataNum + ".Value", value);
                                ZodiacIntegrated.shopData.saveConfig();
                                player.sendMessage(ChatColor.GREEN + "값이 변경되었습니다.");
                                adminUtil.updateShopData();
                            } catch (NumberFormatException exception) {
                                Bukkit.getConsoleSender().sendMessage("Shop Item replace value error : 잘못된 숫자 | " + getChat);
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "잘못된 아이템");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "잘못된 아이템 값");
                    }
                }
                isReplaceValue.put(uuid, false);
                replaceItemStack.remove(uuid);
                AdminInv adminInv = new AdminInv(player);
                Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                    player.openInventory(adminInv.getInventory());
                    adminInv.registeredItemList();
                }, 1L);
            }
        }
    }

}
