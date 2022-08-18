package net.gooday2die.torchdonation;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import com.github.dockerjava.api.model.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.gooday2die.torchdonation.CommandHandler.SessionQueue;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static JSONArray cookies;

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

        try { // Try loading cookies.json.
            ConfigValues.loadJson();
        } catch (Exception e) { // If that failed print stacktrace and send error message.
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE + "cookies.json 파일을 불러올 수 없습니다.");
        }
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
}
