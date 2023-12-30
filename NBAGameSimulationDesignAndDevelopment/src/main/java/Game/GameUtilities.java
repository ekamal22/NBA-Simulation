package main.java.Game;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.java.Player.Player;
import main.java.Team.Team;

public class GameUtilities {
    private static final Random random = new Random();

    // Method to generate a random score for a player
    public static int generatePlayerScore(Player player) {
        // Random factor (e.g., -5 to 5 points deviation)
        int deviationRange = 5;
        int deviation = random.nextInt(deviationRange * 2 + 1) - deviationRange;

        // Calculate score based on player stats and random deviation
        int score = player.calculateScore() + deviation;
        return Math.max(score, 0); // Ensure the score is not negative
    }


    // Simulate a match between two teams
    public static Team simulateMatch(Team team1, Team team2) {
        int score1 = calculateTeamScore(team1);
        int score2 = calculateTeamScore(team2);

        // Return the team with the higher score
        return score1 > score2 ? team1 : team2;
    }

    // Calculate the score for a team
    protected static int calculateTeamScore(Team team) {
        int totalScore = 0;
        for (Player player : team.getPlayers()) {
            totalScore += generatePlayerScore(player);
        }
        return totalScore;
    }

    // Simulate an entire season
    public static void simulateSeason(List<Team> teams) {
        // Reset or initialize season stats for each team
        // (This might involve setting win/loss records to 0, etc.)

        // Simulate matches between each pair of teams
        for (Team team1 : teams) {
            for (Team team2 : teams) {
                if (!team1.equals(team2)) {
                    Team winner = simulateMatch(team1, team2);
                    // Update season stats based on match result
                    // e.g., increment win count for the winner and loss count for the loser
                }
            }
        }

        // Additional season logic (e.g., ranking teams based on wins/losses)
    }

    // Simulate playoff rounds
    public static Team simulatePlayoffs(List<Team> playoffTeams) {
        // Assuming a single-elimination format
        while (playoffTeams.size() > 1) {
            List<Team> nextRoundTeams = new ArrayList<>();

            for (int i = 0; i < playoffTeams.size(); i += 2) {
                // Simulate a match between each pair of teams
                Team winner = simulateMatch(playoffTeams.get(i), playoffTeams.get(i + 1));
                nextRoundTeams.add(winner); // Add the winner to the next round
            }

            playoffTeams = nextRoundTeams; // Update the list of teams for the next round
        }

        return playoffTeams.isEmpty() ? null : playoffTeams.get(0); // The last team is the champion
    }

    // Additional utility methods can be added as needed...
}
