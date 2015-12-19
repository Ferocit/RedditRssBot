package rss;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RssNews {
    String rssUrl;

    public RssNews(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<RssNewsEntry> getRssNewsEntries(String filter) {
        try {
            SyndFeed syndFeedForUrl = RomeHelper.getSyndFeedForUrl(rssUrl);
            List<RssNewsEntry> rssNewsEntries = new ArrayList<RssNewsEntry>();

            for (Object o : syndFeedForUrl.getEntries()) {
                SyndEntryImpl entry = (SyndEntryImpl) o;

                if (entry.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                        entry.getDescription().getValue().toLowerCase().contains(filter.toLowerCase())) {
                    RssNewsEntry rssNewsEntry = new RssNewsEntry(entry.getTitle(), entry.getDescription().getValue(), entry.getLink(), entry.getPublishedDate());
                    rssNewsEntries.add(rssNewsEntry);

                }

            }

            return rssNewsEntries;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
