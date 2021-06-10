package ru.nsu.fit.telegramdownloader.condition;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.Controller;
import ru.nsu.fit.telegramdownloader.buttons.KeyboardAdminMenu;
import ru.nsu.fit.telegramdownloader.buttons.KeyboardUserMenu;
import ru.nsu.fit.telegramdownloader.implementers.GetStat;
import ru.nsu.fit.telegramdownloader.implementers.Helper;
import ru.nsu.fit.telegramdownloader.utils.AuthorisationUtils;
import ru.nsu.fit.telegramdownloader.utils.TokenGenerator;

import java.net.MalformedURLException;

public class AdminMenu extends UserMenu {
    private final AuthorisationUtils authorisationUtils;
    public AdminMenu(Long chatID, Controller controller) throws TelegramApiException {
        super(chatID, controller);
        authorisationUtils = AuthorisationUtils.getInstance();
        keyboard = new KeyboardAdminMenu();
        message.setText("Hi, Admin!");
        message.setReplyMarkup(keyboard.getKeyboard());
        send();
    }

    public void recv(Update update) throws TelegramApiException, MalformedURLException {
        super.recv(update);

        checkAdminButtons();
    }

    private void checkAdminButtons() throws TelegramApiException {
        if(update.getMessage().hasText()){

            String pressedButton = update.getMessage().getText();
            if(pressedButton.equals(KeyboardAdminMenu.ALL_STATS) || "/allstat".equals(pressedButton)){
                GetStat.sendAllStat(controller.getBot(),controller.getStat(),chatID,message);
            } else if(pressedButton.equals(KeyboardAdminMenu.GENERATE_TOKEN) || "/generatetoken".equals(pressedButton)){
                String token = TokenGenerator.generateToken();
                authorisationUtils.generateToken(token);
                message.setText("Generated token:");
                send();
                message.setText(token); // для удобного копирования
                send();
            }

        }

    }

}
