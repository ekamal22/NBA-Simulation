package main.java.GUI;

import main.java.Game.Match;
import main.java.Team.Team;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.java.Utils.Logging;

public class PlayoffSimulationScreen extends JDialog {
	
    private List<Match> playoffMatches;
    private List<Team> currentRoundTeams;
    private int currentMatchIndex;
    private JButton startButton, nextMatchButton, pauseButton;
    private JLabel matchLabel, team1Label, team2Label, scoreLabel;
    private JTextArea playoffTreeTextArea;
    private JLabel lblTeam1;
    private JLabel lblScore1;
    private JLabel lblTeam2;
    private JLabel lblScore2;
    private JLabel lblTeam1Logo;
    private JLabel lblTeam2Logo;
    private JLabel lblSeriesStatus;
    private JLabel lblRound;
    private Match match;
    
    private Logging playoffLogger = new Logging("C:/Users/Effendi Jabid Kamal/Documents/GitHub/NBAGameSimulationDesignAndDevelopment/src/main/resources/DataFiles/playoffMatches.log");
    private Timer matchTimer;

    public PlayoffSimulationScreen(JFrame parent, List<Match> playoffMatches, String playoffTree) {
        super(parent, "Playoff Simulation", true);
        if (playoffMatches.isEmpty()) {
            System.out.println("No playoff matches provided");
        } else {
            System.out.println("Received " + playoffMatches.size() + " playoff matches");
        }
        this.playoffMatches = playoffMatches;
        this.currentRoundTeams = new ArrayList<>();
        this.currentMatchIndex = 0;
        lblTeam1 = new JLabel();
        lblScore1 = new JLabel();
        lblTeam2 = new JLabel();
        lblScore2 = new JLabel();
        lblTeam1Logo = new JLabel();
        lblTeam2Logo = new JLabel();
        lblSeriesStatus = new JLabel();
        lblRound = new JLabel();
        match = null;

        // Initialize UI Components with playoff tree
        initializeComponents(playoffTree);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
    }


