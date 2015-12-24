package reddit.apiwrapper;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;
import net.dean.jraw.models.LoggedInAccount;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Mathias on 19.12.2015.
 */
public class RedditClientFactory {
    final static Logger log = Logger.getLogger(RedditClientFactory.class);
    public static RedditClient getRedditClient() {
        try {
            Properties properties = getProperties();

            UserAgent myUserAgent = UserAgent.of("Windows", "RedditAtheismNews", "0.1", "Feroc");
            RedditClient redditClient = new RedditClient(myUserAgent);
            Credentials credentials = Credentials.script(properties.getProperty("user"), properties.getProperty("password"), properties.getProperty("app_id"), properties.getProperty("app_secret"));
            OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
            redditClient.authenticate(authData);
            LoggedInAccount me = redditClient.me();
            log.info("Logged in: " + me.getFullName());
            return redditClient;
        } catch (Exception ex) {
            log.fatal(ex.getMessage());
        }
        return null;
    }

    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = RedditClientFactory.class.getClassLoader().getResourceAsStream("credentials.properties");
        properties.load(inputStream);
        return properties;
    }
}
