package net.gooday2die.culturelanddonation.donationProcessing;

import net.gooday2die.culturelanddonation.ConfigValues;
import net.gooday2die.culturelanddonation.MessageSender;
import org.bukkit.Bukkit;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.IOException;
import java.sql.*;
/**
 * TODO:
 * Support sqlite as well. Currently MYSQL is being used and there will be cases where mysql is not applicable.
 * So by using polymorphism I am going to select which one we are using.
 */

/**
 * A class for managing db connection
 */
public class dbConnection {
    /**
     * A method for connection to database
     * @return Connection object
     */
    public static Connection connect(){
        Connection conn = null;
        try{
            String url;
            if (ConfigValues.db_enable){
                url = "jdbc:mysql://" + ConfigValues.db_ip + "/" + ConfigValues.db_name;
            }
            else{
                File dataFolder = new File(ConfigValues.cur_path, "data.db");
                if (!dataFolder.exists()){ // if the file does not exist
                    try {
                        dataFolder.createNewFile();
                    } catch (IOException e) {
                        MessageSender.toConsole.error("Cannot generate SQLITE database file.");
                    }
                }
                url = "jdbc:sqlite:" + dataFolder;
            }

            conn = DriverManager.getConnection(url, ConfigValues.db_id, ConfigValues.db_pw);

            // create table if not exists
            String createTable1 = "CREATE TABLE IF NOT EXISTS donation_clients(username TEXT, total INT, tries INT)";
            String createTable2 = "CREATE TABLE IF NOT EXISTS donation_log(username TEXT, code TEXT, date DATETIME, amount INT)";

            Statement stmt = conn.createStatement();
            stmt.execute(createTable1);
            stmt.execute(createTable2);

        }catch(SQLSyntaxErrorException e){
            MessageSender.toConsole.error("Donation - Cannot use database : " + ConfigValues.db_name);
        }catch(SQLException e){
            MessageSender.toConsole.error("Donation - Cannot connect DB" + e);
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * A method for returning false tries of a user
     * @param username the user to check
     * @return -1 if not exists, otherwise tries.
     */
    public static int getTries(String username) {
        int tries = 0;
        Connection conn = connect();

        try {
            String SQL = "SELECT tries FROM donation_clients WHERE username=\"" + username + "\"";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            tries = rs.getInt("tries");
        } catch (SQLException e) { // when user does not exist
            tries = -1;
        }
        try{
            conn.close();
        } catch (SQLException e){
            MessageSender.toConsole.error(" cannot close db connection.");
        }
        return tries;
    }

    /**
     * A method for resetting tries of a specific user
     * @param username the user to reset tries
     */
    public static void resetTries(String username){
        int orgTries = 0;
        orgTries = getTries(username);
        Connection conn = connect();
        String SQL = "";
        if (orgTries == -1) {
            SQL = "INSERT INTO donation_clients (username, total, tries) VALUES (\"" + username + "\", 0, 0);";
        } else{
            SQL = "UPDATE donation_clients SET tries = " + Integer.toString(0) + " WHERE username = \"" + username + "\"";
        }
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that gets total donation amount from db
     * @param username the username to check
     * @return returns integer amount of money. -1 if not exists
     */
    public static int donationTotal(String username){
        int total = 0;
        Connection conn = connect();
        try {
            String SQL = "SELECT total FROM donation_clients WHERE username=\"" + username + "\"";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            total = rs.getInt("total");
        } catch (SQLException e) { // when user does not exist
            total = -1;
            total = -1;
        }
        try{
            conn.close();
        } catch (SQLException e){
            MessageSender.toConsole.error(" cannot close db connection.");
        }
        return total;

    }

    /**
     * A method for recording donation into the database of a user
     * @param username the username for donation
     * @param amount the amount of money for donation
     * @param code the code used for donation
     */
    public static void donated(String username, int amount, String code){
        int total = 0;
        total = donationTotal(username);
        Connection conn = connect();
        String SQL1 = "";
        String SQL2 = "";
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);
        if (total == -1){
            SQL1 = "INSERT INTO donation_clients (username, total, tries) VALUES (\"" + username + "\", " +
                    Integer.toString(amount) + " , 0)";
        }else{
            total = amount + total;
            SQL1 = "UPDATE donation_clients SET total = " + Integer.toString(total) + ", tries=0 WHERE username = \""
                    + username + "\"";
        }
        SQL2 = "INSERT INTO donation_log (username, code, date, amount) VALUES (\"" + username + "\", \"" +
                code + "\", \"" + currentTime + "\" , "  + Integer.toString(amount) + ")";


        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL1);
            stmt.executeUpdate(SQL2);

        }catch (SQLException e){
            e.printStackTrace();
            MessageSender.toConsole.error(" oops I fucked up.");
        }
    }

    /**
     * A method for adding a false try into the user
     * @param username the username to add a false try
     */
    public static void addTry(String username) {
        int orgTries = 0;
        orgTries = getTries(username);
        Connection conn = connect();
        String SQL = "";
        if (orgTries == -1) {
            SQL = "INSERT INTO donation_clients (username, total, tries) VALUES (\"" + username + "\", 0, 0);";
        } else{
            orgTries = orgTries + 1;
            SQL = "UPDATE donation_clients SET tries = " + Integer.toString(orgTries) + " WHERE username = \"" + username + "\"";
        }
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
