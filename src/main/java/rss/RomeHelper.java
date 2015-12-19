package rss;

import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.ParsingFeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * Created by Mathias on 19.12.2015.
 */
public class RomeHelper {

    public static SyndFeed getSyndFeedForUrl(String url) throws MalformedURLException, IOException, IllegalArgumentException, FeedException {

        SyndFeed feed = null;
        InputStream is = null;

        try {

            URLConnection openConnection = new URL(url).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            is = openConnection.getInputStream();
            if ("gzip".equals(openConnection.getContentEncoding())) {
                is = new GZIPInputStream(is);
            }

            InputSource source = new InputSource(is);
            SyndFeedInput input = new SyndFeedInput();
            feed = input.build(source);

        } catch (ParsingFeedException e) {
            throw e;
        } catch (MalformedByteSequenceException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) is.close();
        }

        return feed;
    }
}
