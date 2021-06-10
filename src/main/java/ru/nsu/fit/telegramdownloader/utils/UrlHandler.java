package ru.nsu.fit.telegramdownloader.utils;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class UrlHandler {
    static public boolean isUrl(String text) {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(text);
    }

    public void downloadFile(String url) throws IOException {
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("data");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    static public String getFileName(String address) throws MalformedURLException {
        URL url = new URL(address);
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
