import com.sun.syndication.io.FeedException;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.FluentRedditClient;
import net.dean.jraw.http.SubmissionRequest;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.Submission;
import reddit.apiwrapper.RedditClientFactory;

import java.io.IOException;

public class Main {

    public static void main(String [] args) throws IOException, FeedException, ApiException {
//        RssNews rss = new RssNews("http://www.rawstory.com/rs/feed/");
//        List<RssNewsEntry> rssNewsEntries = rss.getRssNewsEntries("ReLiGiO");
//
//        for (RssNewsEntry rssNewsEntry : rssNewsEntries) {
//            System.out.println(rssNewsEntry.getTitle());
//        }

        RedditClientFactory auth = new RedditClientFactory();
        RedditClient reddit = auth.authenticate();

        FluentRedditClient fluent = new FluentRedditClient(reddit);

        Submission submission = fluent.subreddit("News4AtheistsBotTest").submit("http://www.google.de", "title");





    }


}
