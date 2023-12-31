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
    private void scheduleMatches() {
        // Clear previous schedule
        matches.clear();
        int M = Math.max(2 * teams.size(), teams.size() * (teams.size() - 1) / 2); // Ensure M is at least twice the number of teams
        // Implementing a round-robin schedule
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                // Schedule match between team i and team j
                matches.add(new Match(teams.get(i), teams.get(j)));
            }
        }

        // Optionally, you can shuffle the matches to randomize the schedule
        Collections.shuffle(matches);
    }


    // Play all matches in the season
    /*public void playSeason() {
        if (isSeasonOver) {
            throw new IllegalStateException("Season has already been played.");
        }

        for (Match match : matches) {
            match.playMatch();
            // Here, update team records, standings, or any other relevant data
            // For example, you might increment the win count for the winning team
            Team winner = match.getWinner();
            // Increment win for winner, loss for loser, etc.
        }

        isSeasonOver = true;
        // Optionally, calculate final standings or other end-of-season tasks
    }*/
    
    public void playSeason() {
        if (isSeasonOver) {
            throw new IllegalStateException("Season already played.");
        }

        // Play regular season matches
        for (Match match : matches) {
            match.playMatch();
            // Update team records
            // ...
        }

        startPlayoffs(); // Start the playoff tournament

        isSeasonOver = true;
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
