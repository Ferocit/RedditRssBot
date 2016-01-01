package reddit;

import configuration.NewsConfig;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Contribution;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubmissionSearchPaginator;
import net.dean.jraw.paginators.TimePeriod;
import net.dean.jraw.paginators.UserContributionPaginator;
import org.apache.log4j.Logger;
import rss.RssNewsEntry;

/**
 * Created by Mathias on 01.01.2016.
 */
public class RedditHelper {
    final static Logger log = Logger.getLogger(RedditHelper.class);

    public static boolean hasAlreadyPostedBySomeone(RssNewsEntry rss, NewsConfig newsConfig, RedditClient reddit) {

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

    public static boolean hasAlreadyPostedByMe(RssNewsEntry rss, RedditClient reddit) {
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
