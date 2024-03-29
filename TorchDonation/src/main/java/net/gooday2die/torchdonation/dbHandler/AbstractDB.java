package net.gooday2die.torchdonation.dbHandler;

import net.gooday2die.torchdonation.ConfigValues;
import net.gooday2die.torchdonation.DonationHandler.UserDonation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An abstract class that implements and defines actions for DB interaction.
 */
public abstract class AbstractDB {
    protected Connection conn;

    /**
     * A method that connects to DB.
     */
    public abstract void connect();

    /**
     * A method that generates tables for DB.
     * @throws SQLException When generating tables failed.
     */
    protected void generateTables() throws SQLException {
        // create table if not exists
        String createTable1 = String.format("CREATE TABLE IF NOT EXISTS %s_donation_log(username TEXT, code TEXT, date DATETIME, amount INT)", ConfigValues.dbTablePrefix);
        String createTable2 = String.format("CREATE TABLE IF NOT EXISTS %s_donation_clients(username TEXT, total INT)", ConfigValues.dbTablePrefix);

        try {
            // NullPointerException will never happen since this will not be called before initializing conn.
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createTable1);
            stmt.executeUpdate(createTable2);
        } catch (SQLException e) { // When query failed, throw exception.
            e.printStackTrace();
            throw new dbQueryFailedException();
        }
    }
    /**
     * A method that records donation information to DB.
     * @param donation The UserDonation object.
     * @throws dbQueryFailedException When DB Query failed.
     */
    public void recordDonation(UserDonation donation) throws dbQueryFailedException{
        String username = donation.sender.getName();
        int amount = donation.amount;
        String code = donation.giftCode;

        int total = getDonationTotal(username);
        total = total + amount;
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        String SQL1;
        if (getDonationTotal(username) == -1) {
            SQL1 = String.format("INSERT INTO %s_donation_clients (username, total) VALUES (\"%s\", %d)", ConfigValues.dbTablePrefix, username, amount);
        } else {
            SQL1 = String.format("UPDATE %s_donation_clients SET total=%d WHERE username=\"%s\";", ConfigValues.dbTablePrefix, total, username);
        }
        String SQL2 = String.format("INSERT INTO %s_donation_log (username, code, date, amount) VALUES (\"%s\", \"%s\", \"%s\" , %d)", ConfigValues.dbTablePrefix, username, code, currentTime, amount);
        try {
            // NullPointerException will never happen since this will not be called before initializing conn.
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL1);
            stmt.executeUpdate(SQL2);
        } catch (SQLException e) { // When query failed, throw exception.
            e.printStackTrace();
            throw new dbQueryFailedException();
        }
    }

    /**
     * A method that gets user's total donated amount.
     * @param username The user's name
     * @return The user's donation amount in total.
     */
    public int getDonationTotal(String username) {
        int total;
        try {
            String SQL = String.format("SELECT total FROM %s_donation_clients WHERE username=\"%s\"", ConfigValues.dbTablePrefix, username);

            // NullPointerException will never happen since this will not be called before initializing conn.
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
     * A method that closes connection from DB.
     */
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                    ChatColor.WHITE + "MYSQL DB연결을 종료할 수 없습니다.");
            throw new dbQueryFailedException();
        }
    }

    /**
     * A RuntimeException that is for when db query failed.
     */
    public static class dbQueryFailedException extends RuntimeException {}
}
