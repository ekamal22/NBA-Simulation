package main.java.GUI;

import main.java.User.UserManager;
import main.java.Game.Match;
import main.java.Team.Team;
import main.java.Team.TeamManager;
import main.java.User.User;
import main.java.Utils.AuthenticationService;
import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame {

    private UserManager userManager;
    private TeamManager teamManager;
    private User currentUser;
    private Team currentUserTeam;
    private JMenuItem draftUserTeamItem;
    
    
    public MainApplication() {
        userManager = new UserManager();
        teamManager = new TeamManager();
        currentUser = null;

        setTitle("NBA Game Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeMenu();
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("User");
        JMenuItem loginItem = new JMenuItem("Login");
        JMenuItem registerItem = new JMenuItem("Register");

        loginItem.addActionListener(e -> openLoginScreen());
        registerItem.addActionListener(e -> openRegistrationScreen());

        userMenu.add(loginItem);
        userMenu.add(registerItem);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);
        // User Draft Team Menu Item
        draftUserTeamItem = new JMenuItem("Draft Your Team");
        draftUserTeamItem.addActionListener(e -> openDraftScreen());
        draftUserTeamItem.setEnabled(false); // Initially disabled
        userMenu.add(draftUserTeamItem);
    }

    // Revised openLoginScreen method
    private void openLoginScreen() {
        AuthenticationService authService = new AuthenticationService(); 
        LoginScreen loginScreen = new LoginScreen(authService, userManager, new LoginCallback() {
            @Override
            public void onLoginSuccess(User user) {
                currentUser = user;
                updateMenuForLoggedInUser();
            }
        });
        loginScreen.setVisible(true);
    }
    

    // Method to update the menu after login
    private void updateMenuForLoggedInUser() {
        JMenuBar menuBar = new JMenuBar();
        JMenu userMenu = new JMenu("User");
        JMenuItem updateUserItem = new JMenuItem("Update Profile");
        JMenuItem startSeasonItem = new JMenuItem("Start Season");
        JMenuItem viewTeamItem = new JMenuItem("View Team");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem draftUserTeamItem = new JMenuItem("Draft Your Team");
        updateUserItem.addActionListener(e -> openUpdateUserScreen());
        draftUserTeamItem.addActionListener(e -> openDraftScreen());
        startSeasonItem.addActionListener(e -> startSeason());
        viewTeamItem.addActionListener(e -> openTeamViewScreen());
        exitItem.addActionListener(e -> System.exit(0));

        userMenu.add(updateUserItem);
        userMenu.add(startSeasonItem);
        userMenu.add(viewTeamItem);
        userMenu.add(draftUserTeamItem);
        userMenu.add(exitItem);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);

        // Repaint the frame to update the menu bar
        invalidate();
        validate();
        repaint();
    }
    // Method to handle the start of the season
 // Inside MainApplication class

    private void startSeason() {
    	teamManager.printTeamSizes();
        // Check if the user's team is ready
        if (!teamManager.isTeamReady(currentUserTeam)) {
            JOptionPane.showMessageDialog(this, "Your team is not ready. Please draft players first.");
            openDraftScreen();
            return;
        }
        if (teamManager.getNumberOfTeams() < 16) {
            JOptionPane.showMessageDialog(this, "Not all teams have been created.");
            return;
        }

        if (!teamManager.isSeasonReady()) {
            JOptionPane.showMessageDialog(this, "Not all teams are ready. Please draft players first.");
            
            return;
        }
        // Assuming there's a method in TeamManager to start the season
        boolean seasonStarted = teamManager.startSeason();

        if (seasonStarted) {
            JOptionPane.showMessageDialog(this, "Season has started successfully!");
            // You might want to open the match/season screen here or perform other actions
            openMatchSeasonScreen();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to start the season. Please try again.");
        }
    }

    private void openUpdateUserScreen() {
        if (currentUser != null) {
            UserUpdateScreen updateScreen = new UserUpdateScreen(userManager, currentUser);
            updateScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please log in first.");
        }
    }
    

    private void openTeamViewScreen() {
        if (currentUser != null && currentUserTeam != null) { // Check if the user has a team
            TeamViewScreen teamViewScreen = new TeamViewScreen(currentUserTeam);
            teamViewScreen.setVisible(true);
            System.out.println(TeamManager.getNumberOfTeams());
        } else {
            JOptionPane.showMessageDialog(this, "Please log in and form a team first.");
        }
    }
    
    private void openDraftScreen() {
        if (currentUser != null) {
            if (currentUserTeam == null) {
                currentUserTeam = new Team("User Team", "path_to_logo"); // Or however you determine the logo
                teamManager.createTeam(currentUserTeam.getTeamName(), currentUserTeam.getTeamLogo());
            }
            DraftScreen draftScreen = new DraftScreen(teamManager, currentUserTeam);
            draftScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please log in first.");
        }
    }
    private void openMatchScreen(Match match) {
        MatchScreen matchScreen = new MatchScreen(match);
        matchScreen.setVisible(true);
    }

    private void openMatchSeasonScreen() {
        // Assuming you have logic to get a current match or create a new one for demonstration
        Match currentMatch = getCurrentMatch(); // Implement this method based on your game logic
        if (currentMatch != null) {
            openMatchScreen(currentMatch);
        } else {
            JOptionPane.showMessageDialog(this, "No current match to display.");
        }
    }

    // Method to get or create a current match (this is just a placeholder, implement according to your logic)
    private Match getCurrentMatch() {
        // Sample teams for testing
        Team team1 = new Team("Celtics", "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/mainresources/Pics/Celtics logo.png");
        Team team2 = new Team("Lakers", "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/mainresources/Pics/los-angeles-lakers-logo.png");

        // Create a new match for demonstration
        return new Match(team1, team2);
    }
    private void openRegistrationScreen() {
        RegistrationScreen registrationScreen = new RegistrationScreen(userManager);
        registrationScreen.setVisible(true);
        // Handle post-registration actions
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApplication().setVisible(true));
    }
}
