package net.gooday2die.torchdonation.CulturelandDonation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class Test implements CommandExecutor {
    JavaPlugin thisPlugin;
    public Test(JavaPlugin plugin){
        thisPlugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Session dO = new Session();
        dO.generateDriver();
        dO.login("gooday2die", "Asusb85m$g");
        return true;
    }
}
