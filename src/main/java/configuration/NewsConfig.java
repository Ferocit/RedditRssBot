package configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class NewsConfig {
    @XmlElement
    String id;
    @XmlElement
    String rssFeedUrl;
    @XmlElement
    String subreddit;
    @XmlElement
    List<String> filters;
    @XmlElement
    Integer maxAge;
    @XmlElement
    Boolean active;

    public Boolean isActive() {
        if (active == null) {
            return true;
        }
        return active;
    }

    public Integer getMaxAge() {
        if (maxAge == null) {
            return 1;
        }
        return maxAge;
    }

    public String getId() {
        return id;
    }
    public String getRssFeedUrl() {
        return rssFeedUrl;
    }
    public String getSubreddit() {
        return subreddit;
    }
    public List<String> getFilters() {
        return filters;
    }
}
