import mail.Mail;
import net.dean.jraw.ApiException;
import org.apache.log4j.Logger;

import javax.mail.Session;
import javax.xml.bind.JAXBException;
import java.util.Properties;

public class Main {
    final static Logger log = Logger.getLogger(Main.class);

    public static void main(String [] args) throws ApiException, JAXBException {
        Mail.sendEmail("mail@feroc.de", "test", "test body");
//        log.info("### Reddit Bot starting");
//        RedditBot redditBot = new RedditBot();
//        redditBot.start();
//        log.info("### Reddit Bot shutting down");
    }

}
