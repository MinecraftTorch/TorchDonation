package net.gooday2die.torchdonation.CulturelandDonation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {
    ChromeDriver driver;
    ChromeDriver generateDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Add some arguments that is headless but pretends to be a real web browser
        //options.addArguments("--disable-gpu");
        //options.addArguments("\"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "+
        //        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36\"");
        //options.addArguments("headless");

         driver = new ChromeDriver(options);

        return driver;
    }

    private class login {
        public Map<String, List<String>> generateKeyboard(ChromeDriver driver){
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            // From here Generate lowercase and uppercase keys
            for(int i = 0 ; i < 3; i++){ // from 1st row to 3rd row
                for(int j = 1 ; j < 12 ; i++){
                    String jsCommand = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row" + i +
                            "\"]/div[" + j + "]")).getAttribute("onmousedown");
                    String altValue = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row" + i +
                            "\"]/div[" + j + "]/div/img")).getAttribute("alt");

                    List<String> lowerCommandList = new ArrayList<>();
                    List<String> upperCommandList = new ArrayList<>();

                    lowerCommandList.add(jsCommand);

                    upperCommandList.add("mtk.cap(event, this);");
                    upperCommandList.add(jsCommand);
                    upperCommandList.add("mtk.cap(event, this);");

                    map.put(altValue.toUpperCase(), upperCommandList);
                    map.put(altValue, lowerCommandList);

                }
            }
            for(int i = 2 ; i < 10 ; i++){ // just 4th row
                String jsCommand = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row3" +
                        "\"]/div[" + i + "]")).getAttribute("onmousedown");
                String altValue = driver.findElement(By.xpath("//*[@id=\"mtk_passwd_Row3" +
                        "\"]/div[" + i + "]/div/img")).getAttribute("alt");

                List<String> lowerCommandList = new ArrayList<>();
                List<String> upperCommandList = new ArrayList<>();

                lowerCommandList.add(jsCommand);

                upperCommandList.add("mtk.cap(event, this);");
                upperCommandList.add(jsCommand);
                upperCommandList.add("mtk.cap(event, this);");

                map.put(altValue.toUpperCase(), upperCommandList);
                map.put(altValue, lowerCommandList);
            } // finished Generating lowercase and uppercase keys

            // Generate special keys

            return map;
        }
    }

    private char translateKey(String koreanString){
        if (koreanString.equals("어금기호")) return '`';
        else if (koreanString.equals("물결표시")) return '~';
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        else if (koreanString.equals("")) return ;
        elif word == "느낌표":
        word = '!'
        elif word == "골뱅이":
        word = '@'
        elif word == "샾":
        word = '#'
        elif word == "달러기호":
        word = '$'
        elif word == "퍼센트":
        word = '%'
        elif word == "꺽쇠":
        word = '^'
        elif word == "엠퍼샌드":
        word = '&'
        elif word == "별표":
        word = '*'
        elif word == "왼쪽괄호":
        word = '('
        elif word == "오른쪽괄호":
        word = ')'
        elif word == "빼기":
        word = '-'
        elif word == "밑줄":
        word = '_'
        elif word == "등호":
        word = '='
        elif word == "더하기":
        word = '+'
        elif word == "왼쪽대괄호":
        word = '['
        elif word == "왼쪽중괄호":
        word = '{'
        elif word == "오른쪽대괄호":
        word = ']'
        elif word == "오른쪽중괄호":
        word = '}'
        elif word == "역슬래시":
        word = '\\'
        elif word == "수직막대":
        word = '|'
        elif word == "세미콜론":
        word = ';'
        elif word == "콜론":
        word = ':'
        elif word == "슬래시":
        word = '/'
        elif word == "물음표":
        word = '?'
        elif word == "쉼표":
        word = ','
        elif word == "왼쪽꺽쇠괄호":
        word = '<'
        elif word == "마침표":
        word = '.'
        elif word == "오른쪽꺽쇠괄호":
        word = '>'
        elif word == "작은따옴표":
        word = '\''
        elif word == "따옴표":
        word = '\"'
        elif word == "더하기":
        word = '+'
        elif word == "빼기":
        word = '-'
        elif word == "별표":
        word = '{'
        elif word == "슬래시":
        word = '/'
        else:
        word = 'None'
    }

    boolean login(String username, String password){
        driver.get("https://m.cultureland.co.kr/mmb/loginMain.do");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("$(\"#txtUserId\")[0].value = \"" + username + "\";"); //Scroll vertically down by 1000 pixels
        driver.findElement(By.xpath("//*[@id=\"passwd\"]")).click();
        return true;
    }

    void stopDriver(){
        driver.quit();
    }
}
