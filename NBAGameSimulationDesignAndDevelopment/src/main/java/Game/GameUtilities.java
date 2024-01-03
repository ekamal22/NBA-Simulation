package main.java.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.java.Player.Player;
import main.java.Team.Team;

public class GameUtilities {
    private static final Random random = new Random();

    // Generates a random score for a player based on their stats and a random deviation
    public static int generatePlayerScore(Player player) {
        int deviationRange = 5;
        int deviation = random.nextInt(deviationRange * 2 + 1) - deviationRange;

        int score = player.calculateScore() + deviation;
        return Math.max(score, 0); // Ensures the score is non-negative
    }

    // Simulates a match between two teams and returns the team with the higher score
    public static Team simulateMatch(Team team1, Team team2) {
        int score1 = calculateTeamScore(team1);
        int score2 = calculateTeamScore(team2);

        return score1 > score2 ? team1 : team2;
    }

    // Calculates the total score for a team by summing the scores of all players
    protected static int calculateTeamScore(Team team) {
        int totalScore = 0;
        for (Player player : team.getPlayers()) {
            totalScore += generatePlayerScore(player);
        }
        return totalScore;
    }

    // Simulates an entire season, including matches and updating season statistics
    public static void simulateSeason(List<Team> teams) {
        // Initializes season stats for each team

        for (Team team1 : teams) {
            for (Team team2 : teams) {
                if (!team1.equals(team2)) {
                    Team winner = simulateMatch(team1, team2);
                    // Updates season stats based on the match result
                }
            }
        }

        
    }

    // Simulates playoff rounds using a single-elimination format and determines the champion
    public static Team simulatePlayoffs(List<Team> playoffTeams) {
        while (playoffTeams.size() > 1) {
            List<Team> nextRoundTeams = new ArrayList<>();

            for (int i = 0; i < playoffTeams.size(); i += 2) {
                Team winner = simulateMatch(playoffTeams.get(i), playoffTeams.get(i + 1));
                nextRoundTeams.add(winner); // Advances the winner to the next round
            }

            playoffTeams = nextRoundTeams;
        }

        return playoffTeams.isEmpty() ? null : playoffTeams.get(0); // Declares the last team standing as the champion
    }

   
}
