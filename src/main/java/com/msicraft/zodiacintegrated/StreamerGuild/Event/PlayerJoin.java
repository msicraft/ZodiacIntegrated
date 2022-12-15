package com.msicraft.zodiacintegrated.StreamerGuild.Event;

import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPlayedBefore()) {
            String guildId = ZodiacIntegrated.whiteListPlayerData.getConfig().getString("WhiteList." + player.getUniqueId() + ".GuildId");
        }
    }

}
