package main.java.User;

import java.util.regex.Pattern;

public class Validator {
    // Regular expressions for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MIN_AGE = 12;

    // Validate email format
    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Validate username (letters and numbers only)
    public static boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    // Validate password (length and complexity)
    public static boolean isValidPassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }

            if (hasLetter && hasDigit && hasSpecialChar) {
                return true;
            }
        }

        return false;
    }

    // Validate name (length and characters)
    public static boolean isValidName(String name) {
        return name.length() >= MIN_NAME_LENGTH && name.chars().allMatch(Character::isLetter);
    }

    // Validate age
    public static boolean isValidAge(int age) {
        return age >= MIN_AGE;
    }

    // Additional validation methods can be added as needed...
}

