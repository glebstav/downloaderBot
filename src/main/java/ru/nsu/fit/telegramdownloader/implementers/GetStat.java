package ru.nsu.fit.telegramdownloader.implementers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.Statistics;
import ru.nsu.fit.telegramdownloader.utils.FilesUtils;

import java.util.HashMap;

public class GetStat {
    public static void sendMyStat(DownloaderBot bot, Statistics stat, Long userId, SendMessage message) throws TelegramApiException {
        message.setText("You download " + FilesUtils.humanReadableByteCountBin(stat.getUserStat(userId)));
        bot.execute(message);
    }

    public static void sendAllStat(DownloaderBot bot, Statistics stat, Long userId, SendMessage message) throws TelegramApiException {
        HashMap<Long, Long> statMap = stat.getStat();
        StringBuilder text = new StringBuilder();
        text.append("Stat\n");
        for (Long user : statMap.keySet()) {
            text.append(user).append(": ").append(FilesUtils.humanReadableByteCountBin(statMap.get(user))).append("\n");
        }

        message.setText(text.toString());
        bot.execute(message);
    }
}
