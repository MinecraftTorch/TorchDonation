package net.gooday2die.culturelanddonation.commands;

import net.gooday2die.culturelanddonation.MessageSender;
import net.gooday2die.culturelanddonation.donationProcessing.dbConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class resetTries implements CommandExecutor {
    static JavaPlugin plugin;

    /**
     * A constructor method for this class
     *
     * @param argPlugin the plugin instance object
     */
    public resetTries(JavaPlugin argPlugin) {
        plugin = argPlugin;
    }

    /**
     * An overriding for onCommand
     *
     * @param sender  the sender
     * @param command the command
     * @param label   the label
     * @param args    the args
     * @return always true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        dbConnection.addTry(sender.getName());
        if (args.length == 0) {
            return false;
        }
        else{
            String username = args[0];
            System.out.println(username);
            dbConnection.resetTries(username);
            MessageSender.toConsole.info(username + "'s donation tries were reset by " + sender.getName());
        }
        return true;
    }
}