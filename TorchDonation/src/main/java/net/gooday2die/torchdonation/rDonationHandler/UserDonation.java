package net.gooday2die.torchdonation.rDonationHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * A class that is for storing a single user donation.
 */
public class UserDonation {
    public CommandSender sender;
    public String giftCode;
    public String[] giftCodeParts;
    public boolean isSuccessful;
    public int amount;

    /**
     * A constructor method for class UserDonation.
     * This will parse gift code parts and store them as well.
     * @param sender The Player who issued donation.
     * @param giftCode The gift code.
     */
    public UserDonation(CommandSender sender, String giftCode) {
        this.sender = sender;
        this.giftCode = giftCode;

        giftCodeParts = new String[4];
        giftCodeParts[0] = giftCode.substring(0, 4);
        giftCodeParts[1] = giftCode.substring(5, 9);
        giftCodeParts[2] = giftCode.substring(10, 14);
        giftCodeParts[3] = giftCode.substring(15);
    }

    /**
     * A method that sets this donation as failure.
     */
    public void setFailure() {
        this.isSuccessful = false;
    }

    /**
     * A method that sets this donation as successful.
     */
    public void setSuccessful() {
        this.isSuccessful = true;
    }

    /**
     * A method that sets amount of donation amount.
     * @param amount The amount that was donated.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
