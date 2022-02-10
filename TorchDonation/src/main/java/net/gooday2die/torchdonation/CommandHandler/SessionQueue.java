package net.gooday2die.torchdonation.CommandHandler;

import net.gooday2die.torchdonation.ConfigValues;
import net.gooday2die.torchdonation.CulturelandDonation.RewardUser;
import net.gooday2die.torchdonation.CulturelandDonation.Session;
import net.gooday2die.torchdonation.dbHandler.dbConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-10
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die
 */

/**
 * A class for managing donation sessions by queue
 */
public class SessionQueue {
    Queue<UserDonation> q = new LinkedList<>(); // a queue to store UserDonation object

    /**
     * A method that enqueues UserDonation object into queue.
     * @param userDonation the UserDonation object to enqueue
     */
    public void enqueue(UserDonation userDonation){
        q.add(userDonation);
    }

    /**
     * A method that dequeues UserDonation object from queue
     * @return the dequeued object from queue.
     */
    public UserDonation dequeue(){
        return q.remove();
    }

    /**
     * A method that runs the head UserDonation object.
     */
    public void run() {
        ConfigValues.queueSize++; // add queuesize

        UserDonation curUserDonation = dequeue();
        String code = curUserDonation.getCode();
        CommandSender sender = curUserDonation.getSender();

        Session donationSession = new Session();
        donationSession.generateDriver();
        donationSession.login(ConfigValues.username, ConfigValues.password);

        try { // when it works properly
            int amount = donationSession.redeem(code);
            RewardUser.reward(sender, ConfigValues.thisPlugin, amount);

            dbConnection.connectionAbstract con; // insert into db.
            if (ConfigValues.useMySQL) con = new dbConnection.MysqlCon();
            else con = new dbConnection.sqliteCon();
            con.donated(sender.getName(), code, amount);
            con.close();

        } catch (Session.exceptions.redeemFailureException e) { // with exceptions
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                    sender.getName() + " 님이 후원을 실패했습니다. 사유 : " + e.getMessage());
            sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE + "후원을 실패했습니다. 사유 : " + e.getMessage());
        } catch (Session.exceptions.loginFailureException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                    " 컬쳐랜드에 로그인할 수 없습니다. 아이디 비밀번호를 확인해주세요.");
            sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                    " 문화상품권 충전하며 에러가 생겼습니다. 서버 관리자에게 말씀해주세요.");
        }

        ConfigValues.queueSize--;
        donationSession.end();
    }
}
