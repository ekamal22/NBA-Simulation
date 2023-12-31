package main.java.Utils;

import main.java.User.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private static final String USERS_FILE = "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/DataFiles/Users.txt";
    private Map<String, User> users;

    public AuthenticationService() {
        users = new HashMap<>();
        loadUsersFromFile();
    }

    public boolean authenticate(String nickname, String password) {
        User user = users.get(nickname);
        return user != null && user.getPassword().equals(password);
    }

    public boolean registerUser(String nickname, String password, String realName, String surname, int age, String email) {
        if (!users.containsKey(nickname)) {
            users.put(nickname, new User(nickname, password, realName, surname, age, email));
            saveUsersToFile();
            return true;
        }
        return false;
    }

    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Ensure there are exactly 6 parts
                    String nickname = parts[0];
                    String password = parts[1];
                    String realName = parts[2];
                    String surname = parts[3];
                    int age = Integer.parseInt(parts[4]); // Convert string to integer
                    String email = parts[5];

                    users.put(nickname, new User(nickname, password, realName, surname, age, email));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing age from users file.");
            e.printStackTrace();
        }
    }


    private void saveUsersToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE, false))) {
            for (User user : users.values()) {
                writer.println(user.getNickname() + "," + user.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}