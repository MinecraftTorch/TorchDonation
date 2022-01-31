package net.gooday2die.culturelanddonation.donationProcessing;

import net.gooday2die.culturelanddonation.ConfigValues;
import net.gooday2die.culturelanddonation.MessageSender;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.regex.Pattern;

/**
 * A class for Command Executor of plugin Donate
 */
public class ClandDonate implements CommandExecutor {
    static JavaPlugin plugin;

    /**
     * A constructor method for this class
     * @param argPlugin the plugin instance object
     */
    public ClandDonate(JavaPlugin argPlugin){
        plugin = argPlugin;
    }

    /**
     * An overriding for onCommand
     * @param sender the sender
     * @param command the command
     * @param label the label
     * @param args the args
     * @return always true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        dbConnection.addTry(sender.getName());
        if (args.length == 0){
            MessageSender.toUser.error(sender, ChatColor.GREEN + "/" + label +
                    " 1234-1234-1234-1234" + ChatColor.WHITE + " 로 후원을 하실 수 있습니다");
            return true;
        }
        redeemAsync(plugin, sender, command, label, args);
        return true;
    }

    /**
     * A method for redeeming the gift card asynchronously.
     * @param instance the plugin instance
     * @param sender the sender
     * @param command the command
     * @param label the label
     * @param args the args
     */
    public static void redeemAsync(JavaPlugin instance, CommandSender sender,
                                   Command command, String label, String[] args){
        new BukkitRunnable() {
            /**
             * Override this for run method
             */
            @Override
            public void run() {
                if (!apiConnection.isConnected()) { // When the API server is down
                    MessageSender.toUser.error(sender, "잠시후 다시 시도해주세요");
                    MessageSender.toConsole.error("ClandAPI Server is down...");
                }
                else { // If the API server is up and running
                    if (args.length == 1) {  // Check there is one argument.
                        String giftcardFormat = "^\\d{4}-\\d{4}-\\d{4}-\\d{4,6}$";  // regex pattern for giftcard

                        if (!(Pattern.matches(giftcardFormat, args[0]))) { // If it does not match regex.
                            MessageSender.toUser.error(sender, "문화상품권 번호를 확인해주세요!");
                            sender.sendMessage(ChatColor.GREEN + "문화상품권 예시: 1234-1234-1234-1234 또는 1234-1234-1234-123456");
                            MessageSender.toConsole.error(sender.getName() + " entered a wrong format code");
                        } else if (dbConnection.getTries(sender.getName()) > 3){
                            MessageSender.toUser.error(sender, "문화상품권 핀번호를 너무 많이 틀리셨습니다. 후원이 제한되어있습니다. 관리자에게 문의해주세요.");
                        }
                        else { // If it at least matches the regex. Do real request.
                            MessageSender.toUser.info(sender, "후원을 처리중입니다. 잠시만 기다려주세요...");
                            MessageSender.toConsole.info(sender.getName() + " is redeeming code : " + args[0]);
                            httpResponse response = apiConnection.redeemCode(args[0]);
                            // This is the part where it really requests redeem to API

                            int ChargedAmount = 0; // init them with init values.
                            String errorReason = "null";

                            if (response.responseCode == 200) { // if response was 200. which was successfully donated.
                                ChargedAmount = Integer.parseInt(response.responseText); // get amount donated
                            } else {
                                errorReason = errorMessages.restAPI(response.responseCode); // get error Reason
                            }

                            if (ChargedAmount > 0) { // If donation was successful
                                MessageSender.toConsole.info(sender.getName() + " donated " + ChargedAmount + " KRW");
                                MessageSender.toAdmin.info(sender.getName() + "님이 " + ChatColor.GREEN +
                                        ChargedAmount + ChatColor.WHITE + "원을 후원하셨습니다");
                                MessageSender.toUser.info(sender, ChatColor.GREEN +
                                        Integer.toString(ChargedAmount) + ChatColor.WHITE +
                                        "원을 성공적으로 후원하였습니다. 감사합니다!");

                                if (ConfigValues.broadcast)
                                    sender.getServer().broadcastMessage(ChatColor.GOLD + "[알림] " + ChatColor.BLUE +
                                        sender.getName() + ChatColor.WHITE + "님이 " +
                                        ChatColor.GREEN + ChargedAmount +
                                        ChatColor.WHITE + "원을 후원하셨습니다!");
                                rewardUser.reward(sender, plugin, ChargedAmount); // reward the user by config settings
                                dbConnection.donated(sender.getName(), ChargedAmount, args[0]);
                            } // Broadcast to server
                            else{ // If donation was not successful, show response
                                MessageSender.toConsole.error(sender.getName()
                                        + " failed to redeem code due to :" + errorReason);
                                MessageSender.toAdmin.error(sender.getName() +
                                        " 님이 후원을 실패했습니다. 에러 사유 :" + errorReason);
                                MessageSender.toUser.error(sender,"상품권 등록을 실패했습니다.");
                                sender.sendMessage(ChatColor.RED + "사유 : " + errorReason);
                            }
                        }
                    }
                }
            }
        }.runTaskAsynchronously(instance); // run this asynchronous
    }
}

