package net.gooday2die.torchdonation.dbHandler;

import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL extends AbstractDB {
    public Connection conn = null;

    /**
     * A method that connects to MySQL DB.
     */
    @Override
    public void connect() {
        try { // Try connecting DB.
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ConfigValues.dbIP + "/" + ConfigValues.dbName;
            conn = DriverManager.getConnection(url, ConfigValues.dbID, ConfigValues.dbPW);

        } catch (ClassNotFoundException | SQLException e) { // If connecting DB failed, disable plugin.
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                    ChatColor.WHITE + "MYSQL DB에 연결할 수 없습니다.");
            Bukkit.getPluginManager().disablePlugin(ConfigValues.thisPlugin);
        }

        try {
            this.generateTables();
        } catch (SQLException e) { // If generating table failed, disable plugin.
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                    ChatColor.WHITE + "MYSQL DB에 연결할 수 없습니다.");
            Bukkit.getPluginManager().disablePlugin(ConfigValues.thisPlugin);
        }
    }
}