    private void initializeComponents(String playoffTree) {
        System.out.println("Initializing components with playoff tree: " + playoffTree);
        
        // Create components
        playoffTreeTextArea = new JTextArea(playoffTree);
        playoffTreeTextArea.setEditable(false);
        JScrollPane treeScrollPane = new JScrollPane(playoffTreeTextArea);
        
        startButton = new JButton("Start Playoffs");
        nextMatchButton = new JButton("Next Match");
        pauseButton = new JButton("Pause");
        
        // Instantiate labels
        matchLabel = new JLabel("Match: ");
        team1Label = new JLabel("Team 1: ");
        team2Label = new JLabel("Team 2: ");
        scoreLabel = new JLabel("Score: ");
        
        // Initialize additional labels
        lblTeam1 = new JLabel();
        lblScore1 = new JLabel();
        lblTeam2 = new JLabel();
        lblScore2 = new JLabel();
        lblTeam1Logo = new JLabel();
        lblTeam2Logo = new JLabel();
        lblSeriesStatus = new JLabel();
        lblRound = new JLabel();

        // Set action listeners for buttons
        startButton.addActionListener(this::startPlayoffs);
        nextMatchButton.addActionListener(evt -> playNextMatch());
        nextMatchButton.setEnabled(false); // Initially disabled
        pauseButton.addActionListener(this::pauseMatch);
        pauseButton.setEnabled(false); // Initially disabled

        // Panel for match information
        JPanel matchInfoPanel = new JPanel(new GridLayout(4, 1));
        matchInfoPanel.add(matchLabel);
        matchInfoPanel.add(team1Label);
        matchInfoPanel.add(team2Label);
        matchInfoPanel.add(scoreLabel);

        // Panel for team1 and team2 details
        JPanel team1Panel = new JPanel(new BorderLayout());
        team1Panel.add(lblTeam1, BorderLayout.NORTH);
        team1Panel.add(lblTeam1Logo, BorderLayout.CENTER);
        team1Panel.add(lblScore1, BorderLayout.SOUTH);

        JPanel team2Panel = new JPanel(new BorderLayout());
        team2Panel.add(lblTeam2, BorderLayout.NORTH);
        team2Panel.add(lblTeam2Logo, BorderLayout.CENTER);
        team2Panel.add(lblScore2, BorderLayout.SOUTH);

        // Series status and round panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(lblSeriesStatus, BorderLayout.NORTH);
        statusPanel.add(lblRound, BorderLayout.SOUTH);

        // Combine team panels and status into a single details panel
        JPanel matchDetailsPanel = new JPanel();
        matchDetailsPanel.setLayout(new BoxLayout(matchDetailsPanel, BoxLayout.Y_AXIS));
        matchDetailsPanel.add(matchInfoPanel);
        matchDetailsPanel.add(team1Panel);
        matchDetailsPanel.add(team2Panel);
        matchDetailsPanel.add(statusPanel);

        // Control panel with buttons
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(startButton);
        controlPanel.add(nextMatchButton);
        controlPanel.add(pauseButton);

        // Add components to the dialog
        setLayout(new BorderLayout());
        add(treeScrollPane, BorderLayout.WEST);
        add(controlPanel, BorderLayout.SOUTH);
        add(matchDetailsPanel, BorderLayout.CENTER);

        // Set default values or load the first match if available
        if (!playoffMatches.isEmpty()) {
            updatePlayoffDetails(playoffMatches.get(0)); // Update the UI with the first match details
        }

        // Debugging output to verify component sizes
        System.out.println("Size of treeScrollPane: " + treeScrollPane.getPreferredSize());
        System.out.println("Size of matchDetailsPanel: " + matchDetailsPanel.getPreferredSize());
        System.out.println("Size of controlPanel: " + controlPanel.getPreferredSize());

        // Print out a message to indicate successful initialization
        System.out.println("Components initialized and added to layout.");

        // Force the window to layout its subcomponents
        pack();
    }


    
    private void updatePlayoffDetails(Match playoffMatch) {
        // Assuming you have class variables like lblSeriesStatus, lblRound, etc. that are similar to lblTeam1, lblScore1, etc.
    	System.out.println("Updating playoff details for match: " + playoffMatch.getTeam1().getTeamName() + " vs " + playoffMatch.getTeam2().getTeamName());
        // Load the current playoff match details
        this.match = playoffMatch; // Make sure this match is the current playoff match

        // Update team names and scores for the playoff match
        lblTeam1.setText("Team 1: " + match.getTeam1().getTeamName());
        lblScore1.setText("Score: " + (match.isPlayed() ? match.getScoreTeam1() : "N/A"));
        lblTeam2.setText("Team 2: " + match.getTeam2().getTeamName());
        lblScore2.setText("Score: " + (match.isPlayed() ? match.getScoreTeam2() : "N/A"));

        // Update team logos for the playoff match
        refreshTeamLogoDisplay(lblTeam1Logo, match.getTeam1().getTeamLogo());
        refreshTeamLogoDisplay(lblTeam2Logo, match.getTeam2().getTeamLogo());

        // Update series status and round information
        lblSeriesStatus.setText("Series Status: " + getSeriesStatus(playoffMatch));
        lblRound.setText("Round: " + getPlayoffRound(playoffMatch));

        // Revalidate and repaint components to reflect changes
        revalidateAndRepaintComponents();
    }
    
