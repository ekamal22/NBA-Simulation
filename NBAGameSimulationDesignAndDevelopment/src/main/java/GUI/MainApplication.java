package main.java.GUI;

import javax.swing.*;

import main.java.Game.Season;
import main.java.Team.Team;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplication extends JFrame {

    private JButton btnStartSeason;
    private JButton btnPauseResume;
    private Season currentSeason;
    private boolean isPaused;

    public MainApplication() {
        // Basic setup for the main window
        setTitle("NBA Game Simulation - Main Application");
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize season simulation variables
        currentSeason = null;
        isPaused = false;

        // Add menu buttons
        add(createMenuPanel(), BorderLayout.NORTH);

        // Other components and layout setup...
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3)); // Or any other suitable layout
        btnStartSeason = new JButton("Start Season");
        btnPauseResume = new JButton("Pause");
        JButton btnExit = new JButton("Exit");

        // Button to start the season simulation
        btnStartSeason.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (currentSeason == null || currentSeason.isSeasonOver()) {
                    // Initialize teams
                    List<Team> teams = initializeTeams();

                    // Initialize and start a new season
                    currentSeason = new Season(teams);
                    currentSeason.playSeason();
                }
            }
        });

        // Button to pause and resume the simulation
        btnPauseResume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentSeason != null && !currentSeason.isSeasonOver()) {
                    isPaused = !isPaused; // Toggle pause state
                    btnPauseResume.setText(isPaused ? "Resume" : "Pause");
                    // If you have a simulation thread or timer, pause or resume it here
                }
            }
        });

        // Button to exit the application
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the main window
            }
        });

        panel.add(btnStartSeason);
        panel.add(btnPauseResume);
        panel.add(btnExit);

        return panel;
    }
    
    private List<Team> initializeTeams() {
        List<Team> teams = new ArrayList<>();
        // You would add your Team objects to the list here, possibly retrieving them from a database or file
        // Example:
        teams.add(new Team("Lakers", "C:\\Users\\Effendi Jabid Kamal\\eclipse-workspace\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\Pics\\los-angeles-lakers-logo.png"));
        teams.add(new Team("Celtics", "C:\\Users\\Effendi Jabid Kamal\\eclipse-workspace\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\Pics\\Celtics logo.png"));
        teams.add(new Team("Sonics", "C:\\Users\\Effendi Jabid Kamal\\eclipse-workspace\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\Pics\\Seattle_SuperSonics_logo.png"));
        

        return teams;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainApplication().setVisible(true);
            }
        });
    }

    // Additional methods, getters, setters...
}
