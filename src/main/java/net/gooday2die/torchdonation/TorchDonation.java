package net.gooday2die.torchdonation;

import net.gooday2die.torchdonation.CulturelandDonation.Test;
import org.bukkit.plugin.java.JavaPlugin;

public final class TorchDonation extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("test").setExecutor(new Test(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
