package net.gooday2die.torchdonation.CulturelandDonation;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import net.gooday2die.torchdonation.ConfigValues;
import net.gooday2die.torchdonation.dbHandler.dbConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutionException;

/**
 * A method for rewarding user when donation was successful.
 */
public class RewardUser {
    /**
     * A method that parses commands from config.yml and store them as String[]
     * @param commands The string that contains un-parsed commands from config.yml
     * @return The String[] object of parsed commands
     */
    private static String[] parseCommand(String commands){
        return commands.split("##");
    }

    /**
     * A method that rewards the users by the parsed command from parseCommand.
     * @param user the user to reward
     * @param plugin JavaPlugin object for this plugin
     * @param amount integer value of how much the user donated
     */
    public static void reward(CommandSender user, JavaPlugin plugin, int amount){
        String[] commands = parseCommand(ConfigValues.rewardCommands); // parse commands

        // notify console and the user that user is being rewarded
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급중입니다.");
        user.sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + "후원 보상을 지급중입니다...");

        for (String curCommand : commands) { // for all commands
            curCommand = curCommand.replaceAll("%AMOUNT%" , Integer.toString(amount));
            curCommand = curCommand.replaceAll("%USER%" , user.getName());

            // if we have %AMOUNT% in command as placeholder, replace it with donated amount
            // if we have %USER% in command as placeholder, replace it with user's name
            try { // try executing command as console.
                String finalCurCommand = curCommand;
                Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender()
                        , finalCurCommand)).get(); // had to use this method since we are running this async.
            }catch (ExecutionException | InterruptedException e){ // If exception occurs, just fucking quit.
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급할 수 없습니다.");
                user.sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "후원 보상을 지급할 수 없습니다. 관리자에게 문의해주세요.");
                return;
            }
        }
        // If the rewarding process is done successfully, notify them
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급했습니다.");
        user.sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + "후원 보상을 지급했습니다.");

    }
}
