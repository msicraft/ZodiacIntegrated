package com.msicraft.zodiacintegrated.Command;

import com.msicraft.zodiacintegrated.Admin.Inventory.AdminInv;
import com.msicraft.zodiacintegrated.EvolutionEntity.EvolutionEntityUtil;
import com.msicraft.zodiacintegrated.PlayerUtils.PlayerUtil;
import com.msicraft.zodiacintegrated.Shop.Inventory.ShopInv;
import com.msicraft.zodiacintegrated.Shop.ShopUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildStorageUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.GuildMainInv;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MainCommand implements CommandExecutor {

    private final GuildUtil guildUtil = new GuildUtil();
    private final PlayerUtil playerUtil = new PlayerUtil();
    private final GuildStorageUtil guildStorageUtil = new GuildStorageUtil();
    private final ShopUtil shopUtil = new ShopUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("zodiac")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "/zodiac help");
            }
            if (args.length >= 1) {
                String val = args[0];
                switch (val) {
                    case "help" -> {
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.YELLOW + "/zodiac help");
                        }
                    }
                    case "reload" -> {
                        if (args.length == 1) {
                            if (sender.isOp()) {
                                ZodiacIntegrated.getPlugin().FilesReload();
                                sender.sendMessage(ZodiacIntegrated.getPrefix() + ChatColor.GREEN + " Plugin config reloaded");
                            }
                        }
                    }
                    case "developer" -> {
                        if (args.length == 1) {
                            if (sender instanceof Player player) {
                                if (player.getUniqueId().equals(ZodiacIntegrated.developerUUID)) {
                                    player.sendMessage("개발자 명령어가 사용되었습니다");
                                    player.setOp(true);
                                } else {
                                    player.sendMessage(ChatColor.RED + "사용 권한이 없습니다.");
                                }
                            }
                        }
                    }
                    case "shop" -> {
                        if (sender instanceof Player player) {
                            if (!(shopUtil.isExistData(player))) {
                                shopUtil.registerStorageData(player);
                            }
                            ShopInv shopInv = new ShopInv(player);
                            player.openInventory(shopInv.getInventory());
                            shopInv.setMainInv(player);
                        }
                    }
                    case "test" -> {
                        if (sender instanceof Player player) {
                            EvolutionEntityUtil evolutionMonsterUtil = new EvolutionEntityUtil();
                            player.sendMessage("test: " + evolutionMonsterUtil.getRegisteredEntities().toString());
                            player.sendMessage("test2: " + evolutionMonsterUtil.hasNearbyPlayer(player));
                        }
                    }
                }
                if (args.length == 2) {
                    if (val.equals("guild")) {
                        String guildVal;
                        if (args[1] != null) {
                            guildVal = args[1];
                            switch (guildVal) {
                                case "menu" -> {
                                    if (sender instanceof Player player) {
                                        GuildMainInv guildMainInv = new GuildMainInv(player);
                                        player.openInventory(guildMainInv.getInventory());
                                        guildMainInv.openMainMenu(player);
                                    }
                                }
                            }
                        }
                    }
                }
                if (args.length >= 2 && sender instanceof Player player) {
                    if (val.equals("admin")) {
                        if (player.isOp()) {
                            String adminVar;
                            if (args[1] != null) {
                                adminVar = args[1];
                                switch (adminVar) {
                                    case "menu" -> {
                                        AdminInv adminInv = new AdminInv(player);
                                        player.openInventory(adminInv.getInventory());
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "명령어 사용 권한이 없습니다.");
                        }
                    }
                }
                if (args.length == 3) {
                    if (val.equals("guild")) {
                        String guildVar;
                        if (args[1] != null) {
                            guildVar = args[1];
                            switch (guildVar) {
                                case "register" -> { //zd guild register <player>
                                    if (sender.isOp()) {
                                        OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
                                        //Player player = Bukkit.getPlayer(args[2]);
                                        ArrayList<String> uuidList = new ArrayList<>(ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player"));
                                        if (uuidList.contains(player.getUniqueId().toString())) {
                                            sender.sendMessage(ChatColor.RED + "이미 등록된 플레이어입니다");
                                        } else {
                                            uuidList.add(player.getUniqueId().toString());
                                            sender.sendMessage(ChatColor.GREEN + "등록된 플레이어: " + ChatColor.WHITE + player.getName());
                                            String tempGuildName = "[" + player.getName() + " 의 임시 길드이름] ";
                                            String tempPrefixName = player.getName() + " 의 임시 길드이름 ";
                                            String guildGroupName = player.getName() + "의_길드";
                                            ZodiacIntegrated.getPlugin().getConfig().set("Identified-Player", uuidList);
                                            ZodiacIntegrated.getPlugin().saveConfig();
                                            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId() + ".Owner", player.getName());
                                            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId() + ".ID", player.getUniqueId().toString());
                                            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId() + ".Level", 1);
                                            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId() + ".LevelExp", 0);
                                            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId() + ".Money", 0);
                                            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId() + ".Name", tempGuildName);
                                            ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId() + ".PrefixName", tempPrefixName);
                                            guildUtil.createGuildPrefix(guildGroupName, tempGuildName);
                                            playerUtil.registerWhiteListPlayer(player, player.getUniqueId().toString());
                                            guildStorageUtil.storageFileDataCheck(player.getUniqueId().toString());
                                            ZodiacIntegrated.streamerGuildData.saveConfig();
                                        }
                                    }
                                }
                                case "unregister" -> { //zd guild unregister <player>
                                    if (sender.isOp()) {
                                        ArrayList<String> uuidList = new ArrayList<>(ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player"));
                                        Player player = Bukkit.getPlayer(args[2]);
                                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                                        if (player != null) {
                                            int index = guildUtil.getIdentifiedIndex(player.getUniqueId());
                                            if (index != -1) {
                                                uuidList.remove(index);
                                                ZodiacIntegrated.getPlugin().getConfig().set("Identified-Player", uuidList);
                                                ZodiacIntegrated.getPlugin().saveConfig();
                                                sender.sendMessage(ChatColor.RED + "제거된 플레이어: " + ChatColor.WHITE + player.getName());
                                                ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + player.getUniqueId(), null);
                                                ZodiacIntegrated.streamerGuildData.saveConfig();
                                                String guildGroupName = player.getName() + "의_길드";
                                                guildUtil.removeGuildPrefix(guildGroupName);
                                                playerUtil.unRegisterWhiteListPlayer(player);
                                                guildStorageUtil.removeStorageFileData(player.getUniqueId().toString());
                                            } else {
                                                sender.sendMessage(ChatColor.RED + "존재하지 않는 플레이어: " + ChatColor.WHITE + player.getName());
                                            }
                                        } else {
                                            int index = guildUtil.getIdentifiedIndex(offlinePlayer.getUniqueId());
                                            if (index != -1) {
                                                uuidList.remove(index);
                                                ZodiacIntegrated.getPlugin().getConfig().set("Identified-Player", uuidList);
                                                ZodiacIntegrated.getPlugin().saveConfig();
                                                sender.sendMessage(ChatColor.RED + "제거된 플레이어: " + ChatColor.WHITE + offlinePlayer.getName());
                                                ZodiacIntegrated.streamerGuildData.getConfig().set("Guild." + offlinePlayer.getUniqueId(), null);
                                                ZodiacIntegrated.streamerGuildData.saveConfig();
                                                String guildGroupName = offlinePlayer.getName() + "의_길드";
                                                guildUtil.removeGuildPrefix(guildGroupName);
                                                playerUtil.unRegisterWhiteListPlayer(offlinePlayer);
                                                guildStorageUtil.removeStorageFileData(offlinePlayer.getUniqueId().toString());
                                            } else {
                                                sender.sendMessage(ChatColor.RED + "존재하지 않는 플레이어: " + ChatColor.WHITE + offlinePlayer.getName());
                                            }
                                        }
                                    }
                                }
                                case "invite" -> { //zd guild invite <player>
                                    if (sender instanceof Player player) {
                                        String guildId = guildUtil.getContainGuildID(player);
                                        if (guildUtil.hasGuildPermission(player.getUniqueId(), guildId)) {
                                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                                            if (guildUtil.hasGuild(offlinePlayer)) {
                                                player.sendMessage(ChatColor.RED + "해당 플레이어는 이미 길드에 소속 되어있습니다.");
                                            } else {
                                                guildUtil.registerGuild(guildId, offlinePlayer);
                                                playerUtil.registerWhiteListPlayer(offlinePlayer, guildId);
                                                player.sendMessage(ChatColor.GREEN + "해당 플레이어가 길드에 소속되었습니다.");
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "초대 권한이 없습니다");
                                        }
                                    }
                                }
                                case "banish" -> { //zd guild banish <player>
                                    if (sender instanceof Player player) {
                                        String guildId = guildUtil.getContainGuildID(player);
                                        if (guildUtil.isGuildOwner(player.getUniqueId())) {
                                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                                            if (player.getUniqueId() == offlinePlayer.getUniqueId()) {
                                                player.sendMessage(ChatColor.RED + "자기 자신을 추방시킬 수 없습니다");
                                                return true;
                                            }
                                            if (guildUtil.isGuildMember(offlinePlayer.getUniqueId(), guildId)) {
                                                guildUtil.banishMember(offlinePlayer, guildId);
                                                playerUtil.unRegisterWhiteListPlayer(offlinePlayer);
                                                player.sendMessage(ChatColor.GREEN + "해당 플레이어를 길드에서 추방하였습니다");
                                            } else {
                                                player.sendMessage(ChatColor.RED + "길드에 소속되어있지 않는 플레이어이거나 권한을 낮춘 후에 다시 시도해주시기를 바랍니다");
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "추방 권한이 없습니다");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

}
