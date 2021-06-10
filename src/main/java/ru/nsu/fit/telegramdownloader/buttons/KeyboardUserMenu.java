package ru.nsu.fit.telegramdownloader.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class KeyboardUserMenu extends Keyboard {
    final static int CNT_OF_ROWS = 3;
    final static int ROW_WITH_STOP = 0;
    final static int ROW_WITH_STATS = 1;
    final static int ROW_WITH_HELP = 2;

    public final static String STATS = "Check statistics \uD83D\uDCC8";
    public final static String HELP = "Help\u2753";
    public final static String STOP = "Stop\u274C";
    public KeyboardUserMenu() {
        super(CNT_OF_ROWS);
        setButtonsInRow(rowList.get(ROW_WITH_STATS),STATS);
        setButtonsInRow(rowList.get(ROW_WITH_HELP),HELP);
        setKeyBoard();
    }
    public KeyboardUserMenu(int cntOfRows) {
        super(cntOfRows);
        setButtonsInRow(rowList.get(ROW_WITH_STATS),STATS);
        setButtonsInRow(rowList.get(ROW_WITH_HELP),HELP);
    }
    public void addStopButton(){
        setButtonsInRow(rowList.get(ROW_WITH_STOP),STOP);
        setKeyBoard();
    }

    public void removeStopButton(){
        rowList.remove(ROW_WITH_STOP);
        rowList.add(ROW_WITH_STOP,new KeyboardRow());
        setKeyBoard();
    }
}
