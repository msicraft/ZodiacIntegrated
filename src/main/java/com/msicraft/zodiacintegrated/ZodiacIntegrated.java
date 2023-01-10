package com.msicraft.zodiacintegrated;

import com.christian34.easyprefix.EasyPrefix;
import com.msicraft.zodiacintegrated.Admin.Event.AdminInvChatEvent;
import com.msicraft.zodiacintegrated.Admin.Event.AdminInvEvent;
import com.msicraft.zodiacintegrated.Command.MainCommand;
import com.msicraft.zodiacintegrated.Command.TabComplete;
import com.msicraft.zodiacintegrated.Event.DeathPenalty;
import com.msicraft.zodiacintegrated.EvolutionEntity.Data.EvolutionConfig;
import com.msicraft.zodiacintegrated.EvolutionEntity.Data.EvolutionDataConfig;
import com.msicraft.zodiacintegrated.EvolutionEntity.Event.EvolutionEntityDeath;
import com.msicraft.zodiacintegrated.Shop.Data.ShopData;
import com.msicraft.zodiacintegrated.Shop.Event.ShopInvClickEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.Data.GuildStorageData;
import com.msicraft.zodiacintegrated.StreamerGuild.Data.StreamerGuildData;
import com.msicraft.zodiacintegrated.StreamerGuild.Data.WhiteListPlayerData;
import com.msicraft.zodiacintegrated.StreamerGuild.Event.GuildMoneyChatEditEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.Event.GuildPlayerJoinEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.Event.GuildPrefixChatEditEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.Event.GuildRankManageChatEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.GuildStorageUtil;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.Event.GuildMainInvEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.Event.GuildStorageEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class ZodiacIntegrated extends JavaPlugin {

    private static ZodiacIntegrated plugin;
    public static StreamerGuildData streamerGuildData;
    public static WhiteListPlayerData whiteListPlayerData;
    public static EvolutionConfig evolutionConfig;
    public static EvolutionDataConfig evolutionDataConfig;
    public static GuildStorageData guildStorageData;
    public static ShopData shopData;
    public static UUID developerUUID = UUID.fromString("67bfaabc-6d16-4ad7-90f7-177697c05cee");

    private static Economy econ = null;

    public static ZodiacIntegrated getPlugin() {
        return plugin;
    }

    public static EasyPrefix getEasyPrefix() {
        return EasyPrefix.getInstance();
    }

    public static String getPrefix() {
        return "[Zodiac Integrated]";
    }

    private final GuildStorageUtil guildStorageUtil = new GuildStorageUtil();
    public static HashMap<String, HashMap<Integer, ItemStack>> guildStorage = new HashMap<>();

    public static HashMap<UUID, HashMap<Integer, ItemStack>> shopStorageData = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        streamerGuildData = new StreamerGuildData(this);
        whiteListPlayerData = new WhiteListPlayerData(this);
        evolutionConfig = new EvolutionConfig(this);
        evolutionDataConfig = new EvolutionDataConfig(this);
        guildStorageData = new GuildStorageData(this);
        shopData = new ShopData(this);
        createFiles();
        final int configVersion = plugin.getConfig().contains("config-version", true) ? plugin.getConfig().getInt("config-version") : -1;
        if (configVersion != 1) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " You are using the old config");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " Created the latest config.yml after replacing the old config.yml with config_old.yml");
            replaceconfig();
            createFiles();
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " You are using the latest version of config.yml");
        }
        final int streamerGuildVersion = streamerGuildData.getConfig().contains("config-version", true) ? streamerGuildData.getConfig().getInt("config-version") : -1;
        if (streamerGuildVersion != 1) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " You are using the old streamerGuild.yml");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " Created the latest streamerGuild.yml after replacing the old streamerGuild.yml with streamerGuild_old.yml");
            replaceStreamerGuildConfig();
            streamerGuildData = new StreamerGuildData(this);
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " You are using the latest version of streamerGuild.yml");
        }
        final int evolutionConfigVersion = evolutionConfig.getConfig().contains("config-version", true) ? evolutionConfig.getConfig().getInt("config-version") : -1;
        if (evolutionConfigVersion != 1) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " You are using the old evolution.yml");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " Created the latest evolution.yml after replacing the old evolution.yml with evolution_old.yml");
            replaceEvolutionConfig();
            evolutionConfig = new EvolutionConfig(this);
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " You are using the latest version of evolution.yml");
        }
        final int evolutionDataConfigVersion = evolutionDataConfig.getConfig().contains("config-version", true) ? evolutionDataConfig.getConfig().getInt("config-version") : -1;
        if (evolutionDataConfigVersion != 1) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " You are using the old evolutionData.yml");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " Created the latest evolutionData.yml after replacing the old evolutionData.yml with evolutionData_old.yml");
            replaceEvolutionDataConfig();
            evolutionDataConfig = new EvolutionDataConfig(this);
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " You are using the latest version of evolutionData.yml");
        }
        if (!setupEconomy()) {
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED + " No economy plugin found. Disabling");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        eventsRegister();
        commandsRegister();
        List<String> guildIdList = ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player");
        for (String guildId : guildIdList) {
            guildStorageUtil.loadYamlToStorageMap(guildId);
        }
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin Enable");
    }

    @Override
    public void onDisable() {
        List<String> guildIdList = ZodiacIntegrated.getPlugin().getConfig().getStringList("Identified-Player");
        for (String guildId : guildIdList) {
            guildStorageUtil.saveStorageMapToYaml(guildId);
        }
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED +" Plugin Disable");
    }

    private final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    private void eventsRegister() {
        pluginManager.registerEvents(new DeathPenalty(), this);
        pluginManager.registerEvents(new GuildMainInvEvent(), this);
        pluginManager.registerEvents(new GuildPrefixChatEditEvent(), this);
        pluginManager.registerEvents(new GuildPlayerJoinEvent(), this);
        pluginManager.registerEvents(new GuildMoneyChatEditEvent(), this);
        pluginManager.registerEvents(new ShopInvClickEvent(), this);
        pluginManager.registerEvents(new GuildRankManageChatEvent(), this);
        pluginManager.registerEvents(new GuildStorageEvent(), this);
        pluginManager.registerEvents(new AdminInvEvent(), this);
        pluginManager.registerEvents(new AdminInvChatEvent(), this);
        pluginManager.registerEvents(new EvolutionEntityDeath(), this);
    }

    private void commandsRegister() {
        PluginCommand mainCommand = getServer().getPluginCommand("zodiac");
        if (mainCommand != null) {
            mainCommand.setExecutor(new MainCommand());
            mainCommand.setTabCompleter(new TabComplete());
        }
    }

    public void FilesReload() {
        getPlugin().reloadConfig();
        streamerGuildData.reloadConfig();
        whiteListPlayerData.reloadConfig();
        evolutionConfig.reloadConfig();
    }

    protected FileConfiguration config;

    private void createFiles() {
        File configf = new File(getDataFolder(), "config.yml");

        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void replaceconfig() {
        File file = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"config_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old config.yml with config_old.yml and created a new config.yml");
    }

    private void replaceStreamerGuildConfig() {
        File file = new File(getDataFolder(), "streamerGuild.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"streamerGuild_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old streamerGuild.yml with streamerGuild_old.yml and created a new streamerGuild.yml");
    }

    private void replaceEvolutionConfig() {
        File file = new File(getDataFolder(), "evolution.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"evolution_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old evolution.yml with evolution_old.yml and created a new evolution.yml");
    }

    private void replaceEvolutionDataConfig() {
        File file = new File(getDataFolder(), "evolutionData.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"evolutionData_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old evolutionData.yml with evolutionData_old.yml and created a new evolutionData.yml");
    }

    public static Economy getEconomy() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


}
