/*---------------------------------------------------------------------
 |  Class [SendMail] - author [Nikolai Hegelstad]
 |
 |  Purpose:  [Sender epost til oppgitt epost-adresse med informasjon
 |      om mangler ved hyttene eposten har reservasjoner ved.]
 |
 |  Pre-condition:  [Oppgitt epost må ha reservasjon inne i systemet,
 |      samt at det må være en mangel ved hytten det er reservasjon på.
 |          Benytter seg av DBConnect og javax.mail.jar]
 |
 |  Post-condition: [Mail blir sendt, ingen db-entries blir endret.]
 |
 |  Parameters:
 |      recipient -- [String-verdi av epost-adresse.]
 |
 |  Returns:  [None - so far.]
 *-------------------------------------------------------------------*/
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

public class SendMail {
    final String username = "koieguide@gmail.com";
    final String password = "banan111";
    private Session session;

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

    private String buildMessage (String recipient) {
        try {
            ArrayList<Integer> koieIdVisited = new ArrayList<Integer>();
            ArrayList<Report> reportsToSend = new ArrayList<Report>();

            ArrayList<Reservation> reservations = DBConnect.getReservations();
            for (int i = 0; i < reservations.size(); i++) {
                Reservation res = reservations.get(i);
                if (res.getEmail().toLowerCase().equals(recipient)) {
                    koieIdVisited.add(res.getKoie_id());
                }
            }

            ArrayList<Report> reports = DBConnect.getReports();
            for (int i = 0; i < reports.size(); i++) {
                Report rep = reports.get(i);
                for (int u = 0; u < koieIdVisited.size(); u++) {
                    if (rep.getKoie_id() == (int)koieIdVisited.get(u)) {
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

    //bruk denne funksjonen for å sende mail, skriv inn epost-addresse og den henter ut alle mangler ved epostens reservasjoner.
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

    //for testing purposes:
    public static void main(String[] args) {
        SendMail sendmail = new SendMail();
        //System.out.println(sendmail.buildMessage("nikolai@hegelstad.net")); //console-log av meldingen som sendes.
        sendmail.createAndSendMail("nikolai@hegelstad.net"); //plis ikke spam denne private eposten.
    }
}