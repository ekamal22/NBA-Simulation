package main.java.Game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import main.java.GUI.MatchScreen;
import main.java.GUI.PlayoffSimulationScreen;
import main.java.Team.Team;
import main.java.Utils.Logging;

public class Season {
    private List<Team> teams;
    private List<Match> matches;
    private boolean isSeasonOver;
    private Timer matchTimer;
    private int currentMatchIndex = 0;
    private boolean isPaused;
    private Component parentComponent;
    private Consumer<List<Team>> openPlayoffSimulationCallback;
    private Logging regularSeasonLogger = new Logging("C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/DataFiles/regularSeasonMatches.log");

    // Constructor
    public Season(Component parentComponent, List<Team> teams, Consumer<List<Team>> openPlayoffSimulationCallback) {
        this.teams = new ArrayList<>(teams); // Create a copy of the list to avoid external modifications
        this.matches = new ArrayList<>();
        this.isSeasonOver = false;
        this.parentComponent = parentComponent;
        this.openPlayoffSimulationCallback = openPlayoffSimulationCallback;
        
        scheduleMatches(); // Schedule matches for the season
        
    }
    public boolean hasMoreMatches() {
        return currentMatchIndex < matches.size();
    }
    public void playNextMatch() {
    	System.out.println("playNextMatch() is called.");
    	if (isPaused || !hasMoreMatches()) {
            return;
        }
        	if (hasMoreMatches()) {
        		Match match = matches.get(currentMatchIndex);
        		match.playMatch();
        		updateTeamRecords(match);
        		currentMatchIndex++;
        		
        		String matchResult = String.format("Regular Season - Team %s vs Team %s - Winner: Team %s - Score: %d-%d",
                        match.getTeam1().getTeamName(),
                        match.getTeam2().getTeamName(),
                        match.getWinner().getTeamName(),
                        match.getScoreTeam1(),
                        match.getScoreTeam2());
regularSeasonLogger.log(matchResult);
        		
        		if (!hasMoreMatches()) {
                // Handle end of season logic here
        			calculateFinalStandings();
        			startPlayoffs();
        			isSeasonOver = true;
            }
        }
        	
    }
    
    
    // Schedule matches for the season
    /*private void scheduleMatches() {
        matches.clear(); // Clear previous schedule

        // Implementing a single round-robin schedule
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                // Schedule match between team i and team j
                matches.add(new Match(teams.get(i), teams.get(j)));
            }
        }

        Collections.shuffle(matches); // Randomize the schedule
    }*/
    
    public void startMatches() {
        if (!matchTimer.isRunning()) {
            matchTimer.start();
        }
    }
    private void scheduleMatches() {
        matches.clear();
        // Shuffle and divide teams into groups
        Collections.shuffle(teams);
        if (teams.size() != 16) {
            throw new IllegalStateException("Expected 16 teams, but found: " + teams.size());
        }
        List<Team> groupA = teams.subList(0, 4);
        List<Team> groupB = teams.subList(4, 8);
        List<Team> groupC = teams.subList(8, 12);
        List<Team> groupD = teams.subList(12, 16);

        scheduleGroupMatches(groupA);
        scheduleGroupMatches(groupB);
        scheduleGroupMatches(groupC);
        scheduleGroupMatches(groupD);
    }
    private void scheduleGroupMatches(List<Team> group) {
        for (int i = 0; i < group.size(); i++) {
            for (int j = i + 1; j < group.size(); j++) {
                matches.add(new Match(group.get(i), group.get(j)));
            }
        }
    }


    
    
    public void playSeason() {
        if (isSeasonOver) {
            throw new IllegalStateException("Season already played.");
        }

        for (Match match : matches) {
            match.playMatch();
            updateTeamRecords(match);
        }
        
        
        
        calculateFinalStandings();
        startPlayoffs(); // Start the playoff tournament
        isSeasonOver = true;
    }
    
