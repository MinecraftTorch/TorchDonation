package net.gooday2die.torchdonation.rDonationHandler;

import net.gooday2die.torchdonation.dbHandler.dbConnection;
import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskQueue {
    Queue<UserDonation> queue = new LinkedList<>(); // a queue to store UserDonation object
    AtomicBoolean keepRunning = new AtomicBoolean();
    Session session;

    public TaskQueue(Session session) {
        keepRunning.set(true);
        this.session = session;
        new BukkitRunnable() {
            @Override
            public void run() {
                backgroundProcess();
            }
        }.runTaskAsynchronously(ConfigValues.thisPlugin);
    }

    private void backgroundProcess() {
        while (keepRunning.get()) {
            if (queue.isEmpty()) assert true;
            else {
                UserDonation curDonation = this.dequeue();
            }
        }
    }

    /**
     * A private method that processes donation from userDonation.
     * @param userDonation The UserDonation object to process donation.
     */
    private void processDonation(UserDonation userDonation) {
        DonationHelper.Redeem redeem = new DonationHelper.Redeem(session, userDonation.giftCodeParts);
        CommandSender sender = userDonation.sender;

        try { // when it works properly
            int amount = redeem.perform();
            //RewardUser.reward(sender, ConfigValues.thisPlugin, amount);

            dbConnection.connectionAbstract con; // insert into db.
            // TODO : add polymorphism to DB.
            if (ConfigValues.useMySQL) con = new dbConnection.MysqlCon();
            else con = new dbConnection.sqliteCon();
            ;
            con.close();

        } catch (DonationHelper.redeemFailureException e) { // with exceptions
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                    sender.getName() + " 님이 후원을 실패했습니다. 사유 : " + e.getMessage());
            sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE + "후원을 실패했습니다. 사유 : " + e.getMessage());
        } catch (net.gooday2die.torchdonation.CulturelandDonation.Session.exceptions.loginFailureException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                    " 컬쳐랜드에 로그인할 수 없습니다. 아이디 비밀번호를 확인해주세요.");
            sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                    " 문화상품권 충전하며 에러가 생겼습니다. 서버 관리자에게 말씀해주세요.");
        }
    }

    /**
     * A method that stops the background thread for checking queues.
     */
    public void stopBackgroundTask() {
        keepRunning.set(false);
    }

    /**
     * A method that enqueues UserDonation object into queue.
     * @param userDonation the UserDonation object to enqueue
     */
    public void enqueue(UserDonation userDonation){
        queue.add(userDonation);
        ConfigValues.queueSize++;
    }

    /**
     * A method that dequeues UserDonation object from queue
     * @return the dequeued object from queue.
     */
    private UserDonation dequeue(){
        ConfigValues.queueSize--;
        return queue.remove();
    }
}
