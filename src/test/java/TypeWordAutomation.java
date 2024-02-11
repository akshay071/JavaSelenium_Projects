import java.awt.AWTException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class TypeWordAutomation {
    String driverPath = "D:/Automation Practice/AutomationLearn/Drivers/chromedriver.exe";
    public WebDriver driver;




    @Test
    public void testWords() throws AWTException, InterruptedException {
        // set the system property for Chrome driver
        System.setProperty("webdriver.chrome.driver", driverPath);

        // Create driver object for CHROME browser
        driver = new ChromeDriver();
        driver.get("https://monkeytype.com/");
        driver.manage().window().maximize();

        Actions actions = new Actions(driver);
        Action action = actions.build();
        WebElement element_Word = driver.findElement(By.xpath("//div[@id='words']"));


        //Accept Cookies
        driver.findElement(By.xpath("//div[@class='buttons']/button[@class='active acceptAll']")).click();
        Thread.sleep(5000);

        //find web elements and extract text displayed on screen
        String words = element_Word.getText();
        //converting extracted words in to paragraph or block of text
        StringBuilder block = new StringBuilder();
        String[] para = words.split("\\n");
        for (String s : para) {
            block.append(s).append(" ");
        }
        System.out.println("Original Text Block: ");
        System.out.println(block);
        Thread.sleep(5000);

        //writing text on screen
        boolean flag = true;
        while(flag) {
            try {
                Thread.sleep(2000);

                actions.moveToElement(element_Word);
                actions.click(element_Word);
                action.perform();
                actions.sendKeys(block.toString()).perform();

                //rewrite block to avoid string overlapping
                Thread.sleep(5000);
                String old_block = block.toString();
                StringBuilder search_String = new StringBuilder();
                String[] searchText = old_block.split(" ");
                String[] lastTenWords = Arrays.copyOfRange(searchText, searchText.length - 10, searchText.length);
                for (String word : lastTenWords) {
                    search_String.append(word).append(" ");
                }

                System.out.println("Last 10 word SearchString: ");
                System.out.println(search_String);

                driver.manage().timeouts().pageLoadTimeout(5,TimeUnit.SECONDS);
                words = element_Word.getText();
                String[] para1;
                para1 = words.split("\\n");
                for (String s : para1) {
                    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                    block.append(s).append(" ");
                }
                block = new StringBuilder(block.toString().trim());
                System.out.println("New Block: ");

                //remove overblocking
                StringBuilder buf;
                buf = new StringBuilder(block.toString());
                int start = 0;
                int end =block.indexOf(search_String.toString()) + (search_String.length()-1);
                block = new StringBuilder(String.valueOf(buf.replace(start, end, "")));

                block = new StringBuilder(block.toString().trim());
                System.out.println("Block after 1 iteration replacement: ");
                System.out.println(block);

            } catch (Exception e) {
                System.out.println(e);
                flag = false;
            }
        }





    }
}
