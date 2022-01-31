package net.gooday2die.culturelanddonation.donationProcessing;

import net.gooday2die.culturelanddonation.ConfigValues;
import net.gooday2die.culturelanddonation.MessageSender;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutionException;

/**
 * A method for rewarding user when donation was successful.
 */
public class rewardUser {
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
        String[] commands = parseCommand(ConfigValues.commands); // parse commands

        // notify console and the user that user is being rewarded
        MessageSender.toConsole.info("Rewarding user : " + user.getName());
        MessageSender.toUser.info(user, "후원 보상을 지급중입니다...");

        for (String curCommand : commands) { // for all commands
            curCommand = curCommand.replaceAll("%AMOUNT%" , Integer.toString(amount));
            // if we have %AMOUNT% in command as placeholder, replace it with donated amount
            try { // try executing command as console.
                String finalCurCommand = curCommand;
                Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender()
                        , finalCurCommand)).get(); // had to use this method since we are running this async.
            }catch (ExecutionException | InterruptedException e){ // If exception occurs, just fucking quit.
                MessageSender.toConsole.error("Cannot reward user : " + user.getName());
                MessageSender.toUser.error(user, "죄송합니다... 후원 보상을 지급할 수 없습니다. 관리자에게 문의해주세요");
            }
        }
        // If the rewarding process is done successfully, notify them
        MessageSender.toConsole.info("Successfully rewarded user : " + user.getName());
        MessageSender.toUser.info(user, "후원 보상을 지급했습니다! 감사합니다!");
    }
}
