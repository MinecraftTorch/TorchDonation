package net.gooday2die.torchdonation.CommandHandler;

import net.gooday2die.torchdonation.ConfigValues;
import net.gooday2die.torchdonation.CulturelandDonation.RewardUser;
import net.gooday2die.torchdonation.CulturelandDonation.Session;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.Pattern;

/**
 * A class for redeeming giftcards
 */
public class Redeem implements CommandExecutor {
    JavaPlugin thisPlugin;

    /**
     * A constructor method for this class
     * @param plugin the plugin object
     */
    public Redeem(JavaPlugin plugin){
        thisPlugin = plugin;
    }

    /**
     * Override this command using onCommand.
     * Redeem Giftcard using Asynchronous features.
     * @param sender the command sender
     * @param command the command
     * @param label the label
     * @param args the arguments
     * @return always will return true.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new BukkitRunnable() {
            /**
             * Override this for run method
             */
            @Override
            public void run() {
                Session dO = new Session();
                dO.generateDriver();
                dO.login(ConfigValues.username, ConfigValues.password);

                if (args.length == 1) {  // Check there is one argument.
                    String giftcardFormat = "^\\d{4}-\\d{4}-\\d{4}-\\d{4,6}$";  // regex pattern for giftcard

                    if (!(Pattern.matches(giftcardFormat, args[0]))) { // If it does not match regex.
                        sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                                sender.getName() + " 올바르지 않은 문화상품권 형식입니다.");
                        sender.sendMessage(ChatColor.GREEN + "문화상품권 예시: 1234-1234-1234-1234 또는 1234-1234-1234-123456");
                    } else {
                        try {
                            int amount = dO.redeem(args[0]);
                            RewardUser.reward(sender, thisPlugin, amount);
                        } catch (Session.exceptions.redeemFailureException e) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                                    sender.getName() + " 님이 후원을 실패했습니다. 사유 : " + e.getMessage());
                            sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                                    sender.getName() + " 후원을 실패했습니다. 사유 : " + e.getMessage());
                        } catch (Session.exceptions.loginFailureException e) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                                    " 컬쳐랜드에 로그인할 수 없습니다. 아이디 비밀번호를 확인해주세요.");
                            sender.sendMessage(ChatColor.RED + "[TorchDonation] " + ChatColor.WHITE +
                                    " 문화상품권 충전하며 에러가 생겼습니다. 서버 관리자에게 말씀해주세요.");
                        }
                    }
                }
            }
        }.runTaskAsynchronously(thisPlugin);
        return true;
    }
}
