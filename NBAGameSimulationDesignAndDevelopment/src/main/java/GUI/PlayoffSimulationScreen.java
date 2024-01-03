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

    public PlayoffSimulationScreen(JFrame parent, List<Match> playoffMatches, String playoffTree) {
        super(parent, "Playoff Simulation", true);
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
        // Playoff Tree TextArea
        playoffTreeTextArea = new JTextArea(playoffTree);
        playoffTreeTextArea.setEditable(false);
        JScrollPane treeScrollPane = new JScrollPane(playoffTreeTextArea);

        // Start Button
        startButton = new JButton("Start Playoffs");
        startButton.addActionListener(this::startPlayoffs);

        // Next Match Button
        nextMatchButton = new JButton("Next Match");
        nextMatchButton.addActionListener(this::playNextMatch);
        nextMatchButton.setEnabled(false);

        // Pause Button
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this::pauseMatch);
        pauseButton.setEnabled(false);

        // Labels
        matchLabel = new JLabel("Match: ");
        team1Label = new JLabel("Team 1: ");
        team2Label = new JLabel("Team 2: ");
        scoreLabel = new JLabel("Score: ");

        // Layout
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(startButton);
        controlPanel.add(nextMatchButton);
        controlPanel.add(pauseButton);

        JPanel matchPanel = new JPanel(new GridLayout(4, 1));
        matchPanel.add(matchLabel);
        matchPanel.add(team1Label);
        matchPanel.add(team2Label);
        matchPanel.add(scoreLabel);

        // Adding components to the dialog
        add(treeScrollPane, BorderLayout.WEST);
        add(controlPanel, BorderLayout.NORTH);
        add(matchPanel, BorderLayout.CENTER);
        if (!playoffMatches.isEmpty()) {
            updatePlayoffDetails(playoffMatches.get(0)); // Update the UI with the first match details
        }
    }
    
    private void updatePlayoffDetails(Match playoffMatch) {
        // Assuming you have class variables like lblSeriesStatus, lblRound, etc. that are similar to lblTeam1, lblScore1, etc.
        
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


    private void startPlayoffs(ActionEvent e) {
        // Disable the start button and enable the next match button
        startButton.setEnabled(false);
        nextMatchButton.setEnabled(true);
        playNextMatch(e);
    }

    private void playNextMatch(ActionEvent e) {
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
            updateRoundTeams(currentMatch.getWinner());
        } else {
            announceChampion();
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
