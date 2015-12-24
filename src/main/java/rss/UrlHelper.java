package rss;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mathias on 24.12.2015.
 */
public class UrlHelper {
    final static Logger log = Logger.getLogger(UrlHelper.class);

    public static String getRedirectedUrl(URL url) {
        HttpURLConnection con = null;

        String urlString = url.toString();

        if (!urlString.contains("feedproxy")) {
            return urlString;
        }

        try {
            con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(false);
            con.connect();
            return (con.getHeaderField("Location"));
        } catch (IOException e) {
            log.fatal(e.getMessage());
        }
        return null;
    }
}