    private void calculateFinalStandings() {
        // Sort teams based on wins (and other criteria if needed)
        Collections.sort(teams, (Team t1, Team t2) -> {
            // Compare based on wins, you can add more criteria here
            return Integer.compare(t2.getWins(), t1.getWins());
        });

        // Optionally, print final standings for debugging
        System.out.println("Final Standings:");
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            System.out.println((i + 1) + ". " + team.getTeamName() + " - Wins: " + team.getWins() + ", Losses: " + team.getLosses());
        }
    }
    
    private void updateTeamRecords(Match match) {
        Team winner = match.getWinner();
        Team loser = match.getLoser(); // Assuming you have a method to get the losing team

        if (winner != null && loser != null) {
            winner.addWin();
            // Add logic for loser if needed, e.g., incrementing loss count
            loser.addLoss();
        }
    }
    private List<Team> getTopTeams(int N) {
        // Create a copy of the teams list to avoid modifying the original list
        List<Team> sortedTeams = new ArrayList<>(teams);

        // Sort the teams based on their win count in descending order
        Collections.sort(sortedTeams, new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                // Assuming each team object has a method getWins() that returns the number of wins
                return Integer.compare(t2.getWins(), t1.getWins());
            }
        });

        // If N is greater than the number of teams, return the entire list
        if (N > sortedTeams.size()) {
            return sortedTeams;
        }

        // Return the top N teams from the sorted list
        return sortedTeams.subList(0, N);
    }
    
    public void playAndDelayMatches() {
        matchTimer = new Timer(200, event -> {
            if (hasMoreMatches()) {
                // Play the next match
                Match match = matches.get(currentMatchIndex);
                match.playMatch();
                updateTeamRecords(match);

                // Update GUI
                SwingUtilities.invokeLater(() -> MatchScreen.updateMatchResults(match));
                currentMatchIndex++;
            } else {
                // Stop the timer and check if the season is over
                matchTimer.stop();
                ((Timer) event.getSource()).stop();

                if (!isSeasonOver) {
                    // Season just ended, calculate final standings and start playoffs
                    calculateFinalStandings();
                    isSeasonOver = true;
                    List<Team> playoffTeams = determinePlayoffTeams();

                    // Display the playoff tree
                    SwingUtilities.invokeLater(() -> {
                        JFrame frame;
                        if (parentComponent instanceof JFrame) {
                            frame = (JFrame) parentComponent;
                        } else {
                            frame = (JFrame) SwingUtilities.getWindowAncestor(parentComponent);
                        }
                        displayPlayoffTree(frame, playoffTeams, openPlayoffSimulationCallback);
                    });
                }
            }
        });
        matchTimer.setInitialDelay(0);
        //matchTimer.start();
    }
    
    
    

    public void displayPlayoffTree(JFrame parentFrame, List<Team> playoffTeams, Consumer<List<Team>> onContinueCallback) {
        StringBuilder treeBuilder = new StringBuilder();
        // Logic to build the playoff tree string
        // For simplicity, let's assume it's a basic string representation
        treeBuilder.append("Playoff Tree\n");
        for (int i = 0; i < playoffTeams.size(); i++) {
            treeBuilder.append(playoffTeams.get(i).getTeamName()).append("\n");
        }

        // Display the playoff tree in a dialog or a separate window
        JTextArea playoffTreeTextArea = new JTextArea(treeBuilder.toString());
        playoffTreeTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(playoffTreeTextArea);

        // Creating a dialog to display the playoff tree
        JDialog dialog = new JDialog(parentFrame, "Playoff Tree", true);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        dialog.add(scrollPane);

        // Adding a continue button
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> {
        	if (openPlayoffSimulationCallback != null) {
                openPlayoffSimulationCallback.accept(playoffTeams); // Trigger the callback with the list of playoff teams
            } else {
                // You might want to handle the case where the callback is null more gracefully
                throw new IllegalStateException("Playoff simulation callback is not initialized.");
            }
            dialog.dispose();
            onContinueCallback.accept(playoffTeams); // Trigger the callback with the list of playoff teams
        });
        dialog.add(continueButton);

        // Set dialog size, location, and make it visible
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
    
    public void setPaused(boolean paused) {
        this.isPaused = paused;
        if (isPaused) {
            matchTimer.stop(); // Stop the timer when paused
        } else {
            if (hasMoreMatches()) {
                matchTimer.start(); // Start/resume the timer when unpaused
            }
        }
    }

    
    public List<Team> determinePlayoffTeams() {
        List<Team> sortedTeams = new ArrayList<>(this.teams);

        // Sort the teams based on their wins, then losses (if wins are equal)
        Collections.sort(sortedTeams, new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                // First compare by wins
                int winComparison = Integer.compare(t2.getWins(), t1.getWins());
                if (winComparison != 0) {
                    return winComparison;
                }
                // If wins are equal, compare by losses (fewer losses is better)
                return Integer.compare(t1.getLosses(), t2.getLosses());
            }
        });

        // Assuming you want the top 8 teams for the playoffs
        int numberOfPlayoffTeams = 8;
        return sortedTeams.subList(0, Math.min(numberOfPlayoffTeams, sortedTeams.size()));
    }
    
    @FunctionalInterface
    public interface PlayoffStartCallback {
        void startPlayoffSimulation(List<Team> playoffTeams);
    }

    

    private void startPlayoffs() {
        List<Team> playoffTeams = determinePlayoffTeams(); // Get the top teams for playoffs
        List<Match> playoffMatches = new ArrayList<>();

        // Prepare the matches for the playoff rounds
        for (int i = 0; i < playoffTeams.size(); i += 2) {
            Team team1 = playoffTeams.get(i);
            Team team2 = playoffTeams.get(i + 1);

            // Create a match between each pair of teams
            playoffMatches.add(new Match(team1, team2));
        }

        // Ensure parentComponent is inside a JFrame
        JFrame parentFrame = (parentComponent instanceof JFrame) 
                ? (JFrame) parentComponent 
                : (JFrame) SwingUtilities.getWindowAncestor(parentComponent);

        // Start the playoff simulation screen with an empty string for the playoff tree
        PlayoffSimulationScreen simulationScreen = new PlayoffSimulationScreen(parentFrame, playoffMatches, "");
        simulationScreen.setVisible(true);
    }
	


    // Getters
    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams); // Prevent external modification
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches); // Prevent external modification
    }

    public boolean isSeasonOver() {
        return isSeasonOver;
    }
    
    

    // Additional methods can be added as needed...

    // toString method for debugging
    @Override
    public String toString() {
        return "Season{" +
               "teams=" + teams +
               ", matches=" + matches +
               ", isSeasonOver=" + isSeasonOver +
               '}';
    }
}
