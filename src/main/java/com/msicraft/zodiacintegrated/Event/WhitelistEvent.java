package com.msicraft.zodiacintegrated.Event;

import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class WhitelistEvent implements Listener {

    private GuildUtil guildUtil = new GuildUtil();

    @EventHandler
    public void onWhitelistMovement(PlayerMoveEvent e) {
        if (ZodiacIntegrated.getPlugin().getConfig().getBoolean("Setting.Whitelist.Movement.Enabled")) {
            Player player = e.getPlayer();
            if (!(guildUtil.hasGuild(player))) {
                String getMessage = ZodiacIntegrated.getPlugin().getConfig().getString("Setting.Whitelist.Movement.Message");
                if (getMessage != null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage));
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onWhitelistJoin(PlayerJoinEvent e) {
        if (ZodiacIntegrated.getPlugin().getConfig().getBoolean("Setting.Whitelist.Join.Enabled")) {
            Player player = e.getPlayer();
            if (!(guildUtil.hasGuild(player))) {
                String getMessage = ZodiacIntegrated.getPlugin().getConfig().getString("Setting.Whitelist.Join.Message");
                if (getMessage != null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage));
                    Bukkit.getScheduler().runTaskLater(ZodiacIntegrated.getPlugin(), () -> {
                        player.kick(Component.text(ChatColor.translateAlternateColorCodes('&', getMessage)));
                    }, 20L);
                }
            }
        }
    }

}
