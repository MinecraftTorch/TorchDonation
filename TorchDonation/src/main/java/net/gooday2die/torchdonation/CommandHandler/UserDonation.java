package net.gooday2die.torchdonation.CommandHandler;

import net.gooday2die.torchdonation.ConfigValues;
import net.gooday2die.torchdonation.CulturelandDonation.Session;
import org.bukkit.command.CommandSender;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-10
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die
 */

/**
 * An object that is for saving information about a single user donation case
 */
public class UserDonation {
    CommandSender sender;
    String code;

    /**
     * A constructor method for class UserDonation
     * @param argSender the sender of this command
     * @param argCode the gift code to redeem
     */
    UserDonation(CommandSender argSender, String argCode){
        sender = argSender;
        code = argCode;
    }

    /**
     * A getter for Sender
     * @return returns CommandSender object
     */
    public CommandSender getSender(){
        return sender;
    }

    /**
     * A getter for codes
     * @return returns code String
     */
    public String getCode(){
        return code;
    }
}
