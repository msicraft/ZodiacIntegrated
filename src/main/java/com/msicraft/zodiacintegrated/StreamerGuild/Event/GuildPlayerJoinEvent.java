package com.msicraft.zodiacintegrated.StreamerGuild.Event;

import com.christian34.easyprefix.groups.Group;
import com.christian34.easyprefix.user.User;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class GuildPlayerJoinEvent implements Listener {

    private final GuildUtil guildUtil = new GuildUtil();

    @EventHandler
    public void onPlayerWhiteList(AsyncPlayerPreLoginEvent e) {
        if (ZodiacIntegrated.getPlugin().getConfig().getBoolean("Setting.Whitelist.Enabled")) {
            if (!ZodiacIntegrated.whiteListPlayerData.getConfig().contains("WhiteList." + e.getUniqueId())) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                String message = ZodiacIntegrated.getPlugin().getConfig().getString("Setting.Whitelist.Message");
                if (message != null) {
                    e.kickMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message)));
                } else {
                    e.kickMessage(Component.text(ChatColor.RED + "서버에서 플레이하기 위해서는 길드에 소속되어 있어야 됩니다"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFirstJoinApplyPrefix(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (ZodiacIntegrated.whiteListPlayerData.getConfig().getBoolean("WhiteList." + player.getUniqueId() + ".FirstJoin")) {
            Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                String guildId = ZodiacIntegrated.whiteListPlayerData.getConfig().getString("WhiteList." + player.getUniqueId() + ".GuildId");
                User user = ZodiacIntegrated.getEasyPrefix().getUser(player);
                if (guildId != null) {
                    Group group = guildUtil.getGroup(player);
                    if (user.getGroup() != group) {
                        user.setGroup(group, true);
                    }
                } else {
                    Group group = ZodiacIntegrated.getEasyPrefix().getGroupHandler().getGroup("basic");
                    user.setGroup(group, true);
                }
                ZodiacIntegrated.whiteListPlayerData.getConfig().set("WhiteList." + player.getUniqueId() + ".FirstJoin", false);
                ZodiacIntegrated.whiteListPlayerData.saveConfig();
            }, 20L);
        }
    }

}
