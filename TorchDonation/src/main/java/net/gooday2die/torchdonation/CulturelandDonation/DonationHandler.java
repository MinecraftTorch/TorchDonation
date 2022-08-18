package net.gooday2die.torchdonation.CulturelandDonation;

import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.ExecutionException;

public class DonationHandler {
    /**
     * A method that rewards the user when the donation was success.
     * @param user The user to reward
     * @param amount Integer value of how much the user donated
     */
    public void reward(CommandSender user, int amount){
        // Notify console and the user that user is being rewarded
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급중입니다.");
        user.sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + "후원 보상을 지급중입니다...");

        // Iterate over all reward commands and execute them.
        for (String command : ConfigValues.rewardCommands) {
            command = command.replaceAll("%amount%", Integer.toString(amount));
            String finalCommand = command.replaceAll("%user%", user.getName());
            try { // Use sync method since some might need to be called from main thread.
                Bukkit.getScheduler().callSyncMethod(ConfigValues.thisPlugin, () ->
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand)).get();
            } catch (ExecutionException | InterruptedException e) { // If exception occurred let user know.
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급할 수 없습니다.");
                user.sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "후원 보상을 지급할 수 없습니다. 관리자에게 문의해주세요.");
            }
        }

        // If the rewarding process is done successfully, notify them
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급했습니다.");
        user.sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + "후원 보상을 지급했습니다.");

        // If broadcasting donation was enabled, broadcast the message.
        if (ConfigValues.broadcastDonation) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[TorchDonation] " + ChatColor.GOLD + user.getName()
                    + ChatColor.WHITE + " 님이 " + ChatColor.GREEN + amount + ChatColor.WHITE + " 원을 후원하셨습니다!");
        }
    }
}
