package com.msicraft.zodiacintegrated.StreamerGuild;

import com.christian34.easyprefix.groups.Group;
import com.christian34.easyprefix.groups.GroupHandler;
import com.christian34.easyprefix.utils.Color;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class GuildUtil {

    public void createGuildPrefix(String groupName, String prefixName) {
        GroupHandler groupHandler = ZodiacIntegrated.getEasyPrefix().getGroupHandler();
        groupHandler.createGroup(groupName);
        Group group = groupHandler.getGroup(groupName);
        group.setPrefix(prefixName);
        group.setSuffix("&f:");
        group.setChatColor(Color.WHITE);
    }

    public void removeGuildPrefix(String groupName) {
        GroupHandler groupHandler = ZodiacIntegrated.getEasyPrefix().getGroupHandler();
        Group group = groupHandler.getGroup(groupName);
        group.delete();
    }

    public Group getGroup(Player player) {
        OfflinePlayer guildOwner = Bukkit.getOfflinePlayer(getGuildOwner(getContainGuildID(player)));
        String groupName = guildOwner.getName() + "의_길드";
        GroupHandler groupHandler = ZodiacIntegrated.getEasyPrefix().getGroupHandler();
        return groupHandler.getGroup(groupName);
    }

    public int getIdentifiedIndex(UUID uuid) {
        int index;
        int count = 0;
        ArrayList<String> uuidList = new ArrayList<>(ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player"));
        int max = uuidList.size();
        for (String uuidString : uuidList) {
            UUID getUUID = UUID.fromString(uuidString);
            if (uuid.equals(getUUID)) {
                break;
            } else {
                count++;
            }
            if (count >= max) {
                count = -1;
                break;
            }
        }
        index = count;
        return index;
    }

    public boolean isGuildOwner(UUID uuid) {
        return ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player").contains(uuid.toString());
    }

    public boolean isGuildSubOwner(UUID uuid, String guildId) {
        return ZodiacIntegrated.streamerGuildData.getConfig().getStringList("Guild." + guildId + ".SubOwner").contains(uuid.toString());
    }

    public boolean isGuildMember(UUID uuid, String guildId) {
        return ZodiacIntegrated.streamerGuildData.getConfig().getStringList("Guild." + guildId + ".Member").contains(uuid.toString());
    }

    public boolean hasGuildPermission(UUID uuid, String guildId) {
        return isGuildOwner(uuid) || isGuildSubOwner(uuid, guildId);
    }

    public boolean hasGuild(OfflinePlayer offlinePlayer) {
        boolean check = false;
        UUID uuid = offlinePlayer.getUniqueId();
        for (String guildId : ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player")) {
            if (isGuildMember(uuid, guildId) || isGuildSubOwner(uuid, guildId) || isGuildOwner(uuid)) {
                check = true;
                break;
            }
        }
        return check;
    }

    public String getGuildName(String guildId) {
        String name = null;
        if (ZodiacIntegrated.streamerGuildData.getConfig().contains("Guild." + guildId + ".Name")) {
            name = ZodiacIntegrated.streamerGuildData.getConfig().getString("Guild." + guildId + ".Name");
        }
        return name;
    }

    public UUID getGuildOwner(String guildId) {
        UUID uuid = null;
        String uuidS = ZodiacIntegrated.streamerGuildData.getConfig().getString("Guild." + guildId + ".ID");
        if (uuidS != null) {
            uuid = UUID.fromString(uuidS);
        }
        return uuid;
    }

    public String getContainGuildID(Player player) {
        String uuidS = null;
        for (String guildUUID : ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player")) {
            if (ZodiacIntegrated.streamerGuildData.getConfig().getStringList("Guild." + guildUUID + ".Member").contains(player.getUniqueId().toString())) {
                uuidS = ZodiacIntegrated.streamerGuildData.getConfig().getString("Guild." + guildUUID + ".ID");
            } else if (ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player").contains(player.getUniqueId().toString())) {
                uuidS = ZodiacIntegrated.streamerGuildData.getConfig().getString("Guild." + guildUUID + ".ID");
            }
        }
        return uuidS;
    }

    public ArrayList<UUID> getGuildMemberList(String guildId) {
        ArrayList<UUID> list = new ArrayList<>();
        String ownerUUID = ZodiacIntegrated.streamerGuildData.getConfig().getString("Guild." + guildId + ".ID");
        if (ownerUUID != null) {
            list.add(UUID.fromString(ownerUUID));
        }
        for (String subOwnerUUID : ZodiacIntegrated.streamerGuildData.getConfig().getStringList("Guild." + guildId + ".SubOwner")) {
            list.add(UUID.fromString(subOwnerUUID));
        }
        for (String memberUUID : ZodiacIntegrated.streamerGuildData.getConfig().getStringList("Guild." + guildId + ".Member")) {
            list.add(UUID.fromString(memberUUID));
        }
        return list;
    }

    public void registerGuild(String guildId, OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        ArrayList<String> list = new ArrayList<>(ZodiacIntegrated.streamerGuildData.getConfig().getStringList("Guild." + guildId + ".Member"));
        list.add(uuid.toString());
        ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + guildId + ".Member", list);
        ZodiacIntegrated.streamerGuildData.saveConfig();
    }

    public void banishMember(OfflinePlayer offlinePlayer, String guildId) {
        int count = 0;
        ArrayList<String> getMemberList = new ArrayList<>(ZodiacIntegrated.streamerGuildData.getConfig().getStringList("Guild." + guildId + ".Member"));
        if (!getMemberList.isEmpty()) {
            for (String uuidS : getMemberList) {
                if (uuidS.equals(offlinePlayer.getUniqueId().toString())) {
                    break;
                } else {
                    count++;
                }
            }
            getMemberList.remove(count);
            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + guildId + ".Member", getMemberList);
            ZodiacIntegrated.streamerGuildData.saveConfig();
        }
    }

}
