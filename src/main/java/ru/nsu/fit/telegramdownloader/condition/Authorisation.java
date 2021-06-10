package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.utils.AuthorisationUtils;

public class Authorisation extends Condition {  //вся авторизация делается здесь, после прохода юзер/admin идет в след состояние
    private final AuthorisationUtils authorisationUtils;
    public Authorisation(Long chatID, Controller controller){
        super(chatID, controller);
        authorisationUtils = AuthorisationUtils.getInstance();
    }




    public void authorisation() throws TelegramApiException {
        if (authorisationUtils.isAdmin(chatID)){
            controller.setCondition(chatID,this,new AdminMenu(chatID,controller));
            return;
        }

        if(authorisationUtils.trustedUser(chatID)){
            controller.setCondition(chatID,this,new UserMenu(chatID,controller));
            return;
        }

        message.setText("You are not trusted user, please type the token given by the admin");
        send();
    }

    public void recv(Update update) throws TelegramApiException {
        if(update.getMessage().hasText() && authorisationUtils.isToken(update.getMessage().getText()) && authorisationUtils.unusedToken(update.getMessage().getText())){
            authorisationUtils.addToken(update.getMessage().getText(), update.getMessage().getFrom().getId());
            message.setText("Token accepted");
            send();
            authorisation();
        } else {
            message.setText("Try again");
            send();
        }
    }
}
