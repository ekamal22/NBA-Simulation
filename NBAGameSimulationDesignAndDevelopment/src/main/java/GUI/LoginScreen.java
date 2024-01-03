package main.java.GUI;

import main.java.User.User;
import main.java.User.UserManager;
import main.java.Utils.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.java.Utils.AuthenticationService;

public class LoginScreen extends JFrame {
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private LoginCallback loginCallback;
    private UserManager userManager;
    // Now using AuthenticationService for user authentication
    private AuthenticationService authenticationService;

    public LoginScreen(AuthenticationService authenticationService, UserManager userManager, LoginCallback callback) {
    	this.userManager = userManager;
        this.authenticationService = authenticationService;
        this.loginCallback = callback;
        setTitle("Login");
        setSize(300, 150);
        setLocationRelativeTo(null); // Center the window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        initializeComponents();
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Username
        add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        txtUsername = new JTextField(20);
        add(txtUsername, gbc);

        // Password
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        pwdPassword = new JPasswordField(20);
        add(pwdPassword, gbc);

        // Login Button
        gbc.gridy++;
        gbc.gridwidth = 1;
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        add(btnLogin, gbc);

        // Cancel Button
        gbc.gridx++;
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginScreen.this.dispose();
            }
        });
        add(btnCancel, gbc);

        pack(); // Adjusts window size to fit components
    }

    private void login() {
        // Get user input from text fields
        String username = txtUsername.getText();
        String password = new String(pwdPassword.getPassword());

        // Validate input and authenticate user using AuthenticationService
        boolean isAuthenticated = authenticationService.authenticate(username, password);

        if (isAuthenticated) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            User loggedInUser = userManager.getUser(username); 
            loginCallback.onLoginSuccess(loggedInUser);
            
            dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
        }
    }

    public static void main(String[] args) {
        
        AuthenticationService authService = new AuthenticationService();
        UserManager userManager = null; 
        LoginCallback loginCallback = null; 

        // Launch the login screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen(authService, userManager, loginCallback).setVisible(true);
            }
        });
    }
}