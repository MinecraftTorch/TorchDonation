package net.gooday2die.torchdonation;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import net.gooday2die.torchdonation.dbHandler.AbstractDB;
import net.gooday2die.torchdonation.dbHandler.MySQL;
import net.gooday2die.torchdonation.dbHandler.Sqlite3;
import net.gooday2die.torchdonation.DonationHandler.Session;
import net.gooday2die.torchdonation.DonationHandler.TaskQueue;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigValues {
    public static List<String> rewardCommands = null;
    public static boolean useMySQL = false;
    public static String dbIP = null;
    public static String dbID = null;
    public static String dbPW = null;
    public static String dbName = null;
    public static String dbTablePrefix = null;
    public static File curPath = null;
    public static JavaPlugin thisPlugin = null;
    public static TaskQueue taskQueue = null;
    public static int queueSize = 0;
    public static boolean broadcastDonation;
    public static JSONArray cookies;
    public static AbstractDB db = null;

    public static void loadConfig() {
        FileConfiguration config = ConfigValues.thisPlugin.getConfig();  // get config results

        // Store config values from config.yml
        ConfigValues.rewardCommands = config.getStringList("rewardCommands");
        ConfigValues.useMySQL = config.getBoolean("useMySQL");
        ConfigValues.dbIP = config.getString("dbIP");
        ConfigValues.dbID = config.getString("dbID");
        ConfigValues.dbPW = config.getString("dbPW");
        ConfigValues.dbName = config.getString("dbName");
        ConfigValues.dbTablePrefix = config.getString("dbTablePrefix");
        ConfigValues.broadcastDonation = config.getBoolean("broadcastDonation");
        ConfigValues.curPath = ConfigValues.thisPlugin.getDataFolder();

        try { // Try loading cookies.json.
            ConfigValues.loadJson();
        } catch (Exception e) { // If that failed print stacktrace and send error message also disable plugin.
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE + "cookies.json 파일을 불러올 수 없습니다.");
            Bukkit.getPluginManager().disablePlugin(thisPlugin);
        }

        // Generate session queue using async, since it takes time.
        new BukkitRunnable() {
            @Override
            public void run() {
                try { // Try logging into Cultureland website.
                    generateQueue();
                } catch (Session.LoginFailureException e) { // If that failed, disable plugin.
                    e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE + "컬쳐랜드 웹사이트에 로그인할 수 없습니다.");
                    Bukkit.getPluginManager().disablePlugin(thisPlugin);
                }
            }
        }.runTaskAsynchronously(thisPlugin);

        ConfigValues.loadDB();
    }

    /**
     * A private static method that loads cookie.json file.
     * @throws IOException When opening file had exception.
     * @throws JSONException When JSON was not able to be parsed.
     */
    private static void loadJson() throws IOException, JSONException {
        // The path to cookies.json
        Path cookiesPath = Paths.get(ConfigValues.thisPlugin.getDataFolder().getAbsolutePath(), "cookies.json");

        if (new File(cookiesPath.toString()).exists()){ // Check if it exists.
            // Read it using InputStream
            InputStream is = Files.newInputStream(Paths.get(cookiesPath.toString()));
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            // Parse JSON String.
            ConfigValues.cookies = new JSONArray(jsonTxt);
        }
    }

    /**
     * A private static method that generates task queue.
     * @throws Session.LoginFailureException When login failed.
     */
    private static void generateQueue() throws Session.LoginFailureException {
        Session session = new Session();

        try {
            session.login();
        } catch (Session.LoginFailureException | JSONException e) {
            throw new Session.LoginFailureException();
        }

        taskQueue = new TaskQueue(session);
    }

    /**
     * A private static method that connects db according to settings.
     */
    private static void loadDB() {
        // Select DB type.
        if (ConfigValues.useMySQL) ConfigValues.db = new MySQL();
        else ConfigValues.db = new Sqlite3();

        // Connect.
        ConfigValues.db.connect();
    }
}
