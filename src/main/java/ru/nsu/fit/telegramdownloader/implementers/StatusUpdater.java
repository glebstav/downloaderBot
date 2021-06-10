package ru.nsu.fit.telegramdownloader.implementers;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsu.fit.telegramdownloader.DownloaderBot;
import ru.nsu.fit.telegramdownloader.buttons.InlineKeyboardSelectProtocol;
import ru.nsu.fit.telegramdownloader.buttons.Keyboard;
import ru.nsu.fit.telegramdownloader.utils.FilesUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StatusUpdater {
    private final long FILE_LIMIT = 50 * 1024 * 1024;
    private final Integer statusMessageId;
    private final DownloaderBot bot;
    private final String chatId;
    private final Keyboard keyboard;

    public StatusUpdater(String statusText, DownloaderBot bot, String chatId,Keyboard keyboard) throws TelegramApiException {
        this.keyboard = keyboard;
        SendMessage message = new SendMessage();
        keyboard.addStopButton();
        message.setReplyMarkup(keyboard.getKeyboard());
        bot.sendMessage(message,"Downloading...",chatId); //к сожалению нельзя редактировать сообщение в котором есть установленная клавиатура, поэтому еще одно
        statusMessageId = bot.sendMessage(statusText,chatId).getMessageId();
        this.bot = bot;
        this.chatId = chatId;


    }

    protected void updateStatus(String text) throws TelegramApiException {
        EditMessageText editMessageText = new EditMessageText(text);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(statusMessageId);
        bot.execute(editMessageText);
    }

    protected Integer getStatusMessageId() {
        return statusMessageId;
    }

    protected void uploadFile(String filename) throws TelegramApiException, IOException {
        keyboard.removeStopButton();
        if (FilesUtils.getFileSize(filename) < FILE_LIMIT) {
            _uploadFile(filename);
        } else {
            uploadSplitFiles(filename);
        }
    }

    // https://stackoverflow.com/questions/19177994/java-read-file-and-split-into-multiple-files
    protected void uploadSplitFiles(String filename) throws IOException, TelegramApiException {
        RandomAccessFile raf = new RandomAccessFile(filename, "r");
        long numSplits = FilesUtils.getFileSize(filename) / FILE_LIMIT + 1;
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize/numSplits ;
        long remainingBytes = sourceSize % numSplits;

        int maxReadBufferSize = 8 * 1024; // 8KB
        for(int destIx = 1; destIx <= numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(filename + "_" + destIx));
            if(bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit / maxReadBufferSize;
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                for(int i = 0; i < numReads; i++) {
                    readWrite(raf, bw, maxReadBufferSize);
                }
                if(numRemainingRead > 0) {
                    readWrite(raf, bw, numRemainingRead);
                }
            } else {
                readWrite(raf, bw, bytesPerSplit);
            }
            bw.close();
            _uploadFile(filename + "_" + destIx);
            Files.delete(Paths.get(filename + "_" + destIx));
        }
        if(remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("split."+(numSplits+1)));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }
        raf.close();
    }

    static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if(val != -1) {
            bw.write(buf);
        }
    }

    protected void _uploadFile(String filename) throws TelegramApiException {
        SendDocument sendFile = new SendDocument(chatId, new InputFile(new File(filename)));
        sendFile.setReplyToMessageId(getStatusMessageId());
        sendFile.setReplyMarkup(keyboard.getKeyboard());
        bot.execute(sendFile);
    }
}
