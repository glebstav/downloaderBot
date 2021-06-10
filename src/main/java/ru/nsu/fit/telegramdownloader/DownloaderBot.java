package ru.nsu.fit.telegramdownloader;

import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import ru.nsu.fit.telegramdownloader.implementers.GetStat;
import ru.nsu.fit.telegramdownloader.implementers.TorrentDownloader;
import ru.nsu.fit.telegramdownloader.implementers.UrlDownloader;
import ru.nsu.fit.telegramdownloader.utils.TokenGenerator;
import ru.nsu.fit.telegramdownloader.utils.UrlHandler;
import org.apache.commons.lang3.StringUtils;

public class DownloaderBot extends TelegramLongPollingBot {
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(DownloaderBot.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }

    private final Controller controller;

    public DownloaderBot() {

        controller = new Controller();
        controller.setBot(this);
        LOGGER.log(Level.INFO, "Init bot");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {

                controller.recvMess(update);
            } catch (TelegramApiException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public Message sendMessage(String text, String chatId) throws TelegramApiException {
        SendMessage message = new SendMessage(chatId, text);
        return execute(message);
    }
    public Message sendMessage(SendMessage message,String text, String chatId) throws TelegramApiException {
        message.setText(text);
        message.setChatId(chatId);
        return execute(message);
    }


        /*
    //контроллеру это отправлять
    private void acceptMessage(Message message) throws TelegramApiException, MalformedURLException {
        if (message.hasText()) {
            acceptTextMessage(message);
        } else if (message.hasDocument()) {
            acceptDocumentMessage(message);
        }
    }
    //контроллер
    private boolean checkAuth(Message message) {
        return authorisationUtils.trustedUser(message.getFrom().getId());
    }
    //контроллер
    private void acceptDocumentMessage(Message message) throws TelegramApiException {
        if (!checkAuth(message)) {
            sendMessage("You are not trusted user", message.getChatId().toString());
            return;
        }
        if (!FilenameUtils.getExtension(message.getDocument().getFileName()).equals("torrent")) {
            sendMessage("This file is not a torrent-file", message.getChatId().toString());
            return;
        }
        File torrentFile = downloadTorrentFile(message.getDocument());
        Thread torrentThread = new Thread(new TorrentDownloader(torrentFile, message.getDocument().getFileName(),
                this, message.getChatId().toString(), stat, message.getFrom().getId()));
        torrentThread.start();
    }

    //перенести в состояние скачивателя
    private File downloadTorrentFile(Document torrentDoc) {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(torrentDoc.getFileId());
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFileMethod);
            return downloadFile(file.getFilePath());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    /контроллер
    private void acceptTextMessage(Message message) throws TelegramApiException, MalformedURLException {
        System.out.println(message.getFrom().getId());
        if (UrlHandler.isUrl(message.getText()) && authorisationUtils.trustedUser(message.getFrom().getId())) {
            Thread downloadThread = new Thread(new UrlDownloader(message.getChatId().toString(),
                    message.getText(), this, stat, message.getFrom().getId()));
            downloadThread.start();
        } else if (!authorisationUtils.trustedUser(message.getFrom().getId()) &&
                authorisationUtils.isToken(message.getText())) {
            authorisationUtils.addToken(message.getText(), message.getFrom().getId());
            sendMessage("Token accepted", message.getChatId().toString());
        } else if (!authorisationUtils.trustedUser(message.getFrom().getId())) {
            sendMessage("You are not trusted user", message.getChatId().toString());
        } else if ("/stat".equals(message.getText())) {
            GetStat.sendMyStat(this, stat, message.getFrom().getId(), message.getChatId().toString());
        } else if (authorisationUtils.isAdmin(message.getFrom().getId()) && "/allstat".equals(message.getText())) {
            GetStat.sendAllStat(this, stat, message.getFrom().getId(), message.getChatId().toString());
        } else if (authorisationUtils.isAdmin(message.getFrom().getId()) &&
                message.getText().split(" ", 2)[0].equals("/generatetoken") &&
                StringUtils.isNumeric(message.getText().split(" ", 2)[1])) {
            String token = TokenGenerator.generateToken();
            sendMessage("Generated token: " + token, message.getChatId().toString());
            authorisationUtils.addToken(token, Long.parseLong(message.getText().split(" ", 2)[1], 10));
        } else {
            sendMessage("is not url", message.getChatId().toString());
        }
    }*/











    @Override
    public String getBotUsername() {
        return "Downloader Bot";
    }

    // https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java
    @Override
    public String getBotToken() {
        String token = null;
        BufferedReader br;
        try {
            File tokenFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("token.txt")).getFile());
            br = new BufferedReader(new FileReader(tokenFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            token = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert token != null;
        return token.replaceAll("\r?\n","");
    }

    public void stop() {
        try {
            controller.closeStat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosing() {
        super.onClosing();
        LOGGER.log(Level.INFO, "Stop bot");
    }
}
