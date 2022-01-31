package net.gooday2die.culturelanddonation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * A class for sending message to each class
 */
public class MessageSender {
    /**
     *  A class for sending message to console
     */
    public static class toConsole{
        /**
         * A method for sending information message to console
         * @param message the message to send
         */
        public static void info(String message){
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[CulturelandDonation] " + ChatColor.WHITE + message);
        }

        /**
         * A method for sending error message to console
         * @param message the message to send
         */
        public static void error(String message){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[CulturelandDonation] " + ChatColor.WHITE + message);
        }
    }

    /**
     * A class for sending message to user
     */
    public static class toUser{
        /**
         * A method for sending information message to user
         * @param sender The user
         * @param message The message
         */
        public static void info(CommandSender sender, String message){
            sender.sendMessage(ChatColor.GOLD + "[알림] " + ChatColor.WHITE + message);
        }
        /**
         * A method for sending error message to user
         * @param sender The user
         * @param message The message
         */
        public static void error(CommandSender sender, String message){
            sender.sendMessage(ChatColor.RED + "[에러] " + ChatColor.WHITE + message);
        }
    }

    /**
     * A class for sending message to admins with permission Admins.
     */
    public static class toAdmin{
        /**
         * A method for sending admins information message.
         * @param message the message
         */
        public static void info(String message){
            Bukkit.broadcast(ChatColor.GOLD + "[알림] " + ChatColor.WHITE + message, "culturelanddonate.admin");
        }
        /**
         * A method for sending admins error message.
         * @param message the message
         */
        public static void error(String message){
            Bukkit.broadcast(ChatColor.GOLD + "[에러] " + ChatColor.WHITE + message, "culturelanddonate.admin");
        }
    }
}
