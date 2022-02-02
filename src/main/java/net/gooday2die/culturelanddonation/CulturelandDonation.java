package net.gooday2die.culturelanddonation;

import net.gooday2die.culturelanddonation.commands.ClandDonate;
import net.gooday2die.culturelanddonation.commands.reportDonation;
import net.gooday2die.culturelanddonation.commands.resetTries;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class CulturelandDonation extends JavaPlugin {
    /**
     * A method when this plugin is loaded
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig(); // save default config if it does not exist.
        FileConfiguration config = this.getConfig();  // get config results

        ConfigValues.key1 = config.getString("key1");
        ConfigValues.key2 = config.getString("key2");
        ConfigValues.commands = config.getString("donationCommands");
        ConfigValues.db_ip = config.getString("db_ip");
        ConfigValues.db_id = config.getString("db_user");
        ConfigValues.db_pw = config.getString("db_passwd");
        ConfigValues.broadcast = config.getBoolean("broadcast");
        ConfigValues.api_url = config.getString("api_server");
        ConfigValues.db_enable = config.getBoolean("db_enable");
        ConfigValues.db_name = config.getString("db_name");
        ConfigValues.cur_path = this.getDataFolder();

        getCommand("redeem").setExecutor(new ClandDonate(this));  // register commands into spigot
        getCommand("donate").setExecutor(new ClandDonate(this));  // register commands into spigot
        getCommand("후원").setExecutor(new ClandDonate(this));  // register commands into spigot
        getCommand("문상").setExecutor(new ClandDonate(this));  // register commands into spigot
        getCommand("dresettries").setExecutor(new resetTries(this));
        getCommand("reportdonation").setExecutor(new reportDonation(this));

        if (ConfigValues.db_enable)
            MessageSender.toConsole.info("Mysql DB를 사용합니다.");
        else
            MessageSender.toConsole.info("Sqlite DB를 사용합니다.");

        MessageSender.toConsole.info("플러그인이 성공적으로 로드 되었습니다. By Gooday2die");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MessageSender.toConsole.info("플러그인이 성공적으로 언로드 되었습니다. By Gooday2die");
    }
}
