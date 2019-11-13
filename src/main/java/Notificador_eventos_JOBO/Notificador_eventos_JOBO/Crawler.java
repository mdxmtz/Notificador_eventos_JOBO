package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.pagefactory.ByAll;

import java.util.*;

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
        List<Evento> eventosScrap = new ArrayList<Evento>(); //new List<Evento>();

        for(WebElement evento : eventos){
            if(evento.getAttribute("id").contains("prod_")){
                int id = ID(evento);
                String title = title(evento);
                String date = date(evento);
                String place = place(evento);
                Boolean agotado = agotado(evento);
                Evento event = new Evento(id,title,date,place,agotado);
                eventosScrap.add(event);
            }
        }
        for(Evento evento: eventosScrap){
            evento.showEvento();
            System.out.println("________");
        }
        driver.close();
    }

    public int ID(WebElement evento){
        String[] s = evento.getAttribute("id").split("prod_");
        int result = Integer.parseInt(s[1].trim());
        return result;
    }

    public String date(WebElement evento){
        List<WebElement> h = evento.findElements(By.className("date"));
        return h.get(0).getText().trim();
    }

    public String title(WebElement evento){
        List<WebElement> h = evento.findElements(By.className("title"));
        return h.get(0).getText().trim();
    }

    public String place(WebElement evento){
        //List<WebElement> h = evento.findElements(By.className("location"));
        //return h.get(1).getText().trim();
        List<WebElement> spaces = evento.findElements(By.className("space"));
        List<WebElement> sites = evento.findElements(By.className("site"));
        String space = spaces.get(1).getText().trim();
        String site = sites.get(1).getText().trim();
        String result = space + ", " + site;

        return result;

    }

    public Boolean agotado(WebElement evento){
        if(evento.getText().contains("Agotado"))return true;
        return false;
    }
}
