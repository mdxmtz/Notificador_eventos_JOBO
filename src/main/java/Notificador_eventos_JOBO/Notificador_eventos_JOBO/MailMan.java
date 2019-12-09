package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class MailMan {

    final private String username = "anunciosjobo@gmail.com";
    final private String password = "I9H%8Lo!!OOl16";

    public void sendEventos(List<List<Evento>> updatedEventos){


        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {return new PasswordAuthentication(username, password);
                    }
                });

        String text = "<h1> Actualizaciones de JOBO </h1>\n\n";
        System.out.println(text.length());
        if(updatedEventos.get(0).size()>0)text += eventToText(updatedEventos.get(0),1);
        if(updatedEventos.get(1).size()>0)text += eventToText(updatedEventos.get(1),2);
        if(text.length()<=36)return;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventosjobo", "root", "1234");
            Statement st = conexion.createStatement();
            st.executeQuery ("SELECT * FROM correos");
            ResultSet rs = st.getResultSet ();
            while(rs.next()){
                sendMail(text,rs.getObject("correos").toString().trim(),session);
            }
            rs.close();
            st.close();
            conexion.close();
            System.out.println("Emails send succedfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String eventToText(List<Evento> eventos, int type){
        String text = "";
        switch(type){
            case 1:
                text += "<h2>Nuevos eventos disponibles</h2> \n\n";
                break;
            case 2:
                text += "<h2>Eventos no disponibles</h2> \n\n";
                break;
            default:
                break;
        }
        for(Evento e : eventos){
            text += "<div><h3>Título: " + e.getNombre()+"</h3></div>\n\n";
            text += "<div>Fecha: " + e.getFecha() + "</div>\n\n";
            text += "<div>Lugar: " +e.getLugar() + "</div>\n\n";
            if(e.getAgotado()==1){
                text += "<div>Disponible: No </div>\n\n";
            }else{
                text += "<div>Disponible: Sí </div>\n\n";
            }
        }
        return text;
    }

    private void sendMail(String text, String to, Session sesion){
        try{
            Message message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Actualizaciones de JOBO");
            message.setContent(text,"text/html");
            Transport.send(message);
        }catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
