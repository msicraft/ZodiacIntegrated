package com.msicraft.zodiacintegrated;

import com.christian34.easyprefix.EasyPrefix;
import com.msicraft.zodiacintegrated.Command.MainCommand;
import com.msicraft.zodiacintegrated.Command.TabComplete;
import com.msicraft.zodiacintegrated.Data.StreamerGuildData;
import com.msicraft.zodiacintegrated.Event.PvPDeathPenalty;
import com.msicraft.zodiacintegrated.Event.WhitelistEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.Event.PrefixChatEditEvent;
import com.msicraft.zodiacintegrated.StreamerGuild.Inventory.Event.GuildMainInvEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ZodiacIntegrated extends JavaPlugin {

    private static ZodiacIntegrated plugin;
    public static StreamerGuildData streamerGuildData;

    public static ZodiacIntegrated getPlugin() {
        return plugin;
    }

    public static EasyPrefix getEasyPrefix() {
        return EasyPrefix.getInstance();
    }

    public static String getPrefix() {
        return "[Zodiac Integrated]";
    }

    @Override
    public void onEnable() {
        plugin = this;
        streamerGuildData = new StreamerGuildData(this);
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
        eventsRegister();
        commandsRegister();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin Enable");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + ChatColor.RED +" Plugin Disable");
    }

    private PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    private void eventsRegister() {
        pluginManager.registerEvents(new PvPDeathPenalty(), this);
        pluginManager.registerEvents(new GuildMainInvEvent(), this);
        pluginManager.registerEvents(new WhitelistEvent(), this);
        pluginManager.registerEvents(new PrefixChatEditEvent(), this);
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

}
