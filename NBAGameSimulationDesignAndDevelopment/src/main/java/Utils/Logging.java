package main.java.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logging {
    private String logFile;

    // Constructor to specify log file name
    public Logging(String fileName) {
        this.logFile = fileName;
    }

    public void log(String message) {
        try (FileWriter fw = new FileWriter(this.logFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            LocalDateTime dateTime = LocalDateTime.now();
            String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            out.println(timestamp + " - " + message);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
