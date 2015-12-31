package mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

/**
 * Created by Mathias on 29.12.2015.
 */
public class Mail {

    public static void sendEmail(String recipientAddress, String emailSubject, String emailText){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@domain.com", "Admin", "UTF-8"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
            msg.setSubject(emailSubject);
            msg.setText(emailText);
            Transport.send(msg);

        } catch (AddressException e) {
// TO address not valid
        } catch (MessagingException e) {
            // other email error
        } catch (UnsupportedEncodingException e) {
            // should not happen UTF-8 is always available
        }
    }
}
