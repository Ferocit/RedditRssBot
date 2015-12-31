import configuration.Config;
import configuration.NewsConfig;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.fluent.FluentRedditClient;
import net.dean.jraw.models.Contribution;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubmissionSearchPaginator;
import net.dean.jraw.paginators.TimePeriod;
import net.dean.jraw.paginators.UserContributionPaginator;
import org.apache.log4j.Logger;
import reddit.RedditClientFactory;
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
                    Thread.sleep(60 * 1000);
                    counter++;
                }
            } catch (ApiException e) {
                log.fatal("ApiException");
                log.fatal(e.getMessage());
                return;
            } catch (MalformedURLException e) {
                log.fatal("MalformedURLException");
                log.fatal(e.getMessage());
            } catch (InterruptedException e) {
                log.fatal("InterruptedException");
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

        SubmissionSearchPaginator paginator = new SubmissionSearchPaginator(reddit, String.format("subreddit:%s url:%s", newsConfig.getSubreddit(), rss.getLink()));
        paginator.setLimit(50);
        paginator.setTimePeriod(TimePeriod.MONTH);
        paginator.setSearchSorting(SubmissionSearchPaginator.SearchSort.NEW);
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
        UserContributionPaginator ucp = new UserContributionPaginator(reddit, "submitted", "BelowSubway");
        ucp.setLimit(50);
        ucp.setTimePeriod(TimePeriod.WEEK);
        ucp.setSorting(Sorting.NEW);

        while (ucp.hasNext()) {
            Listing<Contribution> userContributions = ucp.next();
            for (Contribution contribution : userContributions) {
                String url = contribution.data("url").toString();
                String title = contribution.data("title").toString();
                if (url.equals(rss.getLink()) || title.equalsIgnoreCase(rss.getTitle())) {
                    log.info("Already posted by me: '" + rss.getTitle() + "'");
                    return true;
                }
            }
        }
        return false;
    }
}
