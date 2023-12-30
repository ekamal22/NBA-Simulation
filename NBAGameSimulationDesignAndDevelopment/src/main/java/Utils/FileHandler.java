package main.java.Utils;



import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    // Writes a list of strings to a file, each string on a new line
    public static void writeToFile(String filename, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // Reads lines from a file into a list of strings
    public static List<String> readFromFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
