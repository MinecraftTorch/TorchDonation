package net.gooday2die.torchdonation.dbHandler;

import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlite3 extends AbstractDB {
    /**
     * A method that connects to DB.
     */
    @Override
    public void connect() {
        File dataFolder = new File(ConfigValues.curPath, "data.db");
        if (!dataFolder.exists()) { // if the file does not exist, generate one and do init.
            try {
                dataFolder.createNewFile();

                String url = "jdbc:sqlite:" + dataFolder;
                conn = DriverManager.getConnection(url);
                String createTable1 = "CREATE TABLE IF NOT EXISTS donation_log(username TEXT, code TEXT, date DATETIME, amount INT)";
                String createTable2 = "CREATE TABLE IF NOT EXISTS donation_clients(username TEXT, total INT)";

                Statement stmt = conn.createStatement();
                stmt.execute(createTable1);
                stmt.execute(createTable2);

            } catch (IOException | SQLException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "DB 파일을 생성할 수 없습니다.");
                e.printStackTrace();
            }
        }
        else { // if the file exists, just make connection.
            try {
                String url = "jdbc:sqlite:" + dataFolder;
                conn = DriverManager.getConnection(url);
            } catch (SQLException e){
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "DB 파일을 사용할 수 없습니다.");
                e.printStackTrace();
            }
        }
    }
}
