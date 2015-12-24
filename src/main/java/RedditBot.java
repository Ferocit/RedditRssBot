import configuration.Config;
import configuration.NewsConfig;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.FluentRedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubmissionSearchPaginator;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.TimePeriod;
import org.apache.log4j.Logger;
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

public class RedditBot {
    final static Logger log = Logger.getLogger(RedditBot.class);
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
            log.fatal("Error reading config");
            log.fatal(e.getMessage());
        }

        return config;
    }

    public void start() throws ApiException {
        for (NewsConfig newsConfig : config.getNewsConfigs()) {
            log.info("Working on '" + newsConfig.getId() + "'");
            RssNews rss = new RssNews(newsConfig.getRssFeedUrl());
            List<RssNewsEntry> filteredRssNewsEntries = rss.getFilteredRssNewsEntries(newsConfig.getFilters(), newsConfig.getMaxAge());
            postRssNews(filteredRssNewsEntries, newsConfig);
        }
    }

    private void postRssNews(List<RssNewsEntry> rssNews, NewsConfig newsConfig) {
        int counter = 0;
        for (RssNewsEntry entry : rssNews) {
            try {
                if (!hasEntryBeenPosted(entry, newsConfig)) {
                    fluentReddit.subreddit(newsConfig.getSubreddit()).submit(new URL(entry.getLink()), entry.getTitle());
                    log.info("Posted: " + entry.getTitle() + ". Sleeping for a minute.");
                    // Let's sleep a minute, maybe that will be enough to not trigger the posting limit
                    Thread.sleep(60*1000);
                    counter++;
                }
            } catch (ApiException e) {
                log.fatal("Aborting...");
                log.fatal(e.getMessage());
                return;
            } catch (MalformedURLException e) {
                log.fatal(e.getMessage());
            } catch (InterruptedException e) {
                log.fatal(e.getMessage());
            }
        }
        log.info("Done posting " + counter + " new links");
    }

    private boolean hasEntryBeenPosted(RssNewsEntry rss, NewsConfig newsConfig) {
        if (hasAlreadyPostedByMe(rss)) return true;
        if (hasAlreadyPostedBySomeone(rss, newsConfig)) return true;

        return false;
    }

    private boolean hasAlreadyPostedBySomeone(RssNewsEntry rss, NewsConfig newsConfig) {
        SubredditPaginator paginator = new SubredditPaginator(reddit);
        paginator.setLimit(500);
        paginator.setTimePeriod(TimePeriod.DAY);
        paginator.setSorting(Sorting.NEW);
        paginator.setSubreddit(newsConfig.getSubreddit());

        while (paginator.hasNext()) {
            Listing<Submission> submissions = paginator.next();
            for (Submission sub : submissions) {
                if (sub.getUrl().equalsIgnoreCase(rss.getLink()) || sub.getTitle().equalsIgnoreCase(rss.getTitle())) {
                    log.info("Already posted by someone else: '" + rss.getTitle() + "'");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasAlreadyPostedByMe(RssNewsEntry rss) {
        SubmissionSearchPaginator ssp = new SubmissionSearchPaginator(reddit,"author:BelowSubway");
        ssp.setLimit(500);
        ssp.setTimePeriod(TimePeriod.WEEK);
        ssp.setSearchSorting(SubmissionSearchPaginator.SearchSort.NEW);

        while (ssp.hasNext()) {
            Listing<Submission> userSubmissions = ssp.next();
            for (Submission sub : userSubmissions) {
                if (sub.getUrl().equalsIgnoreCase(rss.getLink()) || sub.getTitle().equalsIgnoreCase(rss.getTitle())) {
                    log.info("Already posted by me: '" + rss.getTitle() + "'");
                    return true;
                }
            }
        }
        return false;
    }
}
