package main.java.GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.java.Utils.AuthenticationService;

public class LoginScreen extends JPanel {
    // Components like text fields, labels, buttons
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private MainApplication app;
    private AuthenticationService authService;


    public LoginScreen(MainApplication app) {
        this.app = app;
        this.authService = new AuthenticationService();
        initializeComponents();
        // Initialize login components and layout
        // Add action listeners that call app.showScreen("OtherScreenName") for navigation
    }
    

    // Method to handle login
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

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(passwordField, constraints);

        loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(loginButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        return panel;
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (authService.authenticate(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            // mainApp.showScreen("NextScreenName"); // Navigate to another screen
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Implement your validation logic here
        // For now, let's assume any non-empty credentials are valid
        return !username.isEmpty() && !password.isEmpty();
    }
}