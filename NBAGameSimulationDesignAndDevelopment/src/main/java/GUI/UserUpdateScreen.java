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

    // Assume UserManager is a class that handles updating user details
    private UserManager userManager;
    private String getCurrentUserNickname() {
        return currentUser.getNickname();
    }

    // Method to get the current user's surname
    private String getCurrentUserSurname() {
        return currentUser.getSurname();
    }

    // Method to get the current user's age
    private int getCurrentUserAge() {
        return currentUser.getAge();
    }
    public UserUpdateScreen(UserManager userManager, User currentUser) {
        this.userManager = userManager;
        this.currentUser = currentUser;
        initializeComponents();
        setTitle("Update User Information");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2)); // Simple grid layout
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create UI components
        add(new JLabel("New Password:"));
        pwdPassword = new JPasswordField();
        add(pwdPassword);

        add(new JLabel("New Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Profile Photo:"));
        txtProfilePhoto = new JTextField();
        add(txtProfilePhoto);

        btnUpdate = new JButton("Update");
        btnCancel = new JButton("Cancel");

        // Add action listener for the update button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the current user's details (nickname, surname, age)
                String nickname = getCurrentUserNickname(); // Implement this method to retrieve the nickname
                String surname = getCurrentUserSurname();  // Implement this method to retrieve the surname
                int age = getCurrentUserAge();             // Implement this method to retrieve the age

                // Get the user input from the text fields
                String newPassword = new String(pwdPassword.getPassword());
                String newEmail = txtEmail.getText();
                String newProfilePhoto = txtProfilePhoto.getText();

                // Validate and update user details
                boolean success = userManager.updateUser(nickname, newPassword, surname, age, newEmail, newProfilePhoto);     

                if (success) {
                    JOptionPane.showMessageDialog(UserUpdateScreen.this, "User updated successfully!");
                    dispose(); // Close the update screen
                } else {
                    JOptionPane.showMessageDialog(UserUpdateScreen.this, "Failed to update user.");
                }
            }
        });
        

        // Add action listener for the cancel button
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the update screen
            }
        });

        add(btnUpdate);
        add(btnCancel);

        // Show the window
        setVisible(true);
    }
    
    public UserUpdateScreen(UserManager userManager) {
        this(userManager, null); // Calls the two-argument constructor with null for the User
        // Note: In this case, you must handle a potential null User within your methods
     // Set up the frame
        setTitle("Update User Information");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize UI components
        initializeComponents();
    }
    private void initializeComponents() {
    	// Use a GridBagLayout for flexibility
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Components are added with GridBagLayout constraints to control position and size
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4); // Margins around components

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        pwdPassword = new JPasswordField(20);
        add(pwdPassword, gbc);

        // Email Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("New Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);

        // Profile Photo Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Profile Photo:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
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
                // ...
            }
        });
        add(btnUpdate, gbc);

        // Cancel Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
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
        // Assume you have a UserManager instance called userManager
        UserManager userManager = new UserManager(); // Replace with actual instantiation

        // Start the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserUpdateScreen(userManager);
            }
        });
    }
}
