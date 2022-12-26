package com.msicraft.zodiacintegrated.StreamerGuild.Event;

import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.GuildMainInv;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class GuildMoneyChatEditEvent implements Listener {

    private final GuildUtil guildUtil = new GuildUtil();

    public static HashMap<UUID, Boolean> isMoneyChatEdit = new HashMap<>();
    public static HashMap<UUID, String> moneyEditVar = new HashMap<>();

    private final Economy economy = ZodiacIntegrated.getEconomy();

    @EventHandler
    public void onChatMoneyAdd(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isMoneyChatEdit.containsKey(uuid)) {
            boolean check = isMoneyChatEdit.get(uuid);
            if (check) {
                GuildMainInv guildMainInv = new GuildMainInv(player);
                e.setCancelled(true);
                String editVar = moneyEditVar.get(player.getUniqueId());
                if (editVar != null) {
                    Component component = e.message();
                    PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
                    String guildId = guildUtil.getContainGuildID(player);
                    String getChat = plainText.serialize(component);
                    if (!getChat.equals("cancel")) {
                        double getChatBalance = 0;
                        String replaceChat = getChat.replaceAll("[^0-9]", "");
                        try {
                            getChatBalance = Double.parseDouble(replaceChat);
                            double getPlayerBalance = economy.getBalance(player);
                            double getGuildMoney = guildUtil.getGuildMoney(guildId);
                            switch (editVar) {
                                case "ADD-MONEY" -> {
                                    if (getPlayerBalance - getChatBalance >= 0) {
                                        economy.withdrawPlayer(player, getChatBalance);
                                        guildUtil.addGuildMoney(guildId, getChatBalance);
                                        player.sendMessage(ChatColor.GREEN + "길드에 자금이 추가되었습니다.");
                                        player.sendMessage(ChatColor.GREEN + "추가된 자금: " + ChatColor.WHITE + getChatBalance);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "충분한 돈이 없습니다.");
                                    }
                                }
                                case "REMOVE-MONEY" -> {
                                    if (getGuildMoney - getChatBalance >= 0) {
                                        guildUtil.removeGuildMoney(guildId, getChatBalance);
                                        economy.depositPlayer(player, getChatBalance);
                                        player.sendMessage(ChatColor.GREEN + "길드에서 자금을 찾았습니다.");
                                        player.sendMessage(ChatColor.GREEN + "획득한 자금: " + ChatColor.WHITE + getChatBalance);
                                    } else {
                                        player.sendMessage(ChatColor.RED + "길드에 충분한 자금이 없습니다.");
                                    }

                                }
                            }
                        } catch (NumberFormatException numE) {
                            //numE.printStackTrace();
                            player.sendMessage(ChatColor.RED + "숫자를 입력해주세요");
                        }
                    }
                    moneyEditVar.put(player.getUniqueId(), null);
                    isMoneyChatEdit.put(player.getUniqueId(), false);
                    Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                        player.openInventory(guildMainInv.getInventory());
                        guildMainInv.setGuildMoneyManagementMenu(player);
                    }, 1L);
                }
            }
        }
    }

}
