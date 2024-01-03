package main.java.Game;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import main.java.GUI.MatchScreen;
import main.java.GUI.PlayoffSimulationScreen;
import main.java.Team.Team;

public class Season {
    private List<Team> teams;
    private List<Match> matches;
    private boolean isSeasonOver;
    private Timer matchTimer;
    private int currentMatchIndex = 0;
    private boolean isPaused;
    private Component parentComponent;
    // Constructor
    public Season(Component parentComponent, List<Team> teams) {
        this.teams = new ArrayList<>(teams); // Create a copy of the list to avoid external modifications
        this.matches = new ArrayList<>();
        this.isSeasonOver = false;
        this.parentComponent = parentComponent;
        scheduleMatches(); // Schedule matches for the season
        
    }
    public boolean hasMoreMatches() {
        return currentMatchIndex < matches.size();
    }
    public void playNextMatch() {
    	if (isPaused || !hasMoreMatches()) {
            return;
        }
        	if (hasMoreMatches()) {
        		Match match = matches.get(currentMatchIndex);
        		match.playMatch();
        		updateTeamRecords(match);
        		currentMatchIndex++;

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
    	matchTimer = new Timer(1000, event -> {
            if (!matches.isEmpty()) {
                Match match = matches.remove(0);
                match.playMatch();
                updateTeamRecords(match);
                // Update GUI
                SwingUtilities.invokeLater(() -> MatchScreen.updateMatchResults(match)); // This needs to be done on the EDT
                // More code to update the GUI for wins/losses
            } else {
            	matchTimer.stop();
                ((Timer) event.getSource()).stop();
                // Now you need to create a list of playoff teams and pass it to displayPlayoffTree
                List<Team> playoffTeams = determinePlayoffTeams(); // You'll need to implement this method
                SwingUtilities.invokeLater(() -> displayPlayoffTree(this.parentComponent, playoffTeams, this)); // Also invoke this on the EDT
            }
        });
        matchTimer.setInitialDelay(0);
        
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
    private void displayPlayoffTree(Component parentComponent, List<Team> playoffTeams, Season currentSeason) {
        // Build the string representation of the playoff tree
        StringBuilder treeBuilder = new StringBuilder();
        int rounds = (int) (Math.log(playoffTeams.size()) / Math.log(2));
        int matchNumber = 1;

        for (int round = 0; round <= rounds; round++) {
            int roundMatches = (int) Math.pow(2, rounds - round);
            treeBuilder.append("Round ").append(round + 1).append(":\n");
            for (int match = 0; match < roundMatches; match++) {
                if (round * roundMatches + match < playoffTeams.size()) {
                    Team team = playoffTeams.get(round * roundMatches + match);
                    treeBuilder.append("Match ").append(matchNumber).append(": ")
                            .append(team.getTeamName()).append("\n");
                    matchNumber++;
                }
            }
            treeBuilder.append("\n");
        }

        // Ensure parentComponent is inside a JFrame
        JFrame parentFrame = null;
        if (parentComponent instanceof JFrame) {
            parentFrame = (JFrame) parentComponent;
        } else {
            parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parentComponent);
        }

        // Start the playoff simulation screen
        PlayoffSimulationScreen simulationScreen = new PlayoffSimulationScreen(parentFrame, getPlayoffMatches(playoffTeams), treeBuilder.toString());
        simulationScreen.setVisible(true);
    }
    
    private List<Match> getPlayoffMatches(List<Team> playoffTeams) {
        // Logic to prepare the matches for the playoff rounds
        List<Match> playoffMatches = new ArrayList<>();
        // ... [Logic to prepare the matches] ...
        return playoffMatches;
    }

    // Helper method to prepare playoff matches based on the teams
    private List<Match> preparePlayoffMatches(List<Team> teams) {
        // Similar logic as in the startPlayoffs method to create matches
        List<Match> matches = new ArrayList<>();
        // ... logic to prepare matches ...
        return matches;
    }



    private void startPlayoffs() {
        List<Team> playoffTeams = getTopTeams(8); // Get the top 8 teams
        List<Match> playoffMatches = new ArrayList<>();

        // Prepare the matches for the playoff rounds
        for (int i = 0; i < playoffTeams.size(); i += 2) {
            Team team1 = playoffTeams.get(i);
            Team team2 = (i + 1 < playoffTeams.size()) ? playoffTeams.get(i + 1) : null;
            if (team2 != null) {
                playoffMatches.add(new Match(team1, team2));
            }
        }

        // Ensure parentComponent is inside a JFrame
        JFrame parentFrame = (parentComponent instanceof JFrame) 
                             ? (JFrame) parentComponent 
                             : (JFrame) SwingUtilities.getWindowAncestor(parentComponent);

        // Start the playoff simulation screen with an empty string for the playoff tree
        PlayoffSimulationScreen simulationScreen = new PlayoffSimulationScreen(parentFrame, playoffMatches, "");
        simulationScreen.setVisible(true);

        // The champion will be determined and displayed in the simulation screen
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
