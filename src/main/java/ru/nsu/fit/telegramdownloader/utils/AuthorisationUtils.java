package ru.nsu.fit.telegramdownloader.utils;

//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class AuthorisationUtils {
    private final HashSet<Long> admins;
    private final HashMap<String, Long> tokens;// token -> user

    private AuthorisationUtils() {
        admins = FilesUtils.readNumberSetFile("adminlist.txt");
        tokens = FilesUtils.readMapFileSL("tokenlist.txt");
    }

    public boolean isAdmin(Long userId) {
        return admins.contains(userId);
    }

    public boolean trustedUser(Long userId) {
        return tokens.containsValue(userId);
    }

    private static class AuthorisationUtilsHolder {
        private final static AuthorisationUtils instance = new AuthorisationUtils();
    }

    public void generateToken(String token) {
        synchronized (tokens) {
            tokens.remove(token);
            tokens.put(token, (long) 0);
            writeTokens();
        }

    }
    public void addToken(String token,Long chatID) {
        synchronized (tokens) {
            tokens.remove(token);
            tokens.put(token, chatID);
            writeTokens();
        }

    }

    private void writeTokens() {
        try {
            FileWriter fStream = new FileWriter("tokenlist.txt", false);
            BufferedWriter info = new BufferedWriter(fStream);
            for (String token : tokens.keySet()) {
                info.write(token + ":" + tokens.get(token) + "\n");
            }
            info.close();
            fStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static AuthorisationUtils getInstance() {
        return AuthorisationUtilsHolder.instance;
    }

    public boolean isToken(String text) {
        return tokens.containsKey(text);
    }
    public boolean unusedToken(String text){
        return tokens.get(text).equals((long) 0);
    }
}
