package main.java.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.java.Team.Team;

public class Season {
    private List<Team> teams;
    private List<Match> matches;
    private boolean isSeasonOver;

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
