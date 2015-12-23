import net.dean.jraw.ApiException;

import javax.xml.bind.JAXBException;

public class StartHere {

    public static void main(String [] args) throws ApiException, JAXBException {
        RedditBot redditBot = new RedditBot();
        redditBot.start();
    }

}
