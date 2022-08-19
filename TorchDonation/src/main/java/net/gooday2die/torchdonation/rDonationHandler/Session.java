package net.gooday2die.torchdonation.rDonationHandler;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.gooday2die.torchdonation.ConfigValues;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session {
    private final ChromeDriver driver;
    public boolean isLoggedIn = false;

    /**
     * A constructor method for class Session.
     * This will generate ChromeDriver object and will also try to login.
     */
    public Session() {
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
        options.addArguments("\"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "+
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36\"");

        driver = new ChromeDriver(options);
    }

    /**
     * A method that gets ChromeDriver of this session.
     * @return The ChromeDriver of this session.
     */
    public ChromeDriver getDriver() {
        return driver;
    }

    /**
     * A method that loads all cookies from cookies.json.
     * @throws JSONException When getJsonObject failed.
     * @throws LoginFailureException When login failed.
     */
    public void login() throws JSONException, LoginFailureException {
        // Go over to login page for domain coherency.
        driver.get("https://m.cultureland.co.kr/mmb/loginMain.do");

        // Iterate over all cookies from JSON.
        for (int i = 0 ; i < ConfigValues.cookies.length() ; i++) {
            JSONObject jsonObject = ConfigValues.cookies.getJSONObject(i);

            if (jsonObject.getString("name").equals("KeepLoginConfig")) {
                // Store values from cookies JSON.
                String domain = jsonObject.getString("domain");
                String name = jsonObject.getString("name");
                String value = jsonObject.getString("value");
                String path = jsonObject.getString("path");
                // For expiration date, we need to convert unix epoch time to normal Date.
                // When we set "Keep logged in" for Cultureland, they give an year expiration date for the token.
                // Using that token, we are able to login to their system.
                Date expirationDate = new Date((long) Math.floor(jsonObject.getDouble("expirationDate")) * 1000);
                boolean isSecure = jsonObject.getBoolean("secure");
                boolean isHTTPOnly = jsonObject.getBoolean("httpOnly");

                // Generate Cookie instance and load values to webdriver.
                Cookie cookie = new Cookie(name, value, domain, path, expirationDate, isSecure, isHTTPOnly);
                driver.manage().addCookie(cookie);
            }
        }

        // Redirect to login page and redeem page.
        driver.get("https://m.cultureland.co.kr/mmb/loginMain.do");
        driver.get("https://m.cultureland.co.kr/csh/cshGiftCard.do");

        // This means the login failed.
        if (driver.getCurrentUrl().equals("https://m.cultureland.co.kr/mmb/loginMain.do"))
            throw new LoginFailureException();
        else
            isLoggedIn = true;
    }

    /**
     * A method that closes current session.
     * Close to exiting Chrome window.
     */
    public void close() {
        driver.close();
        driver.quit();
    }

    /**
     * A method that refreshes web browser and checks if current state is logged in.
     * @throws LoginFailureException When the account is not logged in.
     */
    public void refresh() throws LoginFailureException {
        driver.get("https://m.cultureland.co.kr/csh/cshGiftCard.do");
        // This means the login failed.
        if (driver.getCurrentUrl().equals("https://m.cultureland.co.kr/mmb/loginMain.do")) {
            isLoggedIn = false;
            throw new LoginFailureException();
        }
        else
            isLoggedIn = true;
    }

    /**
     * A public static class for Login failures.
     */
    public static class LoginFailureException extends RuntimeException {}
}
