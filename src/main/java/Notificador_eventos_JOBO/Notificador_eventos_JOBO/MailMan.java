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
                    InternetAddress.parse("pablosky301@gmail.com, pablotcampos25@gmail.com, 100405806@alumnos.uc3m.es")
            );
            message.setSubject("Testing Gmail TLS");
            String text = "# Nuevos eventos disponibles!!! \n\n";
            for(Evento e : neventos){
                //text += e.getID() + "\n";

                text += "<h3 style=\"font-size:18px;color:rgb(204,102,0);margin:15px 0 0 0;font-weight:normal\">"+ "Título: " + e.getNombre()+"</h3>" + "\n";
                text += "* Fecha: " + e.getFecha() + "\n";
                text += "* Lugar: " +e.getLugar() + "\n";
                text += "_______________"+"\n";

            }
            String text2 = "<!DOCTYPE HTML>\n" +
                    "<html lang = \"es\">\n" +
                    "\t<head>\n" +
                    "\t\t<meta charset = \"utf-8\"/>\n" +
                    "\t\t<title>Hola Mundo con JS</title>\t\t\n" +
                    "\t\n" +
                    "\t</head>\n" +
                    "\t<body>\n" +
                    "\t\t<h1>Curso JS</h1>\n" +
                    "\t\t<p>\n" +
                    "\t\t\t<button id=\"boton\" onclick=\"\" ondblclick=\"\">Presioname</button>\n" +
                    "\t\t\t<button id=\"start\" >START</button>\n" +
                    "\t\t\t<button id=\"stop\" >STOP</button>\n" +
                    "\t\t\t<form>\n" +
                    "\t\t\t\t<input type =\"text\" name=\"nombre\" id=\"input\"/>\n" +
                    "\t\t\t</form>\n" +
                    "\t\t</p>\n" +
                    "\n" +
                    "\t\t<br/>\n" +
                    "\t\t<div id=\"micaja\">Hola soy una caja</div>\n" +
                    "\t\t<hr/>\n" +
                    "\t\t<section id=\"miseccion\">\n" +
                    "\t\t\t<h1 id=\"encabezado\">Listado de texto de los divs</h1>\n" +
                    "\t\t</section>\n" +
                    "\t\t<div class =\"amarillo\">Primero</div>\n" +
                    "\t\t<div class =\"rojo\">Segundo</div>\n" +
                    "\t\t<div class =\"rojo\">Tercero</div>\n" +
                    "\t\t<!-- SCRIPTS \n" +
                    "\t\t<script type=\"text/javascript\" src=\"js/28-dom.js\"></script>\n" +
                    "\t\t<script type=\"text/javascript\" src=\"js/29-bom.js\"></script>-->\n" +
                    "\t</body>\n" +
                    "</html>";
            message.setText(text2);

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
