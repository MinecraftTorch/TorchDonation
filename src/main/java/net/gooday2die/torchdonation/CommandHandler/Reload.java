package net.gooday2die.torchdonation.CommandHandler;

import net.gooday2die.torchdonation.ConfigValues;
import net.gooday2die.torchdonation.CulturelandDonation.Session;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A class for reloading config.yml
 */
public class Reload implements CommandExecutor {
    JavaPlugin thisPlugin;

    /**
     * A constructor method for this class
     * @param plugin
     */
    public Reload(JavaPlugin plugin){
        thisPlugin = plugin;
    }

    /**
     * Override this command using onCommand.
     * Reload config using Asynchronous features.
     * @param sender the command sender
     * @param command the command
     * @param label the label
     * @param args the arguments
     * @return always will return true.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration config = thisPlugin.getConfig();  // get config results

                ConfigValues.username = config.getString("username");
                ConfigValues.password = config.getString("password");
                ConfigValues.rewardCommands = config.getString("rewardCommands");
                ConfigValues.useMySQL = config.getBoolean("useMySQL");
                ConfigValues.dbIP = config.getString("dbIP");
                ConfigValues.dbID = config.getString("dbID");
                ConfigValues.dbPW = config.getString("dbPW");
                ConfigValues.dbName = config.getString("dbName");
                ConfigValues.dbTablePrefix = config.getString("dbTablePrefix");
            }
        }.runTaskAsynchronously(thisPlugin);
        return true;
    }
}
