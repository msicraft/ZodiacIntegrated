package com.msicraft.zodiacintegrated.StreamerGuild.Event;

import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.GuildMainInv;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GuildRankManageChatEvent implements Listener {

    private final GuildUtil guildUtil = new GuildUtil();

    public static HashMap<UUID, Boolean> isRankEdit = new HashMap<>();
    public static HashMap<UUID, String> rankEditVar = new HashMap<>();

    @EventHandler
    public void onRankEditChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isRankEdit.containsKey(uuid)) {
            boolean check = isRankEdit.get(uuid);
            if (check) {
                GuildMainInv guildMainInv = new GuildMainInv(player);
                e.setCancelled(true);
                String editVar = rankEditVar.get(uuid);
                if (editVar != null) {
                    Component component = e.message();
                    PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
                    String getChat = plainText.serialize(component);
                    String guildId = guildUtil.getContainGuildID(player);
                    if (!getChat.equals("cancel")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getChat);
                        ArrayList<UUID> memberUUIDList = guildUtil.getGuildMemberList(guildId);
                        if (memberUUIDList.contains(offlinePlayer.getUniqueId())) {
                            switch (editVar) {
                                case "RankUp-SubOwner" -> {
                                    if (guildUtil.isGuildMember(offlinePlayer.getUniqueId(), guildId)) {
                                        player.sendMessage(ChatColor.GREEN + "?????? ??????????????? ??? ???????????? ?????????????????????.");
                                        player.sendMessage(ChatColor.GREEN + "????????????: " + ChatColor.WHITE + getChat);
                                        guildUtil.guildRankChange(editVar, offlinePlayer, guildId);
                                    } else if (guildUtil.isGuildSubOwner(offlinePlayer.getUniqueId(), guildId)) {
                                        player.sendMessage(ChatColor.RED + "?????? ??????????????? ?????? ??? ????????? ?????????.");
                                    } else if (guildUtil.isGuildOwner(offlinePlayer.getUniqueId())) {
                                        player.sendMessage(ChatColor.RED + "?????? ???????????? ????????? ????????? ??? ????????????.");
                                    }
                                }
                                case "RankDown-SubOwner" -> {
                                    if (guildUtil.isGuildMember(offlinePlayer.getUniqueId(), guildId)) {
                                        player.sendMessage(ChatColor.RED + "?????? ??????????????? ??? ???????????? ????????????.");
                                    } else if (guildUtil.isGuildSubOwner(offlinePlayer.getUniqueId(), guildId)) {
                                        player.sendMessage(ChatColor.GREEN + "?????? ??????????????? ????????? ?????????????????????.");
                                        player.sendMessage(ChatColor.GREEN + "????????????: " + ChatColor.WHITE + getChat);
                                        guildUtil.guildRankChange(editVar, offlinePlayer, guildId);
                                    } else if (guildUtil.isGuildOwner(offlinePlayer.getUniqueId())) {
                                        player.sendMessage(ChatColor.RED + "?????? ???????????? ????????? ????????? ??? ????????????.");
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "????????? ???????????? ?????? ???????????? ?????????.");
                        }
                    }
                    isRankEdit.put(uuid, false);
                    rankEditVar.put(uuid, null);
                    Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                        player.openInventory(guildMainInv.getInventory());
                        guildMainInv.setGuildMemberRank(player);
                    }, 1L);
                }
            }
        }
    }

}
