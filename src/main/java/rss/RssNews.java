package rss;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RssNews {
    final static Logger log = Logger.getLogger(RssNews.class);
    String rssUrl;
    List<RssNewsEntry> rssNewsEntries;

    public RssNews(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<RssNewsEntry> getRssNewsEntries() {
        List<RssNewsEntry> rssNewsEntries = new ArrayList<RssNewsEntry>();
        try {
            SyndFeed syndFeedForUrl = RomeHelper.getSyndFeedForUrl(rssUrl);

            for (Object o : syndFeedForUrl.getEntries()) {
                SyndEntryImpl entry = (SyndEntryImpl) o;
                if (entry != null) {
                    String title = entry.getTitle();
                    String descriptionValue = "";
                    if (entry.getDescription() != null) {
                        descriptionValue = entry.getDescription().getValue();
                    }
                    String link = entry.getLink();
                    Date publishedDate = entry.getPublishedDate();
                    RssNewsEntry rssNewsEntry = new RssNewsEntry(title, descriptionValue, UrlHelper.getRedirectedUrl(new URL(link)), publishedDate);
                    rssNewsEntries.add(rssNewsEntry);
                }
            }
            this.rssNewsEntries = rssNewsEntries;
            log.info("Read " + rssNewsEntries.size() + " articles from RSS.");
            return rssNewsEntries;

        } catch (IOException e) {
            log.fatal("IOException");
            log.fatal(e.getMessage());
        } catch (FeedException e) {
            log.fatal("FeedException");
            log.fatal(e.getMessage());
        }
        this.rssNewsEntries = rssNewsEntries;
        return rssNewsEntries;
    }

    public List<RssNewsEntry> getFilteredRssNewsEntries(List<String> filters, Integer maxAge) {

        if (rssNewsEntries == null) {
            this.getRssNewsEntries();
        }

        // If we don't have any filters, then I assume we just want everything
        if (filters == null) {
            return rssNewsEntries;
        }

        List<RssNewsEntry> filteredList = new ArrayList<RssNewsEntry>();

        for (String filter : filters) {
            for (RssNewsEntry entry : rssNewsEntries) {
                if (!isOlderThanXDays(entry.getPublishedDate(), maxAge)) {
                    if (entry.getTitle().toLowerCase().contains(filter.toLowerCase())) {
                        if (!listContainsEntryWithSameLink(filteredList, entry.getLink())) {
                            filteredList.add(entry);
                        }
                    }
                }
            }
        }
        return filteredList;
    }

    private boolean listContainsEntryWithSameLink(List<RssNewsEntry> filteredList, String link) {
        for (RssNewsEntry entry : filteredList) {
            if (entry.getLink().equalsIgnoreCase(link)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOlderThanXDays(Date publishedDate, Integer maxAge) {
        if (maxAge == null || publishedDate == null) {
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
