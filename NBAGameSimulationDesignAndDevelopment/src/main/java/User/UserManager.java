package main.java.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;
    private final String usersFilePath = "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/DataFiles/Users.txt";

    // Constructor
    public UserManager() {
        users = new ArrayList<>();
        loadUsers();
    }

    // Loads users from the file
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(usersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = parseUser(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    // Parses a line of text into a User object
    private User parseUser(String line) {
        String[] parts = line.split(",");
        if (parts.length != 6) {
            return null; // or handle error
        }

        String nickname = parts[0];
        String password = parts[1];
        String realName = parts[2];
        String surname = parts[3];
        int age = Integer.parseInt(parts[4]);
        String email = parts[5];

        return new User(nickname, password, realName, surname, age, email);
    }

    // Registers a new user
    public boolean registerUser(String nickname, String password, String realName, String surname, int age, String email) {
        if (isUserExists(nickname)) {
            return false;
        }

        User newUser = new User(nickname, password, realName, surname, age, email);
        users.add(newUser);
        saveUser(newUser);
        return true;
    }

    // Checks if a user already exists
    private boolean isUserExists(String nickname) {
        return users.stream().anyMatch(user -> user.getNickname().equals(nickname));
    }

    // Saves a single user to the file
    private void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFilePath, true))) {
            writer.write(userToCsvLine(user) + "\n");
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    // Converts a User object to a CSV line
    private String userToCsvLine(User user) {
        return user.getNickname() + "," +
               user.getPassword() + "," +
               user.getRealName() + "," +
               user.getSurname() + "," +
               user.getAge() + "," +
               user.getEmail();
    }

    // Logs in a user
    public boolean loginUser(String nickname, String password) {
        return users.stream()
                    .anyMatch(user -> user.getNickname().equals(nickname) && user.getPassword().equals(password));
    }

    // Updates user information
    public boolean updateUser(String nickname, String newPassword, String newSurname, int newAge, String newEmail, String newProfilePhoto) {
        for (User user : users) {
            if (user.getNickname().equals(nickname)) {
                user.setPassword(newPassword);
                user.setSurname(newSurname);
                user.setAge(newAge);
                user.setEmail(newEmail);
                user.setProfilePhoto(newProfilePhoto);
                saveAllUsers(); // Saves all users to file
                return true;
            }
        }
        return false;
    }

    // Saves all users to the file
    private void saveAllUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFilePath))) {
            for (User user : users) {
                writer.write(userToCsvLine(user) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving all users: " + e.getMessage());
        }
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getNickname().equals(username)) {
                return user;
            }
        }
        return null; // Returns null if the user is not found
    }
}
