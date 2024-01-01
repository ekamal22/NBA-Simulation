package main.java.GUI;

import javax.swing.*;
import main.java.User.User;
import main.java.User.UserManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserUpdateScreen extends JFrame {
    private JTextField txtEmail;
    private JPasswordField pwdPassword;
    private JTextField txtProfilePhoto;
    private JButton btnUpdate;
    private JButton btnCancel;
    private User currentUser;
    private UserManager userManager;

    public UserUpdateScreen(UserManager userManager, User currentUser) {
        this.userManager = userManager;
        this.currentUser = currentUser;
        setTitle("Update User Information");
        setSize(300, 200);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new GridBagLayout()); // Use GridBagLayout for layout management
        GridBagConstraints gbc = new GridBagConstraints();

        // Common settings for GridBagConstraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        pwdPassword = new JPasswordField(20);
        add(pwdPassword, gbc);

        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("New Email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);

        // Profile Photo Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Profile Photo:"), gbc);

        gbc.gridx = 1;
        txtProfilePhoto = new JTextField(20);
        add(txtProfilePhoto, gbc);

        // Update Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the update action
            }
        });
        add(btnUpdate, gbc);

        // Cancel Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the update screen
            }
        });
        add(btnCancel, gbc);
    }


    // Main method for testing
    public static void main(String[] args) {
        UserManager userManager = new UserManager(); // Replace with actual instantiation
        SwingUtilities.invokeLater(() -> {
            new UserUpdateScreen(userManager, null).setVisible(true);
        });
    }
}
