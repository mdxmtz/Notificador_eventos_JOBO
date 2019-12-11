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
        MailMan mailMan = new MailMan();
        try {
            System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");
            ChromeDriver driver = new ChromeDriver();
            driver.get(url);

            WebElement username = driver.findElementById("login");
            username.sendKeys("pablosky301@gmail.com");

            WebElement password = driver.findElementById("password");
            password.sendKeys("");

            WebElement button = driver.findElementById("continue_button");
            button.click();

            WebElement button2 = driver.findElementById("account_cart_button");
            button2.click();

            WebElement button3 = driver.findElementById("addOtherProducts");
            button3.click();

            List<WebElement> eventos = driver.findElementByClassName("group_content").findElements(By.cssSelector("*"));

            Tree eventosScrap = new Tree();

            for (WebElement evento : eventos) {
                if (evento.getAttribute("id").contains("prod_")) {
                    int id = ID(evento);
                    String title = title(evento);
                    String date = date(evento);
                    String place = place(evento);
                    int agotado = agotado(evento);
                    Evento event = new Evento(id, title, place, date, agotado);
                    eventosScrap.insert(event);
                }
            }

            List<List<Evento>> updatedEventos = newEvents(eventosScrap);
            System.out.println("Eventos nuevos");
            for (Evento evento : updatedEventos.get(0)) {
                evento.showEvento();
                System.out.println("________");
            }
            System.out.println("Eventos viejos");
            for (Evento evento : updatedEventos.get(1)) {
                evento.showEvento();
                System.out.println("________");
            }
            driver.close();
            mailMan.sendEventos(updatedEventos);
            System.out.println("Done");
        }catch (Exception e){
            mailMan.sendError(e.getMessage());
        }
    }

    //Ingresa en la base de datos los nuevos eventos encontrados, y devuleve un lista con los mismos
    private List<List<Evento>> newEvents(Tree eventosScrap){
        List<List<Evento>> result = new ArrayList<List<Evento>>();
        List<List<Evento>> updatedEvents = new ArrayList<List<Evento>>();
        List<Evento> newEvents = new ArrayList<Evento>();
        List<Evento> oldEvents = new ArrayList<Evento>();
        List<Evento> dummyList = new ArrayList<Evento>();
        updatedEvents.add(dummyList);
        updatedEvents.add(dummyList);
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventosjobo", "root", "1234");
            Statement st = conexion.createStatement();
            st.executeQuery ("SELECT * FROM eventos");
            ResultSet rs = st.getResultSet ();
            Tree dbTree = dbToTree(rs);

            newEvents = compareTrees(eventosScrap.root,dbTree.root,newEvents,st,false);
            oldEvents = compareTrees(dbTree.root,eventosScrap.root,oldEvents,st,true);

            updatedEvents = compareAvailabilty(eventosScrap.root,dbTree.root,updatedEvents,st);
            result.add(newEvents);//0
            result.add(oldEvents);//1
            result.add(updatedEvents.get(0));//2
            result.add(updatedEvents.get(1));//3
            rs.close();
            st.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("No funcionó puto");
            return result;
        }
        return result;

    }
    private List<List<Evento>> compareAvailabilty(Node sNode, Node dbNode, List<List<Evento>> result,  Statement st){
        if(sNode.left != null)compareAvailabilty(sNode.left,dbNode,result,st);
        boolean update = statusChanged(sNode.getEvento(),dbNode);
        if(update){
            try{
                if(sNode.getEvento().getAgotado()==0){
                    result.get(1).add(sNode.getEvento());
                    st.executeUpdate("UPDATE eventos SET Agotado = '0' WHERE ID="+sNode.getEvento().getID());
                }else{
                    result.get(1).add(sNode.getEvento());
                    st.executeUpdate("UPDATE eventos SET Agotado = '1' WHERE ID="+sNode.getEvento().getID());
                }
            }catch (Exception e){

            }
        }
        if(sNode.right !=null)compareAvailabilty(sNode.right,dbNode,result,st);
        return result;
    }
    private boolean statusChanged(Evento e, Node dbNode){
        if(dbNode == null)return false;
        if((dbNode.getEvento().getID()==e.getID())&&(dbNode.getEvento().getAgotado()^e.getAgotado())==1)return true;
        if(e.getID()<dbNode.getEvento().getID())return statusChanged(e,dbNode.left);
        return statusChanged(e,dbNode.right);
    }

    /*Compara los elementos de dos árboles en función de DBvsScrap:
        DBvsScrap = false,
        Compara el árbol de eventos scrapeados con el árbol de la base de datos,
        devuelve una lista con los eventos nuevos y los inserta en la db

        DBvsScrap = true,
        Compara el árbol de la base de datos con el árbol de eventos scrapeados,
        devuelve una lista con los eventos ya no disponibles y los elimina de la db



     */
    private List<Evento> compareTrees(Node sNode, Node dbNode, List<Evento> result,  Statement st, boolean DBvsScrap){
        if(sNode.left != null)compareTrees(sNode.left,dbNode,result,st, DBvsScrap);
        boolean exist = eventoExist(sNode.key,dbNode);
        try{
            if(!exist && !DBvsScrap) {
                result.add(sNode.getEvento());
                st.executeUpdate("INSERT INTO eventos (ID, Nombre, Fecha, Lugar, Agotado) VALUES ('"+sNode.getEvento().getID()+"','"+sNode.getEvento().getNombre()+"','"+sNode.getEvento().getFecha()+"','"+sNode.getEvento().getLugar()+"','"+sNode.getEvento().getAgotado()+"')");
            }if(!exist && DBvsScrap){
                result.add(sNode.getEvento());
                st.executeUpdate("DELETE FROM eventos WHERE ID="+sNode.getEvento().getID());
            }
        }catch (Exception e){
            System.out.println("iora");
            System.out.println(e.getMessage());
        }

        if(sNode.right !=null)compareTrees(sNode.right,dbNode,result,st, DBvsScrap);

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
