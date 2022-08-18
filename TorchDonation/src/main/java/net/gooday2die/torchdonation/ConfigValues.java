package net.gooday2die.torchdonation;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import net.gooday2die.torchdonation.CommandHandler.SessionQueue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class ConfigValues {
    public static String username = null;
    public static String password = null;
    public static List<String> rewardCommands = null;
    public static boolean useMySQL = false;
    public static String dbIP = null;
    public static String dbID = null;
    public static String dbPW = null;
    public static String dbName = null;
    public static String dbTablePrefix = null;
    public static File curPath = null;
    public static JavaPlugin thisPlugin = null;
    public static SessionQueue sessionQueue = null;
    public static int queueSize = 0;
    public static boolean broadcastDonation;

    public static void loadConfig() {
        FileConfiguration config = ConfigValues.thisPlugin.getConfig();  // get config results

        ConfigValues.rewardCommands = config.getStringList("rewardCommands");
        ConfigValues.useMySQL = config.getBoolean("useMySQL");
        ConfigValues.dbIP = config.getString("dbIP");
        ConfigValues.dbID = config.getString("dbID");
        ConfigValues.dbPW = config.getString("dbPW");
        ConfigValues.dbName = config.getString("dbName");
        ConfigValues.dbTablePrefix = config.getString("dbTablePrefix");
        ConfigValues.broadcastDonation = config.getBoolean("broadcastDonation");
        ConfigValues.curPath = ConfigValues.thisPlugin.getDataFolder();
        ConfigValues.sessionQueue = new SessionQueue();
    }
}
