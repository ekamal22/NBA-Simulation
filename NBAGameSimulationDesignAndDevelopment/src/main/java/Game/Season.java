package main.java.Game;

import java.util.ArrayList;
import java.util.Collections;
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
    public void playSeason() {
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
