package net.gooday2die.torchdonation.rDonationHandler;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Session {
    private final ChromeDriver driver;
    private JavascriptExecutor js;
    private boolean isLoggedIn = false;

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

    public ChromeDriver getDriver() {
        return driver;
    }

    public
}
