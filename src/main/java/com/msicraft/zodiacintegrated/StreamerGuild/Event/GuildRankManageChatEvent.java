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
                                        player.sendMessage(ChatColor.GREEN + "해당 플레이어가 부 마스터로 승급되었습니다.");
                                        player.sendMessage(ChatColor.GREEN + "플레이어: " + ChatColor.WHITE + getChat);
                                        guildUtil.guildRankChange(editVar, offlinePlayer, guildId);
                                    } else if (guildUtil.isGuildSubOwner(offlinePlayer.getUniqueId(), guildId)) {
                                        player.sendMessage(ChatColor.RED + "해당 플레이어는 이미 부 마스터 입니다.");
                                    } else if (guildUtil.isGuildOwner(offlinePlayer.getUniqueId())) {
                                        player.sendMessage(ChatColor.RED + "길드 마스터의 직책을 변경할 수 없습니다.");
                                    }
                                }
                                case "RankDown-SubOwner" -> {
                                    if (guildUtil.isGuildMember(offlinePlayer.getUniqueId(), guildId)) {
                                        player.sendMessage(ChatColor.RED + "해당 플레이어는 부 마스터가 아닙니다.");
                                    } else if (guildUtil.isGuildSubOwner(offlinePlayer.getUniqueId(), guildId)) {
                                        player.sendMessage(ChatColor.GREEN + "해당 플레이어가 멤버로 강등되었습니다.");
                                        player.sendMessage(ChatColor.GREEN + "플레이어: " + ChatColor.WHITE + getChat);
                                        guildUtil.guildRankChange(editVar, offlinePlayer, guildId);
                                    } else if (guildUtil.isGuildOwner(offlinePlayer.getUniqueId())) {
                                        player.sendMessage(ChatColor.RED + "길드 마스터의 직책을 변경할 수 없습니다.");
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "길드에 속해있지 않는 플레이어 입니다.");
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
