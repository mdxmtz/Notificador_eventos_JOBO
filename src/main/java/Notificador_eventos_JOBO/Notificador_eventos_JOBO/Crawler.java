package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.pagefactory.ByAll;

import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private String url = "https://madridcultura-jobo.shop.secutix.com/account/login";

    public void start(){
        start(url);
    }

    private void start(String url){

        System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get(url);

        WebElement username = driver.findElementById("login");
        username.sendKeys("pablosky301@gmail.com");

        WebElement password = driver.findElementById("password");
        password.sendKeys("3sF5qq417");

        WebElement button = driver.findElementById("continue_button");
        button.click();

        WebElement button2 = driver.findElementById("account_cart_button");
        button2.click();

        WebElement button3 = driver.findElementById("addOtherProducts");
        button3.click();

        List<WebElement> eventos = driver.findElementByClassName("group_content").findElements(By.cssSelector("*"));

        //List<WebElement> eventos = driver.findElements(By.xpath("//input[@data-product-type='EVENT']"));
        //List<WebElement> eventos = driver.findElements(By.cssSelector("input[type=EVENT]"));

        System.out.println( eventos.isEmpty());

        for(WebElement evento : eventos){
            if(evento.getAttribute("id").contains("prod_")){
                System.out.println(evento.getText());
                System.out.println("---------------");
            }
        }
        /*driver.get("http://demo.guru99.com/");
        WebElement element=driver.findElement(By.xpath("//input[@name='emailid']"));
        element.sendKeys("abc@gmail.com ");

        WebElement button=driver.findElement(By.xpath("//input[@name='btnLogin']"));
        button.click();*/
    }
}
