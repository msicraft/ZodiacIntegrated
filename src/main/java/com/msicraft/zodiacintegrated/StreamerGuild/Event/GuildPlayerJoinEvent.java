package com.msicraft.zodiacintegrated.StreamerGuild.Event;

import com.christian34.easyprefix.groups.Group;
import com.christian34.easyprefix.user.User;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GuildPlayerJoinEvent implements Listener {

    private final GuildUtil guildUtil = new GuildUtil();

    @EventHandler
    public void onPlayerFirstJoinApplyPrefix(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPlayedBefore()) {
            //String guildId = ZodiacIntegrated.whiteListPlayerData.getConfig().getString("WhiteList." + player.getUniqueId() + ".GuildId");
            User user = ZodiacIntegrated.getEasyPrefix().getUser(player);
            Group group = guildUtil.getGroup(player);
            if (user.getGroup() != group) {
                user.setGroup(group, true);
            }
        }
    }

}
