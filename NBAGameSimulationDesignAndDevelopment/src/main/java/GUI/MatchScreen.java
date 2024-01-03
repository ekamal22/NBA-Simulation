package main.java.GUI;

import main.java.Game.Match;
import main.java.Game.Season;
import main.java.Team.Team;
import main.java.Team.TeamManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class MatchScreen extends JFrame {
    private static JLabel lblTeam1;
    private static JLabel lblTeam2;
    private static JLabel lblScore1;
    private static JLabel lblScore2;
    private JButton btnPlayMatch;
    private Match match;
    private List<Match> matches; // List of matches to be played
    private int currentMatchIndex = 0; // Index of the current match
    private JLabel lblTeam1Logo; // JLabel to display the logo of team 1
    private JLabel lblTeam2Logo;
    private JButton btnPauseResume;
    private boolean isPaused = false;
    private static JPanel standingsPanel; // Panel to display standings
    private static TeamManager teamManager; 
    private Season currentSeason;


    public MatchScreen(List<Match> matches, TeamManager teamManager) {
    	this.matches = matches;
        this.match = matches.get(currentMatchIndex); // Loading the first match
        this.teamManager = teamManager;
        this.currentSeason = TeamManager.getCurrentSeason();
        standingsPanel = new JPanel();
        standingsPanel.setLayout(new BoxLayout(standingsPanel, BoxLayout.Y_AXIS));
        initializeComponents();
        updateMatchDetails();
        setTitle("Match Details");
        setSize(400, 200);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        
        GridBagConstraints gbcPanel = new GridBagConstraints();
        gbcPanel.gridx = 2; // Adjusting these as per the layout needs
        gbcPanel.gridy = 2; 
        gbcPanel.fill = GridBagConstraints.BOTH;
        gbcPanel.weightx = 1.0;
        gbcPanel.weighty = 1.0;
        add(standingsPanel, gbcPanel); 

        updateStandings();
        
		
    }

    private void togglePauseResume() {// Logic to pause/resume the simulation
        isPaused = !isPaused;
        btnPauseResume.setText(isPaused ? "Resume" : "Pause");
        
        currentSeason.setPaused(isPaused);
    }
    
    private void initializeComponents() {
    	
    	
        
        // Setting GridBagLayout for the entire frame
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        lblTeam1Logo = new JLabel();
        lblTeam2Logo = new JLabel();

        lblTeam1Logo.setPreferredSize(new Dimension(80, 80)); // Setting the preferred size for the logo labels
        lblTeam2Logo.setPreferredSize(new Dimension(80, 80));
        
        btnPauseResume = new JButton("Pause");
        btnPauseResume.addActionListener(e -> togglePauseResume());
        add(btnPauseResume, gbc);

        // Start the season
        
        currentSeason.playAndDelayMatches();
     
        gbc.gridx = 2; // Column index for the logo
        gbc.gridy = 0; // Row index for team 1
        add(lblTeam1Logo, gbc);

        gbc.gridy = 1; // Row index for team 2
        add(lblTeam2Logo, gbc);
        
        
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Team 1 Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        lblTeam1 = new JLabel("Team 1: " + match.getTeam1().getTeamName());
        add(lblTeam1, gbc);

        // Team 1 Score Label
        gbc.gridx = 1;
        lblScore1 = new JLabel(" Score: " + (match.isPlayed() ? match.getScoreTeam1() : "N/A"));
        add(lblScore1, gbc);

        // Team 2 Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        lblTeam2 = new JLabel("Team 2: " + match.getTeam2().getTeamName());
        add(lblTeam2, gbc);

        // Team 2 Score Label
        gbc.gridx = 1;
        lblScore2 = new JLabel(" Score: " + (match.isPlayed() ? match.getScoreTeam2() : " N/A "));
        add(lblScore2, gbc);

        // Play Match Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Spanning across two columns
        btnPlayMatch = new JButton("Play Match");
        btnPlayMatch.addActionListener(new ActionListener() {
           
        	@Override
            public void actionPerformed(ActionEvent e) {
                //currentSeason.playAndDelayMatches(); // Starting the simulation here
        		currentSeason.startMatches();
                btnPlayMatch.setEnabled(false); 
            }
        });
        
        
        add(btnPlayMatch, gbc);
        btnPlayMatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playAndLoadNextMatch();
            }
        });

        // If the match is already played, gotta disable the Play Match button
        if (match.isPlayed()) {
            btnPlayMatch.setEnabled(false);
        }
        
        
    }
    
    private void playAndLoadNextMatch() {
        if (!match.isPlayed()) {
            match.playMatch(); // Simulating the match
            updateMatchDetails(); // Updating the GUI with new match details
            updateLogo(lblTeam1Logo, match.getTeam1().getTeamLogo());
            updateLogo(lblTeam2Logo, match.getTeam2().getTeamLogo());
            if (currentMatchIndex < matches.size() - 1) {
                // Preparing for the next match
                currentMatchIndex++;
                match = matches.get(currentMatchIndex);
                updateMatchDetails(); // Updating the GUI with new match details
                btnPlayMatch.setEnabled(true);
            } else {
                // Season is completed
                btnPlayMatch.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Season completed!");
            }
        }
        SwingUtilities.invokeLater(() -> {
            updateMatchDetails(); 
        });
    }


    
    
    private void updateMatchDetails() {
        // Loading the current match
        match = matches.get(currentMatchIndex);

        // Update Team 1 and Team 2 names
        lblTeam1.setText("Team 1: " + match.getTeam1().getTeamName() + "");
        lblTeam2.setText("Team 2: " + match.getTeam2().getTeamName()+ "");

        // Update Team 1 and Team 2 logos
        System.out.println("Updating Team 1 Logo: " + match.getTeam1().getTeamLogo());
        updateLogo(lblTeam1Logo, match.getTeam1().getTeamLogo());
        System.out.println("Updating Team 2 Logo: " + match.getTeam2().getTeamLogo());
        updateLogo(lblTeam2Logo, match.getTeam2().getTeamLogo());

        // Update scores
        lblScore1.setText(" Score: " + (match.isPlayed() ? match.getScoreTeam1() : "N/A"));
        lblScore2.setText(" Score: " + (match.isPlayed() ? match.getScoreTeam2() : "N/A"));
    }


    private void updateLogo(JLabel label, String logoPath) {
        SwingUtilities.invokeLater(() -> {
            try {
                File logoFile = new File(logoPath);
                if (logoFile.exists()) {
                    ImageIO.setUseCache(false); 
                    BufferedImage img = ImageIO.read(logoFile);
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                    label.setIcon(icon);
                    System.out.println("Logo updated for path: " + logoPath);
                } else {
                    System.out.println("Logo file not found at path: " + logoPath);
                    label.setIcon(null);
                    label.setText("Logo not found");
                }
            } catch (IOException e) {
                System.out.println("Exception when updating logo for path: " + logoPath);
                e.printStackTrace();
                label.setIcon(null);
                label.setText("Error loading logo");
            }
            label.revalidate();
            label.repaint();
            this.revalidate(); // Revalidate the entire JFrame
            this.repaint(); // Repaint the entire JFrame
        });
    }

    
    public static void updateMatchResults(Match match) {
        if (lblTeam1 != null) {
            lblTeam1.setText("Team 1: " + match.getTeam1().getTeamName());
        }
        if (lblScore1 != null) {
            lblScore1.setText("Score: " + match.getScoreTeam1());
        }
        if (lblTeam2 != null) {
            lblTeam2.setText("Team 2: " + match.getTeam2().getTeamName());
        }
        if (lblScore2 != null) {
            lblScore2.setText("Score: " + match.getScoreTeam2());
        }

        
        updateStandings();
    }
    
    

    private static void updateStandings() {
    	if (standingsPanel != null) {
            standingsPanel.removeAll();

            for (Team team : teamManager.getTeams()) {
                standingsPanel.add(new JLabel(team.getTeamName() + " - Wins: " + team.getWins() + ", Losses: " + team.getLosses()));
            }

            standingsPanel.revalidate();
            standingsPanel.repaint();
        } else {
            System.out.println("Attempted to update standings before standingsPanel was initialized");
        }
    }


    // Main method for testing
    public static void main(String[] args) {
        // Sample teams for testing
        Team team1 = new Team("Celtics", "/C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Celtics logo.png");
        Team team2 = new Team("Lakers", "/C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Los Angeles-Lakers.png");
        Team team3 = new Team("Atlanta Hawks", "/C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Atlanta Hawks.png");
        Team team4 = new Team("Brooklyn Nets", "/C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Brooklyn Nets.png");
       
        TeamManager teamManager = new TeamManager();
        teamManager.startNewSeason();
        
     // Ensure that currentSeason is properly initialized
        Season currentSeason = teamManager.getCurrentSeason();
        
        List<Match> matches = new ArrayList<>();
        matches.add(new Match(team1, team2));
        matches.add(new Match(team3, team4));

        // Start the Match Screen with a list of matches and the team manager
        SwingUtilities.invokeLater(new Runnable() {
        	
            @Override
            public void run() {
                new MatchScreen(matches,teamManager).setVisible(true);
            }
        });
        
    }
    
    
}
