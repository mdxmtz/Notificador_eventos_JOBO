package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.pagefactory.ByAll;

import java.util.*;

import java.sql.*;

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

        List<Evento> eventosScrap = new ArrayList<Evento>(); //new List<Evento>();

        for(WebElement evento : eventos){
            if(evento.getAttribute("id").contains("prod_")){
                int id = ID(evento);
                String title = title(evento);
                String date = date(evento);
                //String place = place(evento);
                int agotado = agotado(evento);
                Evento event = new Evento(id,title," ",date,agotado);
                eventosScrap.add(event);
            }
        }

        List<Evento> newEventos = newEvents(eventosScrap);
        for(Evento evento: newEventos){
            evento.showEvento();
            System.out.println("________");
        }
        driver.close();
        MailMan mailMan = new MailMan();
        //mailMan.sendEventos(newEventos,newEventos);
    }
    //List<Evento> eventosScrap

    //Ingresa en la base de datos los nuevos eventos encontrados, y devuleve un lista con los mismos
    private List<Evento> newEvents(List<Evento> eventosScrap){
        List<Evento> result = new ArrayList<Evento>();
        try{


            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventosjobo", "root", "1234");
            Statement st = conexion.createStatement();
            st.executeQuery ("SELECT * FROM eventos");
            ResultSet rs = st.getResultSet ();
            List<Evento> dbList = dbToList(rs);


            boolean add;
            System.out.println(dbList.size());
            System.out.println(eventosScrap.size());

            for(Evento evento : eventosScrap){
                add = true;
                for(Evento dbevento : dbList){
                    if(evento.getID() ==  dbevento.getID()){
                        add = false;
                        break;
                    }
                }
                if(add){
                    st.executeUpdate("INSERT INTO eventos (ID, Nombre, Fecha, Lugar, Agotado) VALUES ('"+evento.getID()+"','"+evento.getNombre()+"','"+evento.getFecha()+"','"+evento.getLugar()+"','"+evento.getAgotado()+"')");
                    result.add(evento);
                }
            }
            rs.close();
            st.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("No funcionó puto");
            return result;
        }
        return result;
    }

    //Convertimos la base de datos de eventos en una lista
    private List<Evento> dbToList(ResultSet rs){
        List<Evento> result = new ArrayList<Evento>();
        try{

            while (rs.next())
            {
                int ID = Integer.parseInt(rs.getObject("ID").toString().trim());
                String nombre = rs.getObject("Nombre").toString();
                String fecha = rs.getObject("Fecha").toString();
                String lugar = rs.getObject("Lugar").toString();
                int agotado = Integer.parseInt(rs.getObject("Agotado").toString().trim());

                Evento nEvento = new Evento(ID,nombre,fecha,lugar,agotado);
                result.add(nEvento);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    private int ID(WebElement evento){
        String[] s = evento.getAttribute("id").split("prod_");
        int result = Integer.parseInt(s[1].trim());
        return result;
    }

    private String date(WebElement evento){
        List<WebElement> h = evento.findElements(By.className("date"));
        return h.get(0).getText().trim();
    }

    private String title(WebElement evento){
        List<WebElement> h = evento.findElements(By.className("title"));
        return h.get(0).getText().trim();
    }

    private String place(WebElement evento){
        //List<WebElement> h = evento.findElements(By.className("location"));
        //return h.get(1).getText().trim();
        List<WebElement> spaces = evento.findElements(By.className("space"));
        List<WebElement> sites = evento.findElements(By.className("site"));
        String space = spaces.get(1).getText().trim();
        String site = sites.get(1).getText().trim();
        String result = space + ", " + site;

        return result;

    }

    private int agotado(WebElement evento){

        if(evento.getText().contains("Agotado"))return 1;
        return 0;
    }
}
