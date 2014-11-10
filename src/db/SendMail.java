package db;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The class {@code SendMail} initializes a Google Mail session
 * and allows you to send dynamic hardcoded content over mail from
 * a hardcoded account to any specified e-mail account.
 * recipient.
 *
 * @author  Nikolai Hegelstad
 */

public class SendMail {
    final String username = "koieguide@gmail.com";
    final String password = "banan111";
    private Session session;

    /**
     * Initializes an Google Mail session.
     */
    public SendMail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    /**
     * The method {@code buildMessage} utilizes {@code ui.GetData}
     * to retrieve relevant information regarding the state of the
     * cabin where the recipient holds a reservation.
     *
     * @param recipient user which associated data is linked to
     * @return message
     */
    private String buildMessage (String recipient) {
        try {

            ArrayList<Integer> koieIdVisited = new ArrayList<Integer>();
            ArrayList<Report> reportsToSend = new ArrayList<Report>();

            ArrayList<Reservation> reservations = GetData.getReservations();
            for (int i = 0; i < reservations.size(); i++) {
                Reservation res = reservations.get(i);
                if (res.getEmail().toLowerCase().equals(recipient)) {
                    if (!koieIdVisited.contains(res.getKoie_id())) {
                        koieIdVisited.add(res.getKoie_id());
                    }
                }
            }

            ArrayList<Report> reports = GetData.getReports();
            for (int i = 0; i < reports.size(); i++) {
                Report rep = reports.get(i);
                for (int u = 0; u < koieIdVisited.size(); u++) {
                    if (rep.getKoie_id() == koieIdVisited.get(u)) {
                        reportsToSend.add(rep);
                    }
                }
            }

            String message;
            message = "Hei, " + recipient + ".\n\nHer er en liste over feil eller mangler ved hytten(e) du har reservasjoner på.\n\n";
            if (reportsToSend.size() < 1) {
                message = "Hei, " + recipient + ".\n\nDet er ingen feil eller mangler ved hyttene du har reservasjoner på.\n\n";
            } else {
                for (int x = 0; x < reportsToSend.size(); x++) {
                    Report rep = reportsToSend.get(x);
                    message += "Mangelen er på hytte nr: " + rep.getKoie_id() + ".\n";
                    message += "Fra rapport nr: " + rep.getReport_id() + ":\n";
                    message += "Mangel:\n" + rep.getDeficiency() + ".\n\n";
                }
            }
            return message;
        } catch(Exception e) {
            return "En feil oppstod da denne meldingen skulle lages.";
        }
    }

    /**
     * The method {@code createAndSendMail} utilizes the instanced session
     * to create the mail to be sent, then sends it to the specified recipient.
     *
     * @param recipient user whom is to receive mail
     */
    public void createAndSendMail(String recipient) {
        try {
            recipient = recipient.toLowerCase();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("koieguide@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Info om din(e) reservasjon(er).");
            message.setText(buildMessage(recipient));
            Transport.send(message);
            System.out.println("email sent to: " + recipient);

        } catch (MessagingException e) {
            System.out.println("failed sending of email to: " + recipient);
            throw new RuntimeException(e);
        }
    }

    /**
     * The method {@code main} is used for testing purposes.
     *
     * @param args
     */
    /*
    public static void main(String[] args) {
        SendMail sendmail = new SendMail();
        //System.out.println(sendmail.buildMessage("nikolai@hegelstad.net")); //console-log av meldingen som sendes.
        sendmail.createAndSendMail("nikolai@hegelstad.net"); //plis ikke spam denne private eposten.
    }
    */
}