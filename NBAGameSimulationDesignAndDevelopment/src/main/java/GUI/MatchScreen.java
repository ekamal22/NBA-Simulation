package main.java.GUI;

import javax.swing.*;

import main.java.Team.Team;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.MatchResult;

public class MatchScreen extends JPanel {
    private MainApplication mainApplication;
    private List<Team> teams;
    private List<MatchResult> matchResults;
    private int currentMatchIndex;
    private Timer timer;

    public MatchScreen(MainApplication mainApplication, List<Team> teams) {
        this.mainApplication = mainApplication;
        this.teams = teams;
        this.matchResults = new ArrayList<>();
        this.currentMatchIndex = 0;

        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JButton pauseButton = new JButton("Pause");
        JButton resumeButton = new JButton("Resume");

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pause the simulation
                timer.stop();
                // Replace the Pause button with Resume button
                remove(pauseButton);
                add(resumeButton, BorderLayout.SOUTH);
                revalidate();
                repaint();
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Resume the simulation
                timer.start();
                // Replace the Resume button with Pause button
                remove(resumeButton);
                add(pauseButton, BorderLayout.SOUTH);
                revalidate();
                repaint();
            }
        });

        add(pauseButton, BorderLayout.SOUTH); // Initially, show the Pause button

        // Create a panel for the playoff tree
        JPanel playoffPanel = createPlayoffPanel();
        add(playoffPanel, BorderLayout.CENTER);

        // Simulate the matchmaking process
        simulateMatchmaking();
    }

    private JPanel createPlayoffPanel() {
        JPanel playoffPanel = new JPanel(new GridLayout(3, 3));

        // Create buttons for each stage of the playoff tree
        for (int i = 0; i < 9; i++) {
            JButton matchButton = new JButton("Match " + (i + 1));
            playoffPanel.add(matchButton);

            // Add ActionListener to handle match button clicks
            matchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle match button click
                    // You can display match details or perform other actions
                    int matchNumber = i + 1;
                    if (matchNumber <= matchResults.size()) {
                        MatchResult result = matchResults.get(i);
                        displayMatchResult(result);
                    }
                }
            });
        }

        return playoffPanel;
    }

    // Simulate the matchmaking process
    private void simulateMatchmaking() {
        int numTeams = teams.size();
        int numMatches = 2 * numTeams;

        // Play M different games
        for (int i = 0; i < numMatches; i++) {
            // Randomly pick teams for the match
            Team teamA = teams.get(new Random().nextInt(numTeams));
            Team teamB = teams.get(new Random().nextInt(numTeams));

            // Simulate the game and determine the winner
            Team winner = simulateGame(teamA, teamB);

            // Update match results
            MatchResult matchResult = new MatchResult(teamA, teamB, winner);
            matchResults.add(matchResult);
        }

        // Sort teams based on the number of wins
        teams.sort((t1, t2) -> Integer.compare(t2.getWins(), t1.getWins()));

        // Set up a timer to run the playoff matches with a delay of 0.1 seconds
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMatchIndex < matchResults.size()) {
                    // Play playoff matches one by one
                    MatchResult result = matchResults.get(currentMatchIndex);
                    displayMatchResult(result);
                    currentMatchIndex++;
                } else {
                    // All matches are over, stop the timer
                    timer.stop();
                    JOptionPane.showMessageDialog(MatchScreen.this, "Tournament Over");
                }
            }
        });

        // Start the timer
        timer.start();
    }

    // Simulate a game between two teams and determine the winner
    private Team simulateGame(Team teamA, Team teamB) {
        // Determine the home team (randomly selected)
        Team homeTeam = (new Random().nextBoolean()) ? teamA : teamB;

        // Multiply home team's score by 1.05
        int homeTeamScore = (int) (homeTeam.calculateTotalScore() * 1.05);
        int awayTeamScore = teamA.calculateTotalScore() + teamB.calculateTotalScore();

        // Determine the winner
        return (homeTeamScore > awayTeamScore) ? homeTeam : (awayTeamScore > homeTeamScore) ? teamA : teamB;
    }

    // Display the result of a match
 // Display the result of a match
    private void displayMatchResult(int matchIndex) {
        if (matchIndex < matchResults.size()) {
            Team teamA = matchResults.get(matchIndex).getTeamA();
            Team teamB = matchResults.get(matchIndex).getTeamB();
            Team winner = matchResults.get(matchIndex).getWinner();

            // Update the UI to show match result
            // You can customize this based on your UI requirements
            JOptionPane.showMessageDialog(
                    MatchScreen.this,
                    "Match Result:\n" +
                            teamA.getName() + " vs " + teamB.getName() + "\n" +
                            "Winner: " + winner.getName(),
                    "Match Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    }

