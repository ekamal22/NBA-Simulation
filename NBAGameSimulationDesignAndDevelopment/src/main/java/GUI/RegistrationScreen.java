package main.java.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationScreen extends JPanel {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField ageField;
    private JButton registerButton;
    private MainApplication mainApp; // Reference to the main application

    public RegistrationScreen(MainApplication mainApp) {
        this.mainApp = mainApp;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);

        usernameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(usernameField, constraints);

        JLabel emailLabel = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(emailLabel, constraints);

        emailField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(emailField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(passwordField, constraints);

        JLabel nameLabel = new JLabel("Name:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(nameLabel, constraints);

        nameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(nameField, constraints);

        JLabel surnameLabel = new JLabel("Surname:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(surnameLabel, constraints);

        surnameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(surnameField, constraints);

        JLabel ageLabel = new JLabel("Age:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(ageLabel, constraints);

        ageField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 5;
        panel.add(ageField, constraints);

        registerButton = new JButton("Register");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        panel.add(registerButton, constraints);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });

        return panel;
    }

    private void performRegistration() {
    	String username = usernameField.getText();
    	String email = emailField.getText();
    	String password = new String(passwordField.getPassword());
    	String name = nameField.getText();  // Assuming you have a text field for name
    	String surname = surnameField.getText();  // Assuming you have a text field for surname
    	Integer age = null;
    	
    	try {
            // Attempt to parse age as an integer
            age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            // Handle the case where age is not a valid integer
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Implement your registration logic here
        if (validateRegistrationDetails(username, email, password, name, surname, age)) {
            // Registration logic
            // Show success message or navigate to login screen
            JOptionPane.showMessageDialog(this, "Registration Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Example: mainApp.showScreen("LoginScreen");
        } else {
            // Show error message
            JOptionPane.showMessageDialog(this, "Invalid or incomplete registration details", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateRegistrationDetails(String username, String email, String password, String name, String surname, int age) {
        // Check constraints for username
        if (!isValidUsername(username)) {
            return false;
        }

        // Check constraints for email
        if (!isValidEmail(email)) {
            return false;
        }

        // Check constraints for password
        if (!isValidPassword(password)) {
            return false;
        }

        // Check constraints for name and surname
        if (!isValidName(name) || !isValidName(surname)) {
            return false;
        }

        // Check constraints for age
        if (age < 12) {
            return false;
        }

        // All constraints passed
        return true;
    }

    private boolean isValidUsername(String username) {
        // Username can only include letter and number characters
        String usernameRegex = "^[a-zA-Z0-9]+$";
        return username.matches(usernameRegex);
    }

    private boolean isValidEmail(String email) {
        // Email address should be in the correct format
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        // Password should be at least eight characters, including letters, numbers, and special characters
        return password.length() >= 8 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*") && password.matches(".*[!@#$%^&*()_+\\-=[]{};':\",./<>?|\\\\`~]+.*");
    }

    private boolean isValidName(String name) {
        // Name and surname should have at least three characters (only letters)
        return name.length() >= 3 && name.matches("^[a-zA-Z]+$");
    }
}