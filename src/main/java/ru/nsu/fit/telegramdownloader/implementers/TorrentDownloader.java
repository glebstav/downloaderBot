package ru.nsu.fit.telegramdownloader.implementers;


import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.zeroturnaround.zip.ZipUtil;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.Statistics;
import ru.nsu.fit.telegramdownloader.buttons.Keyboard;
import ru.nsu.fit.telegramdownloader.utils.FilesUtils;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TorrentDownloader extends StatusUpdater implements Runnable {
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(TorrentDownloader.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }
    private Client client;
    private final DownloaderBot bot;
    private final String inputName;
    private final Statistics stat;
    private final Long userId;

    public TorrentDownloader(Document torrentDoc, String inputName, DownloaderBot bot, String chatId,
                             Statistics stat, Long userId, Keyboard keyboard) throws TelegramApiException {
        super("Init torrent..", bot, chatId,keyboard);
        this.bot = bot;
        this.inputName = inputName;
        this.stat = stat;
        this.userId = userId;
        try {
            File torrentFile = downloadTorrentFile(torrentDoc);
            LOGGER.info("Start create client");
            FilesUtils.mkDir("downloadTelegramBot/" + getDirName(inputName));
            client = new Client(InetAddress.getLocalHost(),
                    SharedTorrent.fromFile(torrentFile, new File("downloadTelegramBot/" + getDirName(inputName))));

            LOGGER.info("Client created");
            client.setMaxDownloadRate(0.0);
            client.setMaxUploadRate(0.0);
        } catch (IOException | NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            updateStatus("Start download torrent");
            client.download();
            int percentage = 0;
            while (client.getTorrent().getCompletion()!=100){
                if(percentage!=(int)client.getTorrent().getCompletion()) {
                    updateStatus("Downloaded " + (int)client.getTorrent().getCompletion()+"%");
                }
                percentage = (int)client.getTorrent().getCompletion();
                Thread.sleep(100);
            }
            client.waitForCompletion();
            updateStatus("Start upload file...");
            String zipName = zipDir("downloadTelegramBot/" + getDirName(inputName));
            uploadFile(zipName);
            stat.updateUserStat(userId, FilesUtils.getFileSize(zipName));
            Files.delete(Paths.get(zipName));
            FileUtils.deleteDirectory(new File("downloadTelegramBot/" + getDirName(inputName)));
            updateStatus("Done!");
        } catch (TelegramApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String zipDir(String dirName) throws IOException {
        ZipUtil.pack(new File(dirName), new File(deleteSlash(dirName) + ".zip"));
        return deleteSlash(dirName) + ".zip";
    }

    private String deleteSlash(String text) {
        if (text.endsWith("/")) {
            return text.substring(0, text.length() - 1);
        }
        return text;
    }

    // https://stackoverflow.com/questions/3449218/remove-filename-extension-in-java
    private String getDirName(String torrentFilename) {
        final int lastPeriodPos = torrentFilename.lastIndexOf('.');
        if (lastPeriodPos <= 0) {
            return torrentFilename;
        }
        return torrentFilename.substring(0, lastPeriodPos);
    }

    private File downloadTorrentFile(Document torrentDoc) {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(torrentDoc.getFileId());
        try {
            LOGGER.info("download .torrent file");
            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFileMethod);
            return bot.downloadFile(file.getFilePath());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

}
