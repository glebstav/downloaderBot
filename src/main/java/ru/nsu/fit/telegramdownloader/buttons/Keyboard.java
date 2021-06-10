package ru.nsu.fit.telegramdownloader.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public abstract class Keyboard {

    ReplyKeyboardMarkup keyboardMarkup;
    List<KeyboardRow> rowList;

    Keyboard(int cntOfRows){
        keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        rowList = new ArrayList<>();
        for(int row = 0;row<cntOfRows; row++) { //создаем ряды
            rowList.add(new KeyboardRow());
        }
    }

    public void setKeyBoard(){
        keyboardMarkup.setKeyboard(rowList);
    }




    void setButtonsInRow(KeyboardRow row, String[] nameButton){ //заполняем ряды
        for (String s : nameButton) {
            KeyboardButton button = new KeyboardButton();
            button.setText(s);
            row.add(button);
        }
    }
    void setButtonsInRow(KeyboardRow row, String nameButton){ //заполняем ряд 1-й кнопкой
            KeyboardButton button = new KeyboardButton();
            button.setText(nameButton);
            row.add(button);

    }



    public ReplyKeyboardMarkup getKeyboard(){
        return keyboardMarkup;
    }

    public abstract void addStopButton();
    public abstract void removeStopButton();
}
