package main.java.Team;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Player.Center;
import main.java.Player.Player;
import main.java.Player.PointGuard;
import main.java.Player.PowerForward;
import main.java.Player.ShootingGuard;
import main.java.Player.SmallForward;

public class TeamManager {
    private List<Team> teams;
    private Map<String, Player> availablePlayers; // Map of available players by their name

    // Constructor
    public TeamManager() {
        teams = new ArrayList<>();
        availablePlayers = new HashMap<>();
        // Initialize available players (presumably from a data source like a CSV file)
        loadPlayers();
    }

    // Load players into the availablePlayers map
    private void loadPlayers() {
        String csvFilePath = "C:\\Users\\Effendi Jabid Kamal\\eclipse-workspace\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\DataFiles\\2022-2023 NBA Player Stats - Regular.csv"; 
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] playerData = line.split(";");
                createPlayerFromData(playerData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions, perhaps log the error or notify the user
        }
    }

    private void createPlayerFromData(String[] data) {
        // Assuming the CSV columns of interest are Player (1), Position (2), PTS (29)
        try {
            String name = data[1]; // Player's name
            String position = data[2]; // Player's position
            double pts = Double.parseDouble(data[29]); // Convert string to double for Points per Game

            // You can add more statistics here as needed
            // For simplicity, let's use the same value for TRB, AST, BLK, STL
            double trb = pts; // Total Rebounds
            double ast = pts; // Assists
            double blk = pts; // Blocks
            double stl = pts; // Steals

            Player player = createPlayerByPosition(name, position, pts, trb, ast, blk, stl);
            availablePlayers.put(name, player);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            // Handle parsing errors here
        }
    }

    private Player createPlayerByPosition(String name, String position, double pts, double trb, double ast, double blk, double stl) {
        switch (position) {
            case "PG":
                return new PointGuard(name, pts, trb, ast, blk, stl);
            case "SG":
                return new ShootingGuard(name, pts, trb, ast, blk, stl);
            case "SF":
                return new SmallForward(name, pts, trb, ast, blk, stl);
            case "PF":
                return new PowerForward(name, pts, trb, ast, blk, stl);
            case "C":
                return new Center(name, pts, trb, ast, blk, stl);
            default:
                return null; // or handle unknown positions appropriately
        }
    }

    // Create a new team and add it to the list of teams
    public void createTeam(String teamName, String teamLogo) {
        Team newTeam = new Team(teamName, teamLogo);
        teams.add(newTeam);
    }

    // Draft a player to a team
    public boolean draftPlayerToTeam(String playerName, String teamName) {
        Team team = getTeamByName(teamName);
        Player player = availablePlayers.get(playerName);

        if (team != null && player != null) {
            team.addPlayer(player);
            availablePlayers.remove(playerName); // Remove the player from available players
            return true;
        }
        return false;
    }

    // Get a team by its name
    private Team getTeamByName(String teamName) {
        for (Team team : teams) {
            if (team.getTeamName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    // Getters
    public List<Team> getTeams() {
        return teams;
    }

    public Map<String, Player> getAvailablePlayers() {
        return new HashMap<>(availablePlayers); // Return a copy to prevent external modification
    }

    // Additional methods can be added as needed...
}