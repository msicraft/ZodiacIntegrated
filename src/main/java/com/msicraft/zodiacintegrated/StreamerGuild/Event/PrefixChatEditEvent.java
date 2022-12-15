package com.msicraft.zodiacintegrated.StreamerGuild.Event;
import com.christian34.easyprefix.groups.Group;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.GuildMainInv;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

public class PrefixChatEditEvent implements Listener {

    private GuildUtil guildUtil = new GuildUtil();

    public static HashMap<UUID, Boolean> isPrefixChatEdit = new HashMap<>();
    public static HashMap<UUID, String> prefixEditVar = new HashMap<>();

    public static HashMap<UUID, Boolean> isPrefixChatColorEdit = new HashMap<>();

    @EventHandler
    public void onPrefixChatEdit(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isPrefixChatEdit.containsKey(uuid)) {
            boolean isCPrefixEdit = isPrefixChatEdit.get(uuid);
            if (isCPrefixEdit) {
                GuildMainInv guildMainInv = new GuildMainInv(player);
                e.setCancelled(true);
                Component component = e.message();
                PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
                String var = prefixEditVar.get(uuid);
                if (var != null) {
                    if (var.equals("Change_Prefix")) {
                        String getChat = plainText.serialize(component);
                        Group group = guildUtil.getGroup(player);
                        if (!getChat.equals("cancel")) {
                            String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|\s]*$";
                            if (!Pattern.matches(pattern, getChat)) {
                                player.sendMessage(ChatColor.RED +"특수문자는 사용 불가능합니다");
                            } else {
                                String guildId = guildUtil.getContainGuildID(player);
                                String originalPrefix = ZodiacIntegrated.streamerGuildData.getConfig().getString("Guild." + guildId + ".PrefixName");
                                String regexPrefix = "[" + getChat + "] ";
                                group.setPrefix(regexPrefix);
                                ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + guildId + ".Name", regexPrefix);
                                ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + guildId + ".PrefixName", getChat);
                                ZodiacIntegrated.streamerGuildData.saveConfig();
                                player.sendMessage(ChatColor.GREEN + "길드 칭호 이름이 변경되었습니다.");
                                player.sendMessage(ChatColor.GREEN + "변경전 이름: " + ChatColor.WHITE + originalPrefix);
                                player.sendMessage(ChatColor.GREEN + "변경된 이름: " + ChatColor.WHITE + getChat);
                            }
                        }
                        isPrefixChatEdit.put(uuid, false);
                        prefixEditVar.put(uuid, null);
                        Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                            player.openInventory(guildMainInv.getInventory());
                            guildMainInv.setGuildManagement(player);
                        }, 1L);
                    }
                }
            }
        }
    }

    public static final ArrayList<ChatColor> banishChatColor = new ArrayList<>(Arrays.asList(ChatColor.ITALIC, ChatColor.MAGIC, ChatColor.RESET, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE, ChatColor.BOLD));

    public static ArrayList<String> getAvailableColorName() {
        ArrayList<String> list = new ArrayList<>();
        for (ChatColor color : ChatColor.values()) {
            if (!banishChatColor.contains(color)) {
                list.add(color.name());
            }
        }
        return list;
    }

    @EventHandler
    public void onPrefixChatColorEdit(AsyncChatEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isPrefixChatColorEdit.containsKey(uuid)) {
            boolean isColorEdit = isPrefixChatColorEdit.get(uuid);
            if (isColorEdit) {
                GuildMainInv guildMainInv = new GuildMainInv(player);
                e.setCancelled(true);
                Component component = e.message();
                PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
                String getChat = plainText.serialize(component);
                Group group = guildUtil.getGroup(player);
                if (!getChat.equals("cancel")) {
                    String guildId = guildUtil.getContainGuildID(player);
                    String getPrefix = ZodiacIntegrated.streamerGuildData.getConfig().getString("Guild." + guildId + ".PrefixName");
                    if (getAvailableColorName().contains(getChat.toUpperCase())) {
                        ChatColor color = ChatColor.valueOf(getChat.toUpperCase());
                        String prefix = ChatColor.WHITE + "[" + color + getPrefix + ChatColor.WHITE + "] ";
                        group.setPrefix(prefix);
                        ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + guildId + ".Name", prefix);
                        ZodiacIntegrated.streamerGuildData.saveConfig();
                        player.sendMessage(ChatColor.GREEN + "칭호 색상이 변경되었습니다");
                    } else {
                        player.sendMessage(ChatColor.RED + "잘못된 색상입니다.");
                    }
                }
                isPrefixChatColorEdit.put(uuid, false);
                Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                    player.openInventory(guildMainInv.getInventory());
                    guildMainInv.setGuildManagement(player);
                }, 1L);
            }
        }
    }

}
