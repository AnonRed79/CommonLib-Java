package net.anonhub.projects.dragonlib;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

public class Logger {
    private final Path file;

    public Logger(String filePath) {
        file = Paths.get(filePath);
        try {
            Files.createFile(file);
        } catch (IOException ignore) {}
    }

    public void log(Object textObject) {
        String now = LocalTime.now().toString().substring(0, 11);
        // Print to console
        System.out.println("[" + now + "] " + textObject.toString());

        // Append to file
        try {
            List<String> lines = Files.readAllLines(file);
            StringBuilder sb = new StringBuilder();
            for (String line:lines) {
                sb.append(line).append("\n");
            }
            sb.append("[").append(now).append("] ").append(textObject).append("\n");

            Files.writeString(file, sb.toString());

        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}
