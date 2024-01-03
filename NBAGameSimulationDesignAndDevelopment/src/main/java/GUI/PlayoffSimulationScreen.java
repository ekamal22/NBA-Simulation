package main.java.GUI;

import main.java.Game.Match;
import main.java.Team.Team;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayoffSimulationScreen extends JDialog {
    private List<Match> playoffMatches;
    private List<Team> currentRoundTeams;
    private int currentMatchIndex;
    private JButton startButton, nextMatchButton, pauseButton;
    private JLabel matchLabel, team1Label, team2Label, scoreLabel;
    private JTextArea playoffTreeTextArea;

    public PlayoffSimulationScreen(JFrame parent, List<Match> playoffMatches, String playoffTree) {
        super(parent, "Playoff Simulation", true);
        this.playoffMatches = playoffMatches;
        this.currentRoundTeams = new ArrayList<>();
        this.currentMatchIndex = 0;

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
