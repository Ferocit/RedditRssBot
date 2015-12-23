package rss;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

    public List<RssNewsEntry> getFilteredRssNewsEntries(List<String> filters, Integer maxAge) {

        if (rssNewsEntires == null) {
            this.getRssNewsEntries();
        }

        List<RssNewsEntry> filteredList = new ArrayList<RssNewsEntry>();

        for (String filter : filters) {
            for (RssNewsEntry entry : rssNewsEntires) {
                if (!isOlderThanXDays(entry.getPublishedDate(), maxAge)) {
                    if (entry.getTitle().toLowerCase().contains(filter.toLowerCase())) {
                        filteredList.add(entry);
                    }
                }
            }
        }
        return filteredList;

    }

    private boolean isOlderThanXDays(Date publishedDate, Integer maxAge) {
        if (maxAge == null) {
            return false;
        }

        Date today = new Date();
        long startTime = publishedDate.getTime();
        long endTime = today.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        if (diffDays > maxAge) {
            return true;
        }

        return false;
    }

}