    private void refreshTeamLogoDisplay(JLabel label, String logoPath) {
        SwingUtilities.invokeLater(() -> {
            try {
            	 System.out.println("Loading logo from: " + logoPath);
                BufferedImage img = ImageIO.read(new File(logoPath));
                ImageIcon icon = new ImageIcon(img);
                if (label.getWidth() <= 0 || label.getHeight() <= 0) {
                    // Default size if the component has no size yet
                    icon = new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                } else {
                    // Scale the icon to the label's size
                	
                    icon = new ImageIcon(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH));
                }
                label.setIcon(icon);
            } catch (IOException e) {
            	System.out.println("Failed to load logo from: " + logoPath);
                e.printStackTrace();
                label.setIcon(null);
                label.setText("Error loading logo");
            }
        });
    }

    
    private String getSeriesStatus(Match playoffMatch) {
    	if (!playoffMatch.isPlayed()) {
            return "Upcoming";
        } else if (playoffMatch.getWinner().equals(playoffMatch.getTeam1())) {
            return playoffMatch.getTeam1().getTeamName() + " won";
        } else {
            return playoffMatch.getTeam2().getTeamName() + " won";
        }
    }
    

    private String getPlayoffRound(Match playoffMatch) {
    	int round = getRoundForMatch(playoffMatch); 
        switch (round) {
            case 1:
                return "First Round";
            case 2:
                return "Semi-Finals";
            case 3:
                return "Finals";
            default:
                return "Unknown Round";
        }
    }
    
    public int getRoundForMatch(Match match) {
        // Find the index of the match in the playoffMatches list
        int index = playoffMatches.indexOf(match);

        if (index < 0) {
            throw new IllegalArgumentException("Match not found in playoff matches.");
        }

        // Determine the round based on the index
        if (index < 4) { // First 4 matches are quarterfinals
            return 1;
        } else if (index < 6) { // Next 2 matches are semifinals
            return 2;
        } else { // Final match is the championship
            return 3;
        }
    }

    

    private void revalidateAndRepaintComponents() {
        SwingUtilities.invokeLater(() -> {
            // Update any other components that need to be refreshed
            // This ensures all updates occur on the Event Dispatch Thread
            this.revalidate();
            this.repaint();
        });
    }


    /*private void startPlayoffs(ActionEvent e) {
        // Disable the start button and enable the next match button
        startButton.setEnabled(false);
        nextMatchButton.setEnabled(true);
        playNextMatch(e);
        matchTimer = new Timer(1000, evt -> playNextMatch());
        matchTimer.start();
    }*/
    
    private void startPlayoffs(ActionEvent e) {
        startButton.setEnabled(false);
        nextMatchButton.setEnabled(false); // Disable if automatic
        pauseButton.setEnabled(true); // Enable if you have pause functionality

        // Timer setup - assuming you want a delay of 1 second (1000 ms) between matches
        matchTimer = new Timer(1000, evt -> playNextMatch());
        matchTimer.start();
    }


    private void playNextMatch() {
        if (currentMatchIndex < playoffMatches.size()) {
            Match currentMatch = playoffMatches.get(currentMatchIndex);
            currentMatch.playMatch(); // Simulate the match

            // Update labels with match info
            Team team1 = currentMatch.getTeam1();
            Team team2 = currentMatch.getTeam2();
            matchLabel.setText("Match: " + (currentMatchIndex + 1));
            team1Label.setText("Team 1: " + team1.getTeamName());
            team2Label.setText("Team 2: " + team2.getTeamName());
            scoreLabel.setText("Score: " + currentMatch.getScoreTeam1() + " - " + currentMatch.getScoreTeam2());

            currentMatchIndex++;
            String playoffMatchResult = String.format("Playoff Match - Team %s vs Team %s - Winner: Team %s - Score: %d-%d",
                    currentMatch.getTeam1().getTeamName(),
                    currentMatch.getTeam2().getTeamName(),
                    currentMatch.getWinner().getTeamName(),
                    currentMatch.getScoreTeam1(),
                    currentMatch.getScoreTeam2());
playoffLogger.log(playoffMatchResult);
            updateRoundTeams(currentMatch.getWinner());
        } else {
            announceChampion();
            matchTimer.stop();
        }
    }

    private void pauseMatch(ActionEvent e) {
        // Logic to pause the match simulation
    }

    private void updateRoundTeams(Team winner) {
        currentRoundTeams.add(winner);
        if (currentRoundTeams.size() == playoffMatches.size() / 2) {
            // Prepare for the next round
            prepareNextRound();
        }
    }

    private void prepareNextRound() {
        // Logic to prepare the next round of matches based on winners
        // Reset currentMatchIndex and currentRoundTeams for the next round
    }

    private void announceChampion() {
        if (!currentRoundTeams.isEmpty()) {
            Team champion = currentRoundTeams.get(0);
            JOptionPane.showMessageDialog(this, champion.getTeamName() + " is the champion!", "Champion", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose(); // Close the simulation screen
    }
}
