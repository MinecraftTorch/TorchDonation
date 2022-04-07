package net.gooday2die.torchdonation.CulturelandDonation;

/**
 * Torch Donation Plugin
 * Edited Date : 2022-02-09
 * DO NOT REMOVE MESSAGE PREFIXES OF THIS PLUGIN
 *
 * @author Gooday2die @ https://github.com/gooday2die/TorchDonation
 */

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session {
    static ChromeDriver driver;
    static JavascriptExecutor js;
    public ChromeDriver generateDriver() {
        WebDriverManager.chromedriver().setup();

        Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.silentLogging", "true");
        System.setProperty("webdriver.chrome.verboseLogging", "false");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        ChromeOptions options = new ChromeOptions();
        // Add some arguments that is headless but pretends to be a real web browser
        options.addArguments("--log-level=3");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox"); // Bypass OS security model
        options.addArguments("--disable-dev-shm-usage"); // Bypass OS security model
        options.addArguments("headless");
        //options.addArguments("\"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "+
        //        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36\"");

        driver = new ChromeDriver(options);

        return driver;
    }

    /**
     * A function that logs into the system
     * @param username username to login as
     * @param password password to use while logging in
     * @return returns true if the login was successful
     */
    public boolean login(String username, String password){
        login l = new login();
        l.perform(username, password);
        return true;
    }

    /**
     * A function that redeems the code
     * @param code the code to redeem
     * @return returns amount of money redeemed
     */
    public int redeem(String code) throws exceptions.loginFailureException, exceptions.redeemFailureException  {
        try {
            driver.get("https://m.cultureland.co.kr/csh/cshGiftCard.do");
            redeem r = new redeem();
            int amount = r.perform(code);
            return amount;
        } catch (org.openqa.selenium.JavascriptException e){
            e.printStackTrace();
            throw new exceptions.loginFailureException("LoginFailure");
        }
    }

    /**
     * A method that closes and ends current driver session.
     */
    public void end(){
        driver.quit();
    }

    /**
     * A class for logging in
     */
    private class login {
        Map<String, Character> specialCharMap = generateCharMap();
        /**
         * A method that performs login process all together
         * @param username the username to login as
         * @param password the password to login with
         * @return returns true if successful
         */
        private boolean perform(String username, String password){
            driver.get("https://m.cultureland.co.kr/mmb/loginMain.do");
            js = (JavascriptExecutor) driver;
            js.executeScript("$(\"#txtUserId\")[0].value = \"" + username + "\";"); //Scroll vertically down by 1000 pixels
            driver.findElement(By.xpath("//*[@id=\"passwd\"]")).click();
            enterPassword(password);
            return true;
        }

        /**
         * A method that generates virtual keyboard and js command mapping with nKEY protect
         * Raonsoft gave us a bit of hint when it comes to the commands and original virtual key mapping.
         * They left "ALT" value of /div/img/ of each keys with the original key values.
         * Meaning that if we can get "alt" value of img of the button, it will show the original keys.
         * Example: MAP<'a', <mtk. js commands>>
         * @return returns Map<Character, List<String>> object that contains info
         */
        private Map<Character, List<String>> generateKeyboard(){
            Map<Character, List<String>> map = new HashMap<Character, List<String>>();
            // From here Generate lowercase and uppercase keys
            for(int i = 0 ; i < 3; i++){ // from 1st row to 3rd row
                for(int j = 1 ; j < 12 ; j++){
                    String jsCommand = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row" + i +
                            "\"]/div[" + j + "]")).getAttribute("onmousedown");
                    Character altValue = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row" + i +
                            "\"]/div[" + j + "]/div/img")).getAttribute("alt").charAt(0);

                    List<String> lowerCommandList = new ArrayList<>();
                    List<String> upperCommandList = new ArrayList<>();

                    lowerCommandList.add(jsCommand);

                    upperCommandList.add("mtk.cap(event, this);");
                    upperCommandList.add(jsCommand);
                    upperCommandList.add("mtk.cap(event, this);");

                    map.put(Character.toUpperCase(altValue), upperCommandList);
                    map.put(altValue, lowerCommandList);
                }
            }
            for(int i = 2 ; i < 10 ; i++){ // just 4th row
                String jsCommand = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row3" +
                        "\"]/div[" + i + "]")).getAttribute("onmousedown");
                Character altValue = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row3" +
                        "\"]/div[" + i + "]/div/img")).getAttribute("alt").charAt(0);

                List<String> lowerCommandList = new ArrayList<>();
                List<String> upperCommandList = new ArrayList<>();

                lowerCommandList.add(jsCommand);

                upperCommandList.add("mtk.cap(event, this);");
                upperCommandList.add(jsCommand);
                upperCommandList.add("mtk.cap(event, this);");

                map.put(Character.toUpperCase(altValue), upperCommandList);
                map.put(altValue, lowerCommandList);
            } // finished Generating lowercase and uppercase keys

            js.executeScript("mtk.sp(event, this);");
            // Generate special keys
            for(int i = 0 ; i < 3; i++){ // from 1st row to 3rd row
                for(int j = 1 ; j < 12 ; j++){
                    String jsCommand = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row" + i +
                            "\"]/div[" + j + "]")).getAttribute("onmousedown");
                    String  altValue = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row" + i +
                            "\"]/div[" + j + "]/div/img")).getAttribute("alt");

                    List<String> commandList = new ArrayList<>();
                    char specialChar = translateKey(altValue);

                    commandList.add("mtk.sp(event, this);");
                    commandList.add(jsCommand);
                    commandList.add("mtk.sp(event, this);");

                    map.put(specialChar, commandList);

                }
            }
            for(int i = 2 ; i < 10 ; i++){ // just 4th row
                String jsCommand = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row3" +
                        "\"]/div[" + i + "]")).getAttribute("onmousedown");
                String altValue = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row3" +
                        "\"]/div[" + i + "]/div/img")).getAttribute("alt");

                List<String> commandList = new ArrayList<>();
                char specialChar = translateKey(altValue);

                commandList.add("mtk.sp(event, this);");
                commandList.add(jsCommand);
                commandList.add("mtk.sp(event, this);");

                map.put(specialChar, commandList);
            } // finished Generating special characters
            js.executeScript("mtk.sp(event, this);");

            return map;
        }

        /**
         * A method that generates special character map
         * Raonsoft used some weird method that stores special letters translated into korean.
         * So this method generates mapping that contains all korean -> to original special character
         * @return returns special character
         */
        private Map<String, Character> generateCharMap(){
            Map<String, Character> specialCharMap = new HashMap<String, Character>();
            specialCharMap.put("어금기호", '`');
            specialCharMap.put("물결표시", '~');
            specialCharMap.put("느낌표", '!');
            specialCharMap.put("골뱅이", '@');
            specialCharMap.put("샾", '#');
            specialCharMap.put("달러기호", '$');
            specialCharMap.put("퍼센트", '%');
            specialCharMap.put("꺽쇠", '^');
            specialCharMap.put("엠퍼샌드", '&');
            specialCharMap.put("별표", '*');
            specialCharMap.put("왼쪽괄호", '(');
            specialCharMap.put("오른쪽괄호", ')');
            specialCharMap.put("빼기", '-');
            specialCharMap.put("밑줄", '_');
            specialCharMap.put("등호", '=');
            specialCharMap.put("왼쪽대괄호", '[');
            specialCharMap.put("왼쪽중괄호", '{');
            specialCharMap.put("오른쪽대괄호", ']');
            specialCharMap.put("오른쪽중괄호", '}');
            specialCharMap.put("역슬래시", '\\');
            specialCharMap.put("세미콜론", ';');
            specialCharMap.put("수직막대", '|');
            specialCharMap.put("콜론", ':');
            specialCharMap.put("슬래시", '/');
            specialCharMap.put("물음표", '?');
            specialCharMap.put("쉼표", ',');
            specialCharMap.put("왼쪽꺽쇠괄호", '<');
            specialCharMap.put("마침표", '.');
            specialCharMap.put("오른쪽꺽쇠괄호", '>');
            specialCharMap.put("작은따옴표", '\'');
            specialCharMap.put("따옴표", '\"');
            specialCharMap.put("더하기", '+');

            return specialCharMap;
        }

        /**
         * A method that translates korean String into special characters
         * @param koreanString the korean string to translate into special characters
         * @return returns the translated special characters
         */
        private char translateKey(String koreanString){
            try {
                return specialCharMap.get(koreanString);
            } catch (NullPointerException e){
                return ' ';
            }
        }

        /**
         * A method that enters password by using the virtual keyboard mapping that we generated
         * @param password the password to enter
         */
        private void enterPassword(String password){
            Map<Character, List<String>> keyboardDict = generateKeyboard();
            for(int i = 0 ; i < password.length() ; i++){
                List<String> commands = keyboardDict.get(password.charAt(i));
                for(String command : commands){
                    js.executeScript(command);
                }
            }
            //js.executeScript("mtk.done(event, this);");
            driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/form/fieldset/span[1]/input")).click();
            driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/form/fieldset/input")).click();
        }
    }

    /**
     * A class that redeems code
     */
    public class redeem {
        /**
         * A method that actually performs redeeming code.
         * Code must be in one of those following formats
         * 1234-1234-1234-1234
         * 1234-1234-1234-123456
         * @param code the code to redeem
         * @return returns total amount of money that was redeemed
         */
        public int perform(String code) throws exceptions.redeemFailureException {
            enterKeys(code);
            String amountCharged = "";

            for (int i = 0 ; i < 10 ; i++) {
                try {
                    amountCharged = driver.findElement(By.xpath("//*[@id=\"wrap\"]/div[" + i + "]/section/dl/dd")).getText();
                } catch (NoSuchElementException ignored){
                }
                if (amountCharged.length() != 0) break;
            }

            amountCharged = amountCharged.replace("원", "");
            amountCharged = amountCharged.replace(",", "");

            String errorMessage = "";
            for (int i = 0 ; i < 10 ; i++){
                try{
                    errorMessage = driver.findElement(By.xpath("//*[@id=\"wrap\"]/div[" + i + "]/section/div/table/tbody/tr/td[3]/b")).getText();
                } catch (NoSuchElementException ignored){
                }
                if (!errorMessage.isEmpty()) throw new exceptions.redeemFailureException(errorMessage);
            }
            return Integer.parseInt(amountCharged);
        }

        /**
         * A method that parses code and turns them into String list
         * ex) 1234-1234-1234-1234 -> [1234, 1234, 1234, 1234]
         * @param code the code to parse
         * @return parsed code in String[]
         */
        private String[] parseCode(String code){
            String[] a = new String[4];
            a[0] = code.substring(0, 4);
            a[1] = code.substring(5, 9);
            a[2] = code.substring(10, 14);
            a[3] = code.substring(15, code.length());

            return a;
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
         * A method that enters all keys using JS commands
         * @param code the code to enter
         */
        private void enterKeys(String code){
            String[] codes = parseCode(code);
            js.executeScript("$(\"#txtScr11\")[0].value = \"" + codes[0] + "\";");
            js.executeScript("$(\"#txtScr12\")[0].value = \"" + codes[1] + "\";");
            js.executeScript("$(\"#txtScr13\")[0].value = \"" + codes[2] + "\";");
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
                js.executeScript(curCommand);
            }
            //try { // weird case, there are some case where 'done' is missing.
            //    js.executeScript("mtk.done(event, this);");
            //} catch (JavascriptException e){ // if the script is not executable, just click the login button
                driver.findElement(By.xpath("/html/body/div[1]/div[6]/form/a")).click();
            //}
        }
    }
    public static class exceptions{
        public static class loginFailureException extends Exception{
            loginFailureException(String errorMessage){
                super(errorMessage);
            }
        }

        public static class redeemFailureException extends Exception{
            redeemFailureException(String errorMessage){
                super(errorMessage);
            }
        }
    }
}
