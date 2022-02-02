package net.gooday2die.culturelanddonation.commands;

import net.gooday2die.culturelanddonation.MessageSender;
import net.gooday2die.culturelanddonation.donationProcessing.dbConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class reportDonation implements CommandExecutor {
    static JavaPlugin plugin;

    /**
     * A constructor method for this class
     *
     * @param argPlugin the plugin instance object
     */
    public reportDonation(JavaPlugin argPlugin) {
        plugin = argPlugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        dbConnection.addTry(sender.getName());
        if (args.length == 0){
            return false;
        }
        else{
            int donated = 0;
            donated = dbConnection.donationTotal(args[0]);
            MessageSender.toUser.info(sender, ChatColor.GREEN + args[0] + ChatColor.WHITE +  "님은 " +
                        ChatColor.RED + Integer.toString(donated) + ChatColor.WHITE + " 만큼 후원했습니다.");
            return true;
        }
    }
}

