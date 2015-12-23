package configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class NewsConfig {
    String id;
    String rssFeedUrl;
    String subreddit;
    List<String> filters;

    public String getId() {
        return id;
    }

    @XmlElement
    public void setId(String id) {
        this.id = id;
    }

    public String getRssFeedUrl() {
        return rssFeedUrl;
    }

    @XmlElement
    public void setRssFeedUrl(String rssFeedUrl) {
        this.rssFeedUrl = rssFeedUrl;
    }

    public String getSubreddit() {
        return subreddit;
    }

    @XmlElement
    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public List<String> getFilters() {
        return filters;
    }

    @XmlElement
    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
}
