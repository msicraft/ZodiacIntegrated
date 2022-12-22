package com.msicraft.zodiacintegrated.PlayerUtils;

import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.OfflinePlayer;

public class PlayerUtil {

    public void registerWhiteListPlayer(OfflinePlayer offlinePlayer, String guildId) {
        ZodiacIntegrated.whiteListPlayerData.getConfig().set("WhiteList." + offlinePlayer.getUniqueId() + ".Name", offlinePlayer.getName());
        ZodiacIntegrated.whiteListPlayerData.getConfig().set("WhiteList." + offlinePlayer.getUniqueId() + ".GuildId", guildId);
        ZodiacIntegrated.whiteListPlayerData.getConfig().set("WhiteList." + offlinePlayer.getUniqueId() + ".FirstJoin", true);
        ZodiacIntegrated.whiteListPlayerData.saveConfig();
    }

    public void unRegisterWhiteListPlayer(OfflinePlayer offlinePlayer) {
        ZodiacIntegrated.whiteListPlayerData.getConfig().set("WhiteList." + offlinePlayer.getUniqueId(), null);
        ZodiacIntegrated.whiteListPlayerData.saveConfig();
    }

}
