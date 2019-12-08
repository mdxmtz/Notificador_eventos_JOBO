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

        Tree eventosScrap = new Tree();

        for(WebElement evento : eventos){
            if(evento.getAttribute("id").contains("prod_")){
                int id = ID(evento);
                String title = title(evento);
                String date = date(evento);
                String place = place(evento);
                int agotado = agotado(evento);
                Evento event = new Evento(id,title,place,date,agotado);
                eventosScrap.insert(event);
            }
        }

        List<Evento> newEventos = newEvents(eventosScrap);
        for(Evento evento: newEventos){
            evento.showEvento();
            System.out.println("________");
        }
        driver.close();
        MailMan mailMan = new MailMan();
        mailMan.sendEventos(newEventos,newEventos);
    }

    //Ingresa en la base de datos los nuevos eventos encontrados, y devuleve un lista con los mismos
    private List<Evento> newEvents(Tree eventosScrap){
        List<Evento> result = new ArrayList<Evento>();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventosjobo", "root", "1234");
            Statement st = conexion.createStatement();
            st.executeQuery ("SELECT * FROM eventos");
            ResultSet rs = st.getResultSet ();
            Tree dbTree = dbToTree(rs);

            result = compareTrees(eventosScrap.root,dbTree.root,result,st);

            rs.close();
            st.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("No funcionó puto");
            return result;
        }
        return result;
    }

    //Compara los eventos scrapeados con los existentes en la base de datos
    //Devuelve una lista de eventos con aquellos que no estén en la base de datos
    public List<Evento> compareTrees(Node sNode, Node dbNode, List<Evento> result,  Statement st){
        if(sNode.left != null)compareTrees(sNode.left,dbNode,result,st);
        boolean exist = eventoExist(sNode.key,dbNode);
        if(!exist) {
            try{
                result.add(sNode.getEvento());
                st.executeUpdate("INSERT INTO eventos (ID, Nombre, Fecha, Lugar, Agotado) VALUES ('"+sNode.getEvento().getID()+"','"+sNode.getEvento().getNombre()+"','"+sNode.getEvento().getFecha()+"','"+sNode.getEvento().getLugar()+"','"+sNode.getEvento().getAgotado()+"')");
            }catch (Exception e){
                System.out.println("iora");
            }
        }
        if(sNode.right !=null)compareTrees(sNode.right,dbNode,result,st);

        return result;
    }

    //Comprueba si existe el evento en la base da datos
    public boolean eventoExist (int key, Node dbNode){
        if(dbNode == null)return false;
        if(key==dbNode.key)return true;
        if(key<dbNode.key)return eventoExist(key,dbNode.left);
        return eventoExist(key,dbNode.right);

    }

    //Convertimos la base de datos de eventos en una lista
    private Tree dbToTree(ResultSet rs){
        Tree result = new Tree();
        try{

            while (rs.next())
            {
                int ID = Integer.parseInt(rs.getObject("ID").toString().trim());
                String nombre = rs.getObject("Nombre").toString();
                String fecha = rs.getObject("Fecha").toString();
                String lugar = rs.getObject("Lugar").toString();
                int agotado = Integer.parseInt(rs.getObject("Agotado").toString().trim());

                Evento nEvento = new Evento(ID,nombre,fecha,lugar,agotado);
                result.insert(nEvento);
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
        String result = "";
        try{
            List<WebElement> spaces = evento.findElements(By.className("location"));
            String location = spaces.get(1).getText().trim();
            result = location;
        }catch (Exception e){
            result = "-";
        }

        return result;

    }

    private int agotado(WebElement evento){

        if(evento.getText().contains("Agotado"))return 1;
        return 0;
    }
}
