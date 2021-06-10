package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;

public class Start extends Condition {

    public Start(Long chatID, Controller controller) {
        super(chatID, controller);
    }

    @Override
    public void recv(Update update) throws TelegramApiException {
        message.setText("Hi! This is a bot that can download files and upload them to the telegram");
        send();
        Authorisation authorisation = new Authorisation(chatID,controller);
        controller.setCondition(chatID,this,authorisation);
        authorisation.authorisation();
    }
}
