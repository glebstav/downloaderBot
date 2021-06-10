package ru.nsu.fit.telegramdownloader.buttons;


public class InlineKeyboardSelectProtocol extends InlineKeyboard {
    final static int CNT_OF_ROWS = 1;
    final static int ROW_WITH_SELECT = 0;
    final static String[] NAME_OF_SELECTING_DATA = {".torrent","magnet-link"};
    final static String[] CALLBACK_DATA = {"TORRENT","MAGNET"};

    public InlineKeyboardSelectProtocol() {
        super(CNT_OF_ROWS);
        SetButtonsInRow(rowList.get(ROW_WITH_SELECT),NAME_OF_SELECTING_DATA,CALLBACK_DATA);
        SetInlineKeyBoard();
    }


}
