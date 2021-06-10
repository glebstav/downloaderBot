package ru.nsu.fit.telegramdownloader.exceptions;

public class ConfigurationFileException extends Exception {
    public ConfigurationFileException() {
        super("Error in file \"NameClassesCommand.info\"");
    }
    public ConfigurationFileException(Exception e) { super (e); }
}
