package net.gooday2die.torchdonation.DonationHandler;

import net.gooday2die.torchdonation.ConfigValues;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A class that is for helping donation actions.
 */
public class DonationHelper {
    /**
     * A class that redeems giftcode.
     */
    public static class Redeem {
        private final Session session;
        private final ChromeDriver driver;
        private final String[] codes;

        /**
         * A constructor method for Redeem
         * @param session The Session that we are using to donate.
         * @param codes The String[] of codes.
         */
        public Redeem(Session session, String[] codes) {
            this.session = session;
            this.codes = codes;
            this.driver = this.session.getDriver();
        }

        /**
         * A method that actually performs redeeming code.
         * Code must be in one of those following formats
         * 1234-1234-1234-1234
         * 1234-1234-1234-123456
         * @return returns total amount of money that was redeemed
         * @throws redeemFailureException When redeeming code failed (with reason as well).
         * @throws Session.LoginFailureException When logging into system failed.
         */
        public int perform() throws redeemFailureException, Session.LoginFailureException {
            try { // Try refreshing page.
                session.refresh();
            } catch (Session.LoginFailureException ex1) { // If login failed, try logging in again.
                try {
                    session.login();
                    session.refresh();
                } catch (Session.LoginFailureException | JSONException ex2) { // If that failed again, throw Exception.
                    throw new Session.LoginFailureException();
                }
            }

            this.enterKeys(); // Enter code.
            String amountCharged = "";

            // Parse result values
            // From string price원 to int price.

            for (int i = 0 ; i < 10 ; i++) {
                try {
                    amountCharged = driver.findElement(By.xpath("//*[@id=\"wrap\"]/div[" + i + "]/section/dl/dd")).getText();
                } catch (NoSuchElementException ignored) {}
                if (amountCharged.length() != 0) break;
            }

            amountCharged = amountCharged.replace("원", "");
            amountCharged = amountCharged.replace(",", "");

            int amount;
            boolean isSuccessful;

            try { // Try parsing amount charged.
                amount = Integer.parseInt(amountCharged);
                isSuccessful = amount != 0; // Check if this was successful.
            } catch (NumberFormatException e) { // If not parsable or amount was 0, it failed.
                isSuccessful = false;
                amount = 0;
            }

            if (isSuccessful) return amount;
            else { // If not successful,
                String errorMessage = "";
                for (int i = 0 ; i < 10 ; i++){
                    try{
                        errorMessage = driver.findElement(By.xpath("//*[@id=\"wrap\"]/div[" + i + "]/section/div/table/tbody/tr/td[3]/b")).getText();
                    } catch (NoSuchElementException ignored) {}
                    if (!errorMessage.isEmpty()) throw new redeemFailureException(errorMessage);
                }
                return 0; // This shall never happen.
            }
        }

        /**
         * A method that generates virtual keyboard mapping for last 6 digits
         * @return returns Map<Character, String> object of virtual mappings
         */
        private Map<Character, String> generateMap(){
            Map<Character, String> map = new HashMap<Character, String>();
            driver.findElement(By.xpath("//*[@id=\"txtScr14\"]")).click();
            driver.findElement(By.xpath("// *[ @ id = \"txtScr14\"]")).click();

            for (int i = 0 ; i < 2 ; i++){
                for (int j = 1 ; j < 7 ; j++){
                    String jsCommand = driver.findElement(By.xpath("//*[@id=\"mtk_txtScr14_Row" + i +
                            "\"]/div[" + j + "]")).getAttribute("onmousedown");
                    Character altValue = driver.findElement(By.xpath("//*[@id=\"mtk_txtScr14_Row" + i +
                            "\"]/div[" + j + "]/div/img")).getAttribute("alt").charAt(0);

                    map.put(altValue, jsCommand);
                }
            }
            return map;
        }

        /**
         * A private method that enters gift code to redeeming forms.
         */
        private void enterKeys(){
            driver.executeScript("$(\"#txtScr11\")[0].value = \"" + codes[0] + "\";");
            driver.executeScript("$(\"#txtScr12\")[0].value = \"" + codes[1] + "\";");
            driver.executeScript("$(\"#txtScr13\")[0].value = \"" + codes[2] + "\";");
            try { // Sleep 1 seconds, if this is too fast there will be a minor bug happening.
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            enterLastDigits(codes[3]);
        }

        /**
         * A method that enters last 4 or 6 digits using using generateMap method
         * @param lastDigits the last digits to input
         */
        private void enterLastDigits(String lastDigits) {
            Map<Character, String> map = generateMap();
            for (int i = 0; i < lastDigits.length(); i++) {
                String curCommand = map.get(lastDigits.charAt(i));
                driver.executeScript(curCommand);
            }
            driver.findElement(By.xpath("/html/body/div[1]/div[6]/form/a")).click();
        }
    }

    public static void reward(CommandSender user, int amount){
        // Notify console and the user that user is being rewarded
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급중입니다.");
        user.sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + "후원 보상을 지급중입니다...");

        // Iterate over all reward commands and execute them.
        for (String command : ConfigValues.rewardCommands) {
            command = command.replaceAll("%amount%", Integer.toString(amount));
            String finalCommand = command.replaceAll("%user%", user.getName());
            try { // Use sync method since some might need to be called from main thread.
                Bukkit.getScheduler().callSyncMethod(ConfigValues.thisPlugin, () ->
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand)).get();
            } catch (ExecutionException | InterruptedException e) { // If exception occurred let user know.
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급할 수 없습니다.");
                user.sendMessage(ChatColor.RED + "[TorchDonation] " +
                        ChatColor.WHITE + "후원 보상을 지급할 수 없습니다. 관리자에게 문의해주세요.");
            }
        }

        // If the rewarding process is done successfully, notify them
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + user.getName() + " 님에게 후원 보상을 지급했습니다.");
        user.sendMessage(ChatColor.GOLD + "[TorchDonation] " +
                ChatColor.WHITE + "후원 보상을 지급했습니다.");

        // If broadcasting donation was enabled, broadcast the message.
        if (ConfigValues.broadcastDonation) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[TorchDonation] " + ChatColor.GREEN + user.getName()
                    + ChatColor.WHITE + " 님이 " + ChatColor.GREEN + amount + ChatColor.WHITE + " 원을 후원하셨습니다!");
        }
    }

    /**
     * A public static class for exception when redeeming gift card failed.
     */
    public static class redeemFailureException extends Exception{
        redeemFailureException(String errorMessage){
            super(errorMessage);
        }
    }
}
