package ru.nsu.fit.telegramdownloader;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.condition.Condition;
import ru.nsu.fit.telegramdownloader.condition.Start;
import ru.nsu.fit.telegramdownloader.utils.AuthorisationUtils;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class Controller {

    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(DownloaderBot.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }

    private final AuthorisationUtils authorisationUtils;
    private final Statistics stat;
    private Map<Long, Condition> users;
    private Long chatId;
    private DownloaderBot telegramBot;


    public Controller(){
        authorisationUtils = AuthorisationUtils.getInstance();
        users = new HashMap<>();
        stat = new Statistics();
    }

    public void recvMess(Update update) throws TelegramApiException, MalformedURLException {
        setChatId(update);
        LOGGER.log(Level.INFO,"mess from "+chatId);
        if(!users.containsKey(chatId)){ //нового пользователя кидаем в старт
            users.put(chatId,new Start(chatId,this));
        }
        users.get(chatId).recv(update);  //идем в состояние и кидаем туда месс

    }

    public void sendMess(BotApiMethod<Message> messageNew) throws TelegramApiException {
        telegramBot.execute(messageNew);
    }

    public void setBot(DownloaderBot telegramBot){
        this.telegramBot = telegramBot;
    }
    public DownloaderBot getBot(){
        return telegramBot;
    }

    public Statistics getStat() {
        return stat;
    }

    public void setCondition(Long chatID, Condition condition, Condition newCondition){
        users.replace(chatID,condition, newCondition);
    }

    private void setChatId(Update update){
        chatId = update.getMessage().getFrom().getId();
    }

    public void closeStat() throws Exception {
        stat.close();
    }
}
