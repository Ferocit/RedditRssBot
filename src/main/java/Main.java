import net.dean.jraw.ApiException;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;

public class Main {
    final static Logger log = Logger.getLogger(Main.class);

    public static void main(String [] args) throws ApiException, JAXBException {
        log.info("### Reddit Bot starting");
        RedditBot redditBot = new RedditBot();
        redditBot.start();
        log.info("### Reddit Bot shutting down");
    }

}
