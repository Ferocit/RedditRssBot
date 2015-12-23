import configuration.Config;
import configuration.NewsConfig;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.FluentRedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.TimePeriod;
import reddit.apiwrapper.RedditClientFactory;
import rss.RssNews;
import rss.RssNewsEntry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Mathias on 23.12.2015.
 */
public class RedditBot {
    RedditClient reddit;
    FluentRedditClient fluentReddit;
    Config config;

    public RedditBot() {
        reddit = RedditClientFactory.getRedditClient();
        fluentReddit = new FluentRedditClient(reddit);
        config = readConfig();
    }

    private Config readConfig() {
        Config config = null;
        try {
            File file = new File("config.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            config = (Config) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return config;
    }

    public void start() throws ApiException {
        for (NewsConfig newsConfig : config.getNewsConfigs()) {
            RssNews rss = new RssNews(newsConfig.getRssFeedUrl());
            List<RssNewsEntry> filteredRssNewsEntries = rss.getFilteredRssNewsEntries(newsConfig.getFilters());
            postRssNews(filteredRssNewsEntries, newsConfig);
        }
    }

    private void postRssNews(List<RssNewsEntry> rssNews, NewsConfig newsConfig) {
        for (RssNewsEntry entry : rssNews) {
            try {
                if (!hasLinkBeenPosted(entry.getLink(), newsConfig)) {
                    fluentReddit.subreddit(newsConfig.getSubreddit()).submit(new URL(entry.getLink()), entry.getTitle());
                    System.out.println("Posted: " + entry.getTitle());
                } else {
                    System.out.println("Duplicate found for " + entry.getTitle());
                }
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasLinkBeenPosted(String link, NewsConfig newsConfig) {
        SubredditPaginator paginator = new SubredditPaginator(reddit);
        paginator.setLimit(30);
        paginator.setTimePeriod(TimePeriod.DAY);
        paginator.setSorting(Sorting.NEW);
        paginator.setSubreddit(newsConfig.getSubreddit());

        while (paginator.hasNext()) {
            Listing<Submission> submissions = paginator.next();
            for (Submission sub : submissions) {
                if (sub.getUrl().equalsIgnoreCase(link)) {
                    return true;
                }
            }
        }

        return false;
    }
}
