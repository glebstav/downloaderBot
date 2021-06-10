package ru.nsu.fit.telegramdownloader.implementers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.condition.Authorisation;

public class Helper {
    public static void sendHelp(DownloaderBot bot, SendMessage message) throws TelegramApiException {
        message.setText("DownloaderBot can upload files via URL\\torrent. Just paste the URL or upload .torrent file");
        bot.execute(message);
        message.setText("Also you can view your downloaded statistics");
        bot.execute(message);
        message.setText("For all other questions, please contact @glebstav");
        bot.execute(message);
    }
}
