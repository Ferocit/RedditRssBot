package rss;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RssNews {
    String rssUrl;
    List<RssNewsEntry> rssNewsEntires;

    public RssNews(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<RssNewsEntry> getRssNewsEntries() {
        try {
            SyndFeed syndFeedForUrl = RomeHelper.getSyndFeedForUrl(rssUrl);
            List<RssNewsEntry> rssNewsEntries = new ArrayList<RssNewsEntry>();

            for (Object o : syndFeedForUrl.getEntries()) {
                SyndEntryImpl entry = (SyndEntryImpl) o;
                RssNewsEntry rssNewsEntry = new RssNewsEntry(entry.getTitle(), entry.getDescription().getValue(), entry.getLink(), entry.getPublishedDate());
                rssNewsEntries.add(rssNewsEntry);
            }
            this.rssNewsEntires = rssNewsEntries;
            return rssNewsEntries;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<RssNewsEntry> getFilteredRssNewsEntries(List<String> filters) {

        if (rssNewsEntires == null) {
            this.getRssNewsEntries();
        }

        List<RssNewsEntry> filteredList = new ArrayList<RssNewsEntry>();

        for (String filter : filters) {
            for (RssNewsEntry entry : rssNewsEntires) {
                if (entry.getTitle().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(entry);
                }
            }
        }
        return filteredList;

    }

}
