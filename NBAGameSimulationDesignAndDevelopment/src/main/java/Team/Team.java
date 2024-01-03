package main.java.Team;

import java.util.ArrayList;
import java.util.List;

import main.java.Player.Player;

public class Team {
    private String teamName;
    private List<Player> players;
    private String teamLogo; // Path to the team logo file
    private int wins;
    private int losses; 

    // Constructor
    public Team(String teamName, String teamLogo) {
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.players = new ArrayList<>();
    }
    
    public int compareTo(Team other) {
      
        return Integer.compare(other.getWins(), this.getWins());
    }
    
    public void addWin() {
        this.wins++;
    }
    
    public int getWins() {
        return this.wins;
    }
    
    public void addLoss() { //  method to increment the loss count
        this.losses++;
    }
    
    public int getLosses() { //  getter for losses
        return this.losses;
    }
    
    
    
    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    // Removes a player from the team
    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    // Gets the total score of the team
    public int calculateTeamScore() {
        int totalScore = 0;
        for (Player player : players) {
            totalScore += player.calculateScore();
        }
        return totalScore;
    }

    // Getters
    public String getTeamName() {
        return teamName;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Returns a copy to prevent external modification
    }

    // Setters
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Team{" +
               "teamName='" + teamName + '\'' +
               ", players=" + players +
               ", teamLogo='" + teamLogo + '\'' +
               '}';
    }

  
}
