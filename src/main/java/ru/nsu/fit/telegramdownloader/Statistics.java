package ru.nsu.fit.telegramdownloader;

import ru.nsu.fit.telegramdownloader.utils.FilesUtils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Statistics implements AutoCloseable {
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Statistics.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }
    private final HashMap<Long, Long> data;

    public Statistics() {
        data = FilesUtils.readMapFileLL("stat.txt");
        LOGGER.info("stat: " + data);
    }

    public Long getUserStat(Long userId) {
        synchronized (data) {
            return data.getOrDefault(userId, 0L);
        }
    }

    public void updateUserStat(Long userId, Long downloadData) {
        synchronized (data) {
            data.put(userId, data.getOrDefault(userId, 0L) + downloadData);
        }
    }

    // https://stackoverflow.com/questions/50257374/how-do-i-write-multiple-lines-to-a-text-file-in-java
    @Override
    public void close() throws Exception {
        FileWriter fStream = new FileWriter("stat.txt", false);
        BufferedWriter info = new BufferedWriter(fStream);
        for (Long userId : data.keySet()) {
            info.write(userId + ":" + data.get(userId) + "\n");
        }
        info.close();
        fStream.close();
    }

    public HashMap<Long, Long> getStat() {
        synchronized (data) {
            return new HashMap<>(data);
        }
    }
}
