package ru.nsu.fit.telegramdownloader.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public abstract class InlineKeyboard {
    InlineKeyboardMarkup inlineKeyboardMarkup;
    List<List<InlineKeyboardButton>> rowList;

    InlineKeyboard(int cntOfRows){
        inlineKeyboardMarkup = new InlineKeyboardMarkup();
        rowList = new ArrayList<>();
        for(int row = 0;row<cntOfRows; row++) {
            rowList.add(new ArrayList<>());
        }
    }

    void SetInlineKeyBoard(){
        inlineKeyboardMarkup.setKeyboard(rowList);
    }

    void SetButtonsInRow(List<InlineKeyboardButton> row, String[] nameButton,String[] callbackData){
        for(int buttonNumber = 0;buttonNumber<nameButton.length;buttonNumber++){
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(nameButton[buttonNumber]);
            button.setCallbackData(callbackData[buttonNumber]);
            row.add(button);
        }
    }

    public InlineKeyboardMarkup getInlineKeyboard(){
        return inlineKeyboardMarkup;
    }
}
