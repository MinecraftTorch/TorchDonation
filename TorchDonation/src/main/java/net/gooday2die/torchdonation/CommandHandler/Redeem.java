package net.gooday2die.torchdonation.CommandHandler;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import net.gooday2die.torchdonation.ConfigValues;
import net.gooday2die.torchdonation.rDonationHandler.UserDonation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.Pattern;


/**
 * A class for redeeming giftcards
 */
public class Redeem implements CommandExecutor {
    /**
     * Override this command using onCommand.
     * Redeem Giftcard using Asynchronous features.
     * @param sender the command sender
     * @param command the command
     * @param label the label
     * @param args the arguments
     * @return always will return true.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new BukkitRunnable() {
            /**
             * Override this for run method
             */
            @Override
            public void run() {
                if (args.length == 1) {  // Check there is one argument.
                    String giftcardFormat = "^\\d{4}-\\d{4}-\\d{4}-\\d{4,6}$";  // regex pattern for giftcard

                    if (!(Pattern.matches(giftcardFormat, args[0]))) { // If it does not match regex.
                        sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                                sender.getName() + " 올바르지 않은 문화상품권 형식입니다.");
                        sender.sendMessage(ChatColor.GREEN + "문화상품권 예시: 1234-1234-1234-1234 또는 1234-1234-1234-123456");
                    } else {
                        sender.sendMessage(ChatColor.GOLD + "[TorchDonation] " + ChatColor.WHITE
                                + "후원을 처리중입니다. 잠시만 기다려주세요... / 대기열 순위 " + ConfigValues.queueSize);
                        UserDonation userDonation = new UserDonation(sender, args[0]);

                        synchronized (ConfigValues.taskQueue) {
                            ConfigValues.taskQueue.enqueue(userDonation);
                        }
                    }
                }
            }
        }.runTaskAsynchronously(ConfigValues.thisPlugin);
        return true;
    }
}
