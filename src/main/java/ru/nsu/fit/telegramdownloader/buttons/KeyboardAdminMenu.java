package ru.nsu.fit.telegramdownloader.buttons;

public class KeyboardAdminMenu extends KeyboardUserMenu {
    final static int CNT_OF_ROWS = 5;
    final static int ROW_WITH_ALLSTATS = 3;
    final static int ROW_WITH_GENERATE_TOKEN = 4;
    public final static String ALL_STATS = "Check ALL statistics\uD83D\uDCCA";
    public final static String GENERATE_TOKEN = "Generate token\u26A1";
    public KeyboardAdminMenu(){
        super(CNT_OF_ROWS);
        setButtonsInRow(rowList.get(ROW_WITH_ALLSTATS),ALL_STATS);
        setButtonsInRow(rowList.get(ROW_WITH_GENERATE_TOKEN),GENERATE_TOKEN);
        setKeyBoard();
    }
    public KeyboardAdminMenu(int cntOfRows){
        super(cntOfRows);
        setButtonsInRow(rowList.get(ROW_WITH_ALLSTATS),ALL_STATS);
        setButtonsInRow(rowList.get(ROW_WITH_GENERATE_TOKEN),GENERATE_TOKEN);
    }
}
