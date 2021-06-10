package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.buttons.Keyboard;

import java.net.MalformedURLException;

public abstract class Condition {
    protected Long chatID;
    protected Controller controller;
    protected SendMessage message;
    protected Update update;
    protected Keyboard keyboard;


    public Condition(Long chatID, Controller controller){
        this.chatID = chatID;
        this.controller = controller;
        message = new SendMessage();
    }

    public Long getChatID() {
        return chatID;
    }

    public void send() throws TelegramApiException {
        message.setChatId(chatID.toString());
        controller.sendMess(message);
    }


    public abstract void recv(Update update) throws TelegramApiException, MalformedURLException;





   /* public void checkDefault(Update update) throws TelegramApiException {
        if(update.getMessage().getText().equals("/start") && !(this instanceof Start)){
            controller.setCondition(chatID,this,new Start(chatID,controller));
            controller.recvMess(update);
            return;
        };
        if(update.getMessage().getText().equals("/help") && !(this instanceof Help)){
            controller.setCondition(chatID,this,new Help(chatID,controller));
            controller.recvMess(update); //по сути протолкали сообщение в другое состояние через контроллер
        };
    }*/



}
