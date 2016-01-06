package configuration;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Config {
    List<NewsConfig> newsConfigs;

    public List<NewsConfig> getNewsConfigs() {
        return newsConfigs;
    }

    @XmlElement(name = "NewsConfig")
    public void setNewsConfigs(List<NewsConfig> newsConfigs) {
        this.newsConfigs = newsConfigs;
    }



}
