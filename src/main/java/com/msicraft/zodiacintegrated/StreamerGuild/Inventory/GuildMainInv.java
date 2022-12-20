package com.msicraft.zodiacintegrated.StreamerGuild.Inventory;

import com.msicraft.zodiacintegrated.StreamerGuild.GuildUtil;
import com.msicraft.zodiacintegrated.ZodiacIntegrated;
import it.unimi.dsi.fastutil.Hash;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class GuildMainInv implements InventoryHolder {

    private Inventory guildMainInv;

    private GuildUtil guildUtil = new GuildUtil();

    public GuildMainInv(Player player) {
        guildMainInv = Bukkit.createInventory(player, 54, Component.text("Guild"));
    }

    public void openMainMenu(Player player) {
        guildMainInv.clear();
        createSelectMenu();
        setGuildIcon(player);
    }

    public void setGuildMoneyManagementMenu(Player player) {
        setGuildIcon(player);
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", basicLoreList, "ZD-Guild-MoneyManagement", "Back");
        guildMainInv.setItem(45, itemStack);
        itemStack = createNormalItem(Material.DIAMOND, ChatColor.WHITE + "자금 추가", basicLoreList, "ZD-Guild-MoneyManagement", "ADD-MONEY");
        guildMainInv.setItem(19, itemStack);
        itemStack = createNormalItem(Material.DIAMOND, ChatColor.WHITE + "자금 얻기", basicLoreList, "ZD-Guild-MoneyManagement", "REMOVE-MONEY");
        guildMainInv.setItem(20, itemStack);
    }

    public void setGuildNameChangeMenu(Player player) {
        setGuildIcon(player);
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", basicLoreList, "ZD-GuildPrefixEdit", "Back");
        guildMainInv.setItem(45, itemStack);
        itemStack = createNormalItem(Material.ANVIL, ChatColor.WHITE + "이름 변경", basicLoreList ,"ZD-GuildPrefixEdit", "Change_Prefix");
        guildMainInv.setItem(19, itemStack);
        itemStack = createNormalItem(Material.INK_SAC, ChatColor.WHITE + "색상 변경", basicLoreList, "ZD-GuildPrefixEdit", "Change_Color");
        guildMainInv.setItem(20, itemStack);
    }

    public void setGuildManagement(Player player) {
        setGuildIcon(player);
        ItemStack itemStack;
        itemStack = createNormalItem(Material.BARRIER, ChatColor.RED + "Back", basicLoreList, "ZD-GuildManagement", "Back");
        guildMainInv.setItem(45, itemStack);
        itemStack = createNormalItem(Material.NAME_TAG, ChatColor.WHITE + "길드 이름 변경", basicLoreList, "ZD-GuildManagement", "Change_Name");
        guildMainInv.setItem(19, itemStack);
    }

    public static HashMap<UUID, String> otherGuild_page = new HashMap<>(); //"page:<count>"
    public static HashMap<UUID, String> otherGuild_maxPage = new HashMap<>(); //"max-page:<count>"
    private static final int[] guildSlots = {9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44};
    private ArrayList<String> guildIdList = new ArrayList<>(ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player"));


    public void setOtherGuildList(Player player) {
        setGuildIcon(player);
        setOtherGuildListMaxPage(player);
        setOtherGuildListBasic();
        ItemStack itemStack;
        int maxGuildSize = guildIdList.size();
        int page_num = 0;
        String pageNumObject = otherGuild_page.get(player.getUniqueId());
        if (pageNumObject != null) {
            String[] a = pageNumObject.split(":");
            page_num = Integer.parseInt(a[1]);
        }
        int max = guildSlots.length;
        int lastCount = page_num*max;
        int count = 0;
        List<Component> loreList = new ArrayList<>();
        for (int a = lastCount; a<maxGuildSize; a++) {
            if (!loreList.isEmpty()) {
                loreList.clear();
            }
            int slot = guildSlots[count];
            UUID uuid = UUID.fromString(guildIdList.get(a));
            String getGuildName = guildUtil.getGuildName(String.valueOf(uuid));
            itemStack = new ItemStack(Material.PAPER, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', getGuildName)));
            loreList.add(Component.text(ChatColor.GREEN + "길드 인원: " + ChatColor.WHITE + guildUtil.getGuildMemberList(String.valueOf(uuid)).size() + " 명"));
            itemMeta.lore(loreList);
            itemStack.setItemMeta(itemMeta);
            guildMainInv.setItem(slot, itemStack);
            count++;
            if (count >= max) {
                break;
            }
        }
    }

    private void setOtherGuildListMaxPage(Player player) {
        int max = ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player").size();
        int maxCount = max/36;
        String var = "max-page:" + maxCount;
        otherGuild_maxPage.put(player.getUniqueId(), var);
        //
        String getPage = otherGuild_page.get(player.getUniqueId());
        String pageCount = "0";
        if (getPage != null) {
            String[] a = getPage.split(":");
            pageCount = a[1];
        }
        int count = Integer.parseInt(pageCount) + 1;
        ItemStack itemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(ChatColor.WHITE + "Page: " + count));
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(49, itemStack);
    }

    private void setOtherGuildListBasic() {
        ItemStack itemStack;
        itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        itemMeta.displayName(Component.text(ChatColor.WHITE + "다음"));
        data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildOtherList"), PersistentDataType.STRING, "Next");
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(50, itemStack);
        itemMeta.displayName(Component.text(ChatColor.WHITE + "이전"));
        data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildOtherList"), PersistentDataType.STRING, "Previous");
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(48, itemStack);
        itemStack = new ItemStack(Material.BARRIER, 1);
        itemMeta.displayName(Component.text(ChatColor.RED + "Back"));
        data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildOtherList"), PersistentDataType.STRING, "Back");
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(45, itemStack);
    }

    public static HashMap<UUID, String> member_page = new HashMap<>(); //"page:<count>"
    public static HashMap<UUID, String> member_maxPage = new HashMap<>(); //"max-page:<count>"
    private static final int[] playerSlots = {9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44};

    public void setGuildMemberList(Player player) {
        setMemberListMaxPage(player);
        guildMemberListBasicButton();
        setGuildIcon(player);
        String guildId = guildUtil.getContainGuildID(player);
        ArrayList<UUID> playerUUIDList = guildUtil.getGuildMemberList(guildId);
        int maxPlayerCount = playerUUIDList.size();
        int page_num = 0;
        String pageNumObject = member_page.get(player.getUniqueId());
        if (pageNumObject != null) {
            String[] a = pageNumObject.split(":");
            page_num = Integer.parseInt(a[1]);
        }
        int max = playerSlots.length;
        int lastCount = page_num*max;
        int count = 0;
        ItemStack itemStack;
        for (int a = lastCount; a<maxPlayerCount; a++) {
            int slot = playerSlots[count];
            UUID uuid = playerUUIDList.get(a);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            itemStack = getMemberPlayerHead(offlinePlayer);
            guildMainInv.setItem(slot, itemStack);
            count++;
            if (count >= max) {
                break;
            }
        }
    }

    private ItemStack getMemberPlayerHead(OfflinePlayer offlinePlayer) {
        if (!basicLoreList.isEmpty()) {
            basicLoreList.clear();
        }
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (!skullMeta.hasOwner()) {
            skullMeta.setOwningPlayer(offlinePlayer);
        }
        skullMeta.displayName(Component.text(ChatColor.WHITE + offlinePlayer.getName()));
        if (offlinePlayer.isOnline()) {
            basicLoreList.add(Component.text(ChatColor.WHITE + "상태:" + ChatColor.GREEN + " 온라인"));
        } else {
            basicLoreList.add(Component.text(ChatColor.WHITE + "상태:" + ChatColor.RED + " 오프라인"));
        }
        skullMeta.lore(basicLoreList);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    private void setMemberListMaxPage(Player player) {
        String guildId = guildUtil.getContainGuildID(player);
        int maxSize = guildUtil.getGuildMemberList(guildId).size();
        int maxCount = maxSize/36;
        String var = "max-page:" + maxCount;
        member_maxPage.put(player.getUniqueId(), var);
        //
        String getPage = member_page.get(player.getUniqueId());
        String pageCount = "0";
        if (getPage != null) {
            String[] a = getPage.split(":");
            pageCount = a[1];
        }
        int count = Integer.parseInt(pageCount) + 1;
        ItemStack itemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(ChatColor.WHITE + "Page: " + count));
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(49, itemStack);
    }

    private void guildMemberListBasicButton() {
        ItemStack itemStack;
        itemStack = new ItemStack(Material.ARROW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        itemMeta.displayName(Component.text(ChatColor.WHITE + "다음"));
        data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMemberList"), PersistentDataType.STRING, "ZD-GuildMemberList-Next");
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(50, itemStack);
        itemMeta.displayName(Component.text(ChatColor.WHITE + "이전"));
        data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMemberList"), PersistentDataType.STRING, "ZD-GuildMemberList-Previous");
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(48, itemStack);
        itemStack = new ItemStack(Material.BARRIER, 1);
        itemMeta.displayName(Component.text(ChatColor.RED + "Back"));
        data.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), "ZD-GuildMemberList"), PersistentDataType.STRING, "ZD-GuildMemberList-Back");
        itemStack.setItemMeta(itemMeta);
        guildMainInv.setItem(45, itemStack);
    }

    private List<Component> basicLoreList = new ArrayList<>();

    private void createSelectMenu() {
        if (!basicLoreList.isEmpty()) {
            basicLoreList.clear();
        }
        ItemStack itemStack;
        itemStack = createNormalItem(Material.PLAYER_HEAD, ChatColor.WHITE + "길드원 목록", basicLoreList, "ZD-GuildMainMenu", "ZD-Guild-MemberList");
        guildMainInv.setItem(19, itemStack);
        itemStack = createNormalItem(Material.GRINDSTONE, "길드 관리", basicLoreList, "ZD-GuildMainMenu", "ZD-Guild-Management");
        guildMainInv.setItem(28, itemStack);
        itemStack = createNormalItem(Material.ENCHANTED_BOOK, ChatColor.WHITE + "길드 목록", basicLoreList, "ZD-GuildMainMenu", "ZD-Guild-OtherGuild");
        guildMainInv.setItem(20, itemStack);
        itemStack = createNormalItem(Material.DIAMOND, "길드 자금 관리", basicLoreList, "ZD-GuildMainMenu", "ZD-Guild-MoneyManagement");
        guildMainInv.setItem(29, itemStack);
    }

    private void setGuildIcon(Player player) {
        ItemStack itemStack;
        String getContainGuildId = guildUtil.getContainGuildID(player);
        String guildName = guildUtil.getGuildName(getContainGuildId);
        double getGuildMoney = guildUtil.getGuildMoney(getContainGuildId);
        basicLoreList.add(Component.text(ChatColor.GREEN+"길드 이름: " + ChatColor.WHITE + guildName));
        basicLoreList.add(Component.text(""));
        basicLoreList.add(Component.text(ChatColor.GREEN + "길드 자금: " + ChatColor.WHITE + getGuildMoney));
        itemStack = createNormalItem(Material.BOOK, ChatColor.WHITE + "길드", basicLoreList, "ZD-GuildMainMenu", guildName);
        guildMainInv.setItem(4, itemStack);
        if (!basicLoreList.isEmpty()) {
            basicLoreList.clear();
        }
    }

    private List<Component> loreList = new ArrayList<>();

    private ItemStack createNormalItem(Material material, String name, List<Component> list, String dataTag, String data) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(name));
        itemMeta.lore(list);
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(ZodiacIntegrated.getPlugin(), dataTag), PersistentDataType.STRING, data);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getPlayerHead(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        if (!skullMeta.hasOwner()) {
            skullMeta.setOwningPlayer(offlinePlayer);
        }
        skullMeta.displayName(Component.text(ChatColor.WHITE + offlinePlayer.getName()));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    @Override
    public Inventory getInventory() {
        return guildMainInv;
    }
}
