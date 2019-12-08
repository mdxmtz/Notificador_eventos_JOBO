package Notificador_eventos_JOBO.Notificador_eventos_JOBO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class MailMan {
    public void sendEventos(List<Evento> neventos, List<Evento> veventos){
        final String username = "anunciosjobo@gmail.com";
        final String password = "I9H%8Lo!!OOl16";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("pablosky301@gmail.com, pablotcampos25@gmail.com,100405806@alumnos.uc3m.es")
                    // , irenefluxa@gmail.com
            );
            message.setSubject("Testing Gmail TLS");
            String text = "<h1> Actualizaciones de JOBO </h1>\n\n";

             text += "<h2>Nuevos eventos disponibles</h2> \n\n";
            for(Evento e : neventos){
                text += "<div><h3>Título: " + e.getNombre()+"</h3></div>\n\n";
                text += "<div>Fecha: " + e.getFecha() + "</div>\n\n";
                text += "<div>Lugar: " +e.getLugar() + "</div>\n\n";
                if(e.getAgotado()==1){
                    text += "<div>Disponible: No </div>\n\n";
                }else{
                    text += "<div>Disponible: Sí </div>\n\n";
                }
            }
            message.setContent(text,"text/html");
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }




    /*public void start(){
        final String username = "anunciosjobo@gmail.com";
        final String password = "I9H%8Lo!!OOl16";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("pablosky301@gmail.com, pablotcampos25@gmail.com, 100405806@alumnos.uc3m.es")
            );
            message.setSubject("Testing Gmail TLS");
            message.setText("Dear Mail Crawler,"
                    + "\n\n Please do not spam my email!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }*/
}
