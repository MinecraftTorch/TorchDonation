package net.gooday2die.torchdonation;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import net.gooday2die.torchdonation.CommandHandler.SessionQueue;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigValues {
    public static String username = null;
    public static String password = null;
    public static String rewardCommands = null;
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
}
