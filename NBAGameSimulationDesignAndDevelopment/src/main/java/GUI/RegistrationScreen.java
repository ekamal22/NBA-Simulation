package main.java.GUI;

import main.java.User.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationScreen extends JFrame {
    private JTextField txtNickname;
    private JPasswordField pwdPassword;
    private JTextField txtRealName;
    private JTextField txtSurname;
    private JTextField txtAge;
    private JTextField txtEmail;
    private JButton btnRegister;
    private JButton btnCancel;

    
    private UserManager userManager;

    public RegistrationScreen(UserManager userManager) {
        this.userManager = userManager;

        setTitle("Register New User");
        setSize(350, 250);
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

        // Nickname
        add(new JLabel("Nickname:"), gbc);
        gbc.gridy++;
        txtNickname = new JTextField(20);
        add(txtNickname, gbc);

        // Password
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        pwdPassword = new JPasswordField(20);
        add(pwdPassword, gbc);

        // Real Name
        gbc.gridy++;
        add(new JLabel("Real Name:"), gbc);
        gbc.gridy++;
        txtRealName = new JTextField(20);
        add(txtRealName, gbc);

        // Surname
        gbc.gridy++;
        add(new JLabel("Surname:"), gbc);
        gbc.gridy++;
        txtSurname = new JTextField(20);
        add(txtSurname, gbc);

        // Age
        gbc.gridy++;
        add(new JLabel("Age:"), gbc);
        gbc.gridy++;
        txtAge = new JTextField(20);
        add(txtAge, gbc);

        // Email
        gbc.gridy++;
        add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);

        // Register Button
        gbc.gridy++;
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        add(btnRegister, gbc);

        // Cancel Button
        gbc.gridx++;
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationScreen.this.dispose();
            }
        });
        add(btnCancel, gbc);

        pack(); // Adjusts window size to fit components
    }

    private void registerUser() {
        // Get user input from text fields
        String nickname = txtNickname.getText();
        String password = new String(pwdPassword.getPassword());
        String realName = txtRealName.getText();
        String surname = txtSurname.getText();
        int age = Integer.parseInt(txtAge.getText()); // This should include error handling for non-integer input
        String email = txtEmail.getText();

        // Validate input and register user
        boolean success = userManager.registerUser(nickname, password, realName, surname, age, email);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose(); // Close the registration window
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again with different credentials.");
        }
    }

    public static void main(String[] args) {
        // For testing purposes
        UserManager userManager = new UserManager(); 

        // Launch the registration screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistrationScreen(userManager).setVisible(true);
            }
        });
    }
}
