package net.gooday2die.torchdonation;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import net.gooday2die.torchdonation.CommandHandler.Redeem;
import net.gooday2die.torchdonation.CommandHandler.Reload;
import net.gooday2die.torchdonation.CommandHandler.SessionQueue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TorchDonation extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig(); // save default config if it does not exist.
        FileConfiguration config = this.getConfig();  // get config results

        ConfigValues.username = config.getString("username");
        ConfigValues.password = config.getString("password");
        ConfigValues.rewardCommands = config.getString("rewardCommands");
        ConfigValues.useMySQL = config.getBoolean("useMySQL");
        ConfigValues.dbIP = config.getString("dbIP");
        ConfigValues.dbID = config.getString("dbID");
        ConfigValues.dbPW = config.getString("dbPW");
        ConfigValues.dbName = config.getString("dbName");
        ConfigValues.dbTablePrefix = config.getString("dbTablePrefix");
        ConfigValues.curPath = this.getDataFolder();
        ConfigValues.thisPlugin = this;
        ConfigValues.sessionQueue = new SessionQueue();

        getCommand("redeem").setExecutor(new Redeem(this));
        getCommand("donate").setExecutor(new Redeem(this));
        getCommand("후원").setExecutor(new Redeem(this));
        getCommand("문상").setExecutor(new Redeem(this));
        getCommand("treload").setExecutor(new Reload(this));

        if(ConfigValues.useMySQL)
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " + ChatColor.WHITE + "MySQL 을 사용합니다.");
        else
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " + ChatColor.WHITE + "Sqlite 를 사용합니다.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " + ChatColor.WHITE + "플러그인이 로드되었습니다.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " + ChatColor.WHITE + "플러그인이 종료되었습니다.");
    }
}
