import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;

import java.net.MalformedURLException;

public class TestUtils {
    @Test
    public void testGetName() throws MalformedURLException {
        Assert.assertEquals("name", UrlHandler.getFileName("https://example.com/name"));
        Assert.assertEquals("", UrlHandler.getFileName("https://example.com/"));
        Assert.assertEquals("", UrlHandler.getFileName("https://example.com"));
        Assert.assertEquals("", UrlHandler.getFileName("https://example.com"));
        Assert.assertEquals("name", UrlHandler.getFileName("https://example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com:8080/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com:80/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://login@example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://login:password@example.com/path/path/path/path/name"));
        Assert.assertEquals("name", UrlHandler.getFileName("http://example.com/path/name?key=val"));
    }
}
