package net.gooday2die.torchdonation.dbHandler;

import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

public class Sqlite3 extends AbstractDB {
    /**
     * A method that connects to DB.
     */
    @Override
    public void connect() {
        File dataFolder = new File(ConfigValues.curPath, "data.db");

        try {
            // Just tired of warning that the output of this function is ignored. Just go assert true.
            assert true | dataFolder.createNewFile();
            String url = "jdbc:sqlite:" + dataFolder;
            conn = DriverManager.getConnection(url);
            this.generateTables();
        } catch (IOException | SQLException e) {
            if (e instanceof IOException) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "DB 파일을 생성 또는 사용할 수 없습니다.");
            } else {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "DB 에 Table 을 생성할 수 없습니다.");
            }
        }
    }
}
