package ru.nsu.fit.telegramdownloader.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class FilesUtils {
    static public void mkDir(String name) {
        File f = new File(name);
        f.mkdir();
    }

    static public long getFileSize(String filename) throws IOException {
        return Files.size(Paths.get(filename));
    }

    static public HashMap<String, Long> readMapFileSL(String filename) {
        HashMap<String, Long> result = new HashMap<>();
        BufferedReader br;
        try {
            br = getReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        try {
            String line = br.readLine();
            while (line != null) {
                result.put(line.substring(0, line.indexOf(':')), Long.parseLong(line.substring(line.indexOf(':') + 1)));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    static public HashMap<Long, Long> readMapFileLL(String filename) {
        HashMap<Long, Long> result = new HashMap<>();
        BufferedReader br;
        try {
            br = getReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        try {
            String line = br.readLine();
            while (line != null) {
                result.put(Long.parseLong(line.substring(0, line.indexOf(':'))), Long.parseLong(line.substring(line.indexOf(':') + 1)));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    static public HashSet<Long> readNumberSetFile(String filename) {
        HashSet<Long> result = new HashSet<>();
        BufferedReader br;
        try {
            br = getReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }
        try {
            String line = br.readLine();
            while (line != null) {
                result.add(Long.parseLong(line));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static BufferedReader getReader(String filename) throws FileNotFoundException {
        File file;
        if (FilesUtils.class.getClassLoader().getResource(filename) != null) {
            file = new File(Objects.requireNonNull(FilesUtils.class.getClassLoader().getResource(filename)).getFile());
        } else {
            file = new File(filename);
        }
        return new BufferedReader(new FileReader(file));
    }

    // https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java
    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
}
