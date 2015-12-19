import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import rss.RomeHelper;
import rss.RssNews;
import rss.RssNewsEntry;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String [] args) throws IOException, FeedException {
        RssNews rss = new RssNews("http://www.rawstory.com/rs/feed/");
        List<RssNewsEntry> rssNewsEntries = rss.getRssNewsEntries("ReLiGiO");

        for (RssNewsEntry rssNewsEntry : rssNewsEntries) {
            System.out.println(rssNewsEntry.getTitle());
        }

    }


}
