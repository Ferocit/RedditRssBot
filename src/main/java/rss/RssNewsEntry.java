package rss;

import java.util.Date;

public class RssNewsEntry {
    private String title;
    private String description;
    private String link;
    private Date publishedDate;

    public RssNewsEntry(String title, String description, String link, Date publishedDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.publishedDate = publishedDate;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
