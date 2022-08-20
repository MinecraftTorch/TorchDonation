package net.gooday2die.torchdonation.CommandHandler;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * A class for reloading config.yml
 */
public class Reload implements CommandExecutor {
    /**
     * Override this command using onCommand.
     * This will reload config.yml and cookies.json
     * @param sender the command sender
     * @param command the command
     * @param label the label
     * @param args the arguments
     * @return always will return true.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigValues.db.close();
        ConfigValues.taskQueue.stop();
        ConfigValues.loadConfig();
        sender.sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + "파일을 reload 했습니다.");
        return true;
    }
}
