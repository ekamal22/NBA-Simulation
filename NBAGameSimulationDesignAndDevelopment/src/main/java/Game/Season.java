package main.java.Game;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import main.java.GUI.MatchScreen;
import main.java.Team.Team;

public class Season {
    private List<Team> teams;
    private List<Match> matches;
    private boolean isSeasonOver;
    private Timer matchTimer;

    // Constructor
    public Season(List<Team> teams) {
        this.teams = new ArrayList<>(teams); // Create a copy of the list to avoid external modifications
        this.matches = new ArrayList<>();
        this.isSeasonOver = false;
        scheduleMatches(); // Schedule matches for the season
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
    private void scheduleMatches() {
        matches.clear();
        // Shuffle and divide teams into groups
        Collections.shuffle(teams);
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
    	matchTimer = new Timer(100, event -> {
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
                SwingUtilities.invokeLater(() -> displayPlayoffTree(playoffTeams)); // Also invoke this on the EDT
            }
        });
        matchTimer.setInitialDelay(0);
        matchTimer.start();
    }
    
    private List<Team> determinePlayoffTeams() {
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
    private void displayPlayoffTree(List<Team> playoffTeams) {
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

        // This will display a dialog box with the playoff tree
        JOptionPane.showMessageDialog(null, treeBuilder.toString(), "Playoff Tree", JOptionPane.INFORMATION_MESSAGE);
    }


	private void startPlayoffs() {
        List<Team> playoffTeams = getTopTeams(8); // Get the top 8 teams based on regular season performance
        List<Match> playoffMatches = new ArrayList<>();

        // Conduct elimination rounds
        while (playoffTeams.size() > 1) {
            List<Team> winners = new ArrayList<>();

            for (int i = 0; i < playoffTeams.size(); i += 2) {
                Team team1 = playoffTeams.get(i);
                Team team2 = playoffTeams.get(i + 1);

                Match match = new Match(team1, team2);
                match.playMatch();
                playoffMatches.add(match);
                winners.add(match.getWinner());
            }

            playoffTeams = winners; // Proceed with the winners to the next round
        }

        // The final team in playoffTeams is the champion
        // Optionally, store the champion or display the playoff tree
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
