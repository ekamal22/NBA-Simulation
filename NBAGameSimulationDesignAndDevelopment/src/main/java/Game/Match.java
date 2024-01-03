package main.java.Game;

import main.java.Team.Team;

public class Match {
    private Team team1;
    private Team team2;
    private Team winner;
    private int scoreTeam1;
    private int scoreTeam2;
    private boolean isPlayed;

    // Constructs a Match instance with two teams
    public Match(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        this.isPlayed = false;
    }

    // Simulates the match and determines the winner and scores
    public void playMatch() {
        scoreTeam1 = GameUtilities.calculateTeamScore(team1);
        scoreTeam2 = GameUtilities.calculateTeamScore(team2);

        winner = scoreTeam1 > scoreTeam2 ? team1 : team2;
        isPlayed = true;
    }

    // Accessor methods
    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    // Returns the winner, throws exception if match not played
    public Team getWinner() {
        if (!isPlayed) {
            throw new IllegalStateException("Match has not been played yet.");
        }
        return winner;
    }
    
    // Returns the loser, throws exception if match not played
    public Team getLoser() {
        if (!isPlayed) {
            throw new IllegalStateException("Match has not been played yet.");
        }
        return winner == team1 ? team2 : team1;
    }

    // Returns the score of team 1, throws exception if match not played
    public int getScoreTeam1() {
        if (!isPlayed) {
            throw new IllegalStateException("Match has not been played yet.");
        }
        return scoreTeam1;
    }

    // Returns the score of team 2, throws exception if match not played
    public int getScoreTeam2() {
        if (!isPlayed) {
            throw new IllegalStateException("Match has not been played yet.");
        }
        return scoreTeam2;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    // toString method for displaying match details
    @Override
    public String toString() {
        return "Match{" +
               "team1=" + team1.getTeamName() +
               ", team2=" + team2.getTeamName() +
               ", winner=" + (isPlayed ? winner.getTeamName() : "N/A") +
               ", scoreTeam1=" + (isPlayed ? scoreTeam1 : "N/A") +
               ", scoreTeam2=" + (isPlayed ? scoreTeam2 : "N/A") +
               ", isPlayed=" + isPlayed +
               '}';
    }

    
}
