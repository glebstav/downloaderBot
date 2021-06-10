package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        DownloaderBot bot = new DownloaderBot();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        Scanner s = new Scanner(System.in);
        while (!s.next().equals("q")) {}
        bot.stop();
        s.close();
        System.exit(0);
    }
}
