package main.java.GUI;

import main.java.User.UserManager;
import main.java.Game.Match;
import main.java.Game.Season;
import main.java.Team.Team;
import main.java.Team.TeamManager;
import main.java.User.User;
import main.java.Utils.AuthenticationService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends JFrame {

    private UserManager userManager;
    private TeamManager teamManager;
    private User currentUser;
    private Team currentUserTeam;
    private JMenuItem draftUserTeamItem;
    JFrame parentComponent;
    Season currentSeason;
	List<Team> playoffTeams;
    
    public MainApplication() {
        userManager = new UserManager();
        teamManager = new TeamManager();
        currentUser = null;
        teamManager.setParentComponent(this);
        setTitle("NBA Game Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
     // Create an ImagePanel with the path to your image
        ImagePanel backgroundPanel = new ImagePanel("C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Fullscreen Image.png");
     // Add the ImagePanel to the frame
        add(backgroundPanel, BorderLayout.CENTER);
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
        
        if (teamManager.getParentComponent() == null) {
            JOptionPane.showMessageDialog(this, "The parent component has not been set.");
            return;
        }        
        // Assuming there's a method in TeamManager to start the season
        teamManager.setPlayoffCallback(this::openPlayoffSimulationScreen);
        boolean seasonStarted = teamManager.startSeason();

        if (seasonStarted) {
            JOptionPane.showMessageDialog(this, "Season has started successfully!");
            
            // Instantiate currentSeason with the callback to open playoff simulation
            List<Team> teams = teamManager.getTeams(); // Assume you get the teams list from your teamManager
            currentSeason = new Season(this, teams, this::openPlayoffSimulationScreen); // The `this` context is MainApplication

            openMatchSeasonScreen(); // Open the screen to display regular season matches
            /*if (currentSeason != null) {
                List<Team> playoffTeams = currentSeason.determinePlayoffTeams();
                currentSeason.displayPlayoffTree(this, playoffTeams, this::openPlayoffSimulationScreen);
            }*/
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
                currentUserTeam = new Team("User Team", "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Default pfp.png"); // Or however you determine the logo
                teamManager.createTeam(currentUserTeam.getTeamName(), currentUserTeam.getTeamLogo());
            }
            DraftScreen draftScreen = new DraftScreen(teamManager, currentUserTeam);
            draftScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please log in first.");
        }
    }
    private void openMatchScreen(List<Match> matches) {
        if (matches != null && !matches.isEmpty()) {
            MatchScreen matchScreen = new MatchScreen(matches, teamManager); // Pass the list of matches
            matchScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No matches to display.");
        }
    }

    private void openMatchSeasonScreen() {
        List<Match> seasonMatches = teamManager.getCurrentSeason().getMatches();
        if (seasonMatches != null && !seasonMatches.isEmpty()) {
            MatchScreen matchScreen = new MatchScreen(seasonMatches, teamManager); // Pass teamManager here
            matchScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No matches scheduled for the season.");
        }
    }
    /*private void openPlayoffSimulationScreen() {
        Season currentSeason = teamManager.getCurrentSeason();
        
        // Ensure the current season is available and has finished regular matches
        if (currentSeason != null && currentSeason.isSeasonOver()) {
            // Get the top teams for playoffs
            List<Team> playoffTeams = currentSeason.determinePlayoffTeams();

            // Prepare the matches for the playoff rounds
            List<Match> playoffMatches = new ArrayList<>();
            for (int i = 0; i < playoffTeams.size(); i += 2) {
                Team team1 = playoffTeams.get(i);
                Team team2 = playoffTeams.get(i + 1);

                // Create a match between each pair of teams
                playoffMatches.add(new Match(team1, team2));
            }

            // Initiate the playoff simulation screen
            PlayoffSimulationScreen simulationScreen = new PlayoffSimulationScreen(this, playoffMatches, "");
            simulationScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Playoff matches are not yet available or the season has not ended.");
        }
    }*/
    
    public void openPlayoffSimulationScreen(List<Team> playoffTeams) {
        List<Match> playoffMatches = new ArrayList<>();
        for (int i = 0; i < playoffTeams.size(); i += 2) {
            Team team1 = playoffTeams.get(i);
            Team team2 = playoffTeams.get(i + 1);
            playoffMatches.add(new Match(team1, team2));
        }

        PlayoffSimulationScreen simulationScreen = new PlayoffSimulationScreen(this, playoffMatches, "");
        simulationScreen.setVisible(true);
    }

    // Example of using displayPlayoffTree with the callback
    // Assuming currentSeason is an instance of Season
    


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
