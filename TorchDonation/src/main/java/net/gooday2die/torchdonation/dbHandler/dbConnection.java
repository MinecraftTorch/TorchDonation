package net.gooday2die.torchdonation.dbHandler;

import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

/**
 * A class for db connection
 */
public class dbConnection {
    /**
     * An abstraction class for db connection.
     * There will be MySQL connection class and Sqlite connection class.
     * This is the main abstraction class for both of them
     */
    abstract public static class connectionAbstract {
        abstract public int getDonationTotal(String username);
        abstract public void donated(String username, String code, int amount);
        abstract public void close();
    }

    /**
     * A class that is for connection with MySQL server
     */
    public static class MysqlCon extends connectionAbstract{
        Connection conn = null;
        /**
         * A constructor method for connection to MysqlDB
         */
        public MysqlCon() {
            try { // if table does not exist, generate one.
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://" + ConfigValues.dbIP + "/" + ConfigValues.dbName;
                conn = DriverManager.getConnection(url, ConfigValues.dbID, ConfigValues.dbPW);

                // create table if not exists
                String createTable1 = "CREATE TABLE IF NOT EXISTS " + ConfigValues.dbTablePrefix +
                        "_donation_log(username TEXT, code TEXT, date DATETIME, amount INT)";
                String createTable2 = "CREATE TABLE IF NOT EXISTS " + ConfigValues.dbTablePrefix +
                        "_donation_clients(username TEXT, total INT)";

                Statement stmt = conn.createStatement();
                stmt.execute(createTable1);
                stmt.execute(createTable2);

            } catch (ClassNotFoundException | SQLException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "MYSQL DB에 연결할 수 없습니다.");
            }
        }
        /**
         * A method that gets total donation amount from db
         *
         * @param username the username to check
         * @return returns integer amount of money. -1 if not exists
         */
        @Override
        public int getDonationTotal(String username) {
         int total = 0;
            try {
                String SQL = "SELECT total FROM " + ConfigValues.dbTablePrefix +
                        "_donation_clients WHERE username=\"" + username + "\"";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                rs.next();
                total = rs.getInt("total");
            } catch (SQLException e) { // when user does not exist
                total = -1;
            }
            return total;
        }

        /**
         * A method that updates or inserts into database about a new player donation
         * @param username the username to insert
         * @param code the giftcard.
         * @param amount the amount of the giftcard.
         */
        @Override
        public void donated(String username, String code, int amount) {
            int total = getDonationTotal(username);
            total = total + amount;
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);

            String SQL1;
            if (getDonationTotal(username) == -1) {
                SQL1 = "INSERT INTO " + ConfigValues.dbTablePrefix +
                        "_donation_clients (username, total) VALUE (\"" + username + "\", " + amount + ")";
            } else {
                SQL1 = "UPDATE " + ConfigValues.dbTablePrefix + "_donation_clients SET total=" + total
                        + " WHERE username=\"" + username + "\";";
            }
            String SQL2 = "INSERT INTO " + ConfigValues.dbTablePrefix +
                    "_donation_log (username, code, date, amount) VALUE (\"" + username + "\", \"" +
                    code + "\", \"" + currentTime + "\" , " + amount + ")";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(SQL1);
                stmt.executeUpdate(SQL2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * A method that closes the db connection.
         */
        @Override
        public void close(){
            try {
                conn.close();
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "MYSQL DB연결을 종료할 수 없습니다.");
            }
        }
    }

    /**
     * A class for Sqlite connection.
     */
    public static class sqliteCon extends connectionAbstract {
        Connection conn = null;

        /**
         * A constructor method for sqliteCon class.
         */
        public sqliteCon() {
            File dataFolder = new File(ConfigValues.curPath, "data.db");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                    ChatColor.WHITE + "SQLITE DB를 사용중입니다.");
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

        /**
         * A method that retrieves total amount of donation money
         * @param username the username to retrieve from
         * @return returns total amount of money that the user donated. Returns -1 if none before.
         */
        @Override
        public int getDonationTotal(String username) {
            int total = 0;
            try {
                String SQL = "SELECT total FROM donation_clients TorchUsers WHERE username=\"" + username + "\"";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);
                rs.next();
                total = rs.getInt("total");

            } catch (Exception e) { // when user does not exist
                total = -1;
            }
            return total;
        }

        /**
         * A method that updates or inserts into database about a new player donation
         * @param username the username to insert
         * @param code the giftcard.
         * @param amount the amount of the giftcard.
         */
        @Override
        public void donated(String username, String code, int amount) {
            int total = getDonationTotal(username);
            total = total + amount;
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);

            String SQL1;
            if (getDonationTotal(username) == -1) { // if it's first time, insert
                SQL1 = "INSERT INTO donation_clients (username, total) VALUES (" + "\"" + username + "\", " + amount + ")";
            } else { // if not, update
                SQL1 = "UPDATE donation_clients SET total=" + total + " WHERE username=\"" + username + "\";";
            }
            String SQL2 = "INSERT INTO donation_log (username, code, date, amount) VALUES (\"" + username + "\", \"" +
                    code + "\", \"" + currentTime + "\" , " + amount + ")";
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(SQL1);
                stmt.executeUpdate(SQL2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * A method that closes the db connection.
         */
        @Override
        public void close(){
            try {
                conn.close();
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "Sqlite DB연결을 종료할 수 없습니다.");
            }
        }
    }
}
