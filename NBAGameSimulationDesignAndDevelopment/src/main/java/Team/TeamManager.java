package main.java.Team;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.Game.Season;
import main.java.Player.Center;
import main.java.Player.Player;
import main.java.Player.PointGuard;
import main.java.Player.PowerForward;
import main.java.Player.ShootingGuard;
import main.java.Player.SmallForward;

public class TeamManager {
    private static List<Team> teams;
    private Map<String, Player> availablePlayers; // Map of available players by their name
    private final int MIN_PLAYERS_PER_TEAM = 5;
    private final int MAX_PLAYERS_PER_TEAM = 15;
    private static Season currentSeason;
    private Component parentComponent;

    // Constructor
    public TeamManager() {
        teams = new ArrayList<>();
        availablePlayers = new HashMap<>();
        // Initialize available players (presumably from a data source like a CSV file)
        loadPlayers();
        createAutoGeneratedTeams(); // New method to create teams automatically
        autoDraftForTeams();
    }
    private void createAutoGeneratedTeams() {
        String[] teamNames = {
                "Atlanta Hawks",
                "Boston Celtics",
                "Brooklyn Nets",
                "Charlotte Hornets",
                "Chicago Bulls",
                "Cleveland Cavaliers",
                "Dallas Mavericks",
                "Denver Nuggets",
                "Detroit Pistons",
                "Golden State Warriors",
                "Houston Rockets",
                "Indiana Pacers",
                "Los Angeles Clippers",
                "Los Angeles Lakers",
                "Seattle Super Sonics"
            };
        String[] teamLogos = {"C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Atlanta Hawks.png",
        		"C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Celtics logo.png",
        		"C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Brooklyn Nets.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Charlotte Hornets.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Chicago Bulls.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Cleveland Cavaliers.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Dallas Mavericks.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Denver Nuggets.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Detroit Pistons.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Golden State Warriors.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Houston Rockets.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Indiana Pacers.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Los Angeles Clippers.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Los Angeles-Lakers.png",
        	    "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Seattle Super Sonics.png"};
        for (int i = 0; i < 15; i++) {
            createTeam(teamNames[i], teamLogos[i]);
        }
    }
    public void autoDraftForTeams() {
        List<String> remainingPlayerNames = new ArrayList<>(availablePlayers.keySet());
        Collections.shuffle(remainingPlayerNames); // Randomize player order

        System.out.println("Starting auto-draft with " + remainingPlayerNames.size() + " available players.");

        while (!remainingPlayerNames.isEmpty()) {
            boolean playerDrafted = false;
            for (Team team : teams) {
                if (team.getPlayers().size() < MIN_PLAYERS_PER_TEAM) {
                    String playerName = remainingPlayerNames.remove(0);
                    boolean draftSuccess = draftPlayerToTeam(playerName, team.getTeamName());
                    if (draftSuccess) {
                        playerDrafted = true;
                        System.out.println("Drafted " + playerName + " to " + team.getTeamName());
                    } else {
                        System.out.println("Failed to draft " + playerName + " to " + team.getTeamName());
                    }
                }
                if (remainingPlayerNames.isEmpty()) {
                    System.out.println("No more players available for drafting.");
                    break; // Break out of the for-loop
                }
            }
            if (!playerDrafted) {
                System.out.println("No players were drafted in this iteration, stopping the draft to prevent infinite loop.");
                break; // Preventing infinite loop if no player is drafted
            }
            // Debug output to check the progress
            System.out.println("Remaining players: " + remainingPlayerNames.size());
        }
        System.out.println("Auto-draft complete.");
    }
    
    public boolean draftPlayerToTeam(String playerName, String teamName) {
        Team team = getTeamByName(teamName);
        Player player = availablePlayers.get(playerName);

        if (team != null && player != null) {
            team.addPlayer(player); // Make sure this method adds the player to the team's list
            availablePlayers.remove(playerName); // This should remove the player from the map
            return true;
        }
        return false;
    }



    
    public static int getNumberOfTeams() {
        return teams.size();
    }
    public void printTeamSizes() {
        for (Team team : teams) {
            System.out.println(team.getTeamName() + " has " + team.getPlayers().size() + " players.");
        }
    }

    // Load players into the availablePlayers map
    private void loadPlayers() {
        String csvFilePath = "C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/DataFiles/2022-2023 NBA Player Stats - Regular.csv"; 
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
    
    
    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    
    public boolean startSeason() {
        if (isSeasonReady()) {
            currentSeason = new Season(parentComponent, teams); // Use the stored parentComponent
            currentSeason.playAndDelayMatches();
            return true;
            
        }
        return false;
        
        
    }
    
    public Component getParentComponent() {
        return this.parentComponent;
    }
    
    public boolean isSeasonReady() {
        if (teams.isEmpty()) {
            return false; // No teams are registered
        }

        for (Team team : teams) { // Iterate through the List<Team> directly
            if (!isTeamReady(team)) {
                return false; // At least one team is not ready
            }
        }

        return true; // All teams are ready
    }
    
    public static Season getCurrentSeason() {
        return currentSeason;
    }
    
    public boolean isTeamReady(Team team) {
        if (team == null) {
            return false; // Return false if team is null
        }
        List<Player> players = team.getPlayers();
        return players != null && players.size() >= MIN_PLAYERS_PER_TEAM && players.size() <= MAX_PLAYERS_PER_TEAM;
    }
    
    public void startNewSeason() {
        if (!isSeasonReady()) {
            System.out.println("Season not ready. Auto-drafting players to teams.");
            autoDraftForTeams();
        }

        if (isSeasonReady()) {
            currentSeason = new Season(parentComponent, teams); // Ensure this is correctly initialized
            System.out.println("New season started.");
        } else {
            System.out.println("Failed to start the season. Teams are not ready.");
        }
    }
    


    // Additional methods can be added as needed...
}