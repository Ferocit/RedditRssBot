package reddit.apiwrapper;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;
import net.dean.jraw.models.LoggedInAccount;

/**
 * Created by Mathias on 19.12.2015.
 */
public class RedditClientFactory {

    public RedditClient authenticate() {
        try {
            UserAgent myUserAgent = UserAgent.of("Windows", "RedditAtheismNews", "0.1", "Feroc");
            RedditClient redditClient = new RedditClient(myUserAgent);
            Credentials credentials = Credentials.script("BelowSubway", "TimP4>>tidder", "Mt4qjB4iuaWxWw", "mmCxO0QdBCh42IUQ2-tBMN-46dA");
            OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
            redditClient.authenticate(authData);
            LoggedInAccount me = redditClient.me();
            System.out.println("Logged in: " + me.getFullName());
            return redditClient;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
