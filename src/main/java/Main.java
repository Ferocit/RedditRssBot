import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.FluentRedditClient;
import net.dean.jraw.models.Submission;
import reddit.apiwrapper.RedditClientFactory;
import rss.RomeHelper;
import rss.RssNews;
import rss.RssNewsEntry;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String [] args) throws IOException, FeedException, InterruptedException, ApiException {

        RssNews rss = new RssNews("http://www.rawstory.com/rs/feed/");
        List<RssNewsEntry> rssNewsEntries = rss.getRssNewsEntries("a");

        RedditClientFactory auth = new RedditClientFactory();
        RedditClient reddit = auth.authenticate();

        FluentRedditClient fluent = new FluentRedditClient(reddit);

        Submission submission = fluent.subreddit("News4AtheistsBotTest").submit("http://www.google.de", "title");
    }

}
