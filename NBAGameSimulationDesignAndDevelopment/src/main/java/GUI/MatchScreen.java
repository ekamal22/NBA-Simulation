package main.java.GUI;

import main.java.Game.Match;
import main.java.Team.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MatchScreen extends JFrame {
    private JLabel lblTeam1;
    private JLabel lblTeam2;
    private JLabel lblScore1;
    private JLabel lblScore2;
    private JButton btnPlayMatch;
    private Match match;

    public MatchScreen(Match match) {
        this.match = match;
        
        initializeComponents();
        setTitle("Match Details");
        setSize(400, 200);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    
    private void initializeComponents() {
        // Setting GridBagLayout for the entire frame
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Common GridBagConstraints settings
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
                if (!match.isPlayed()) {
                    match.playMatch(); // Simulate the match
                    lblScore1.setText(" Score: " + match.getScoreTeam1() + " ");
                    lblScore2.setText(" Score: " + match.getScoreTeam2() + " ");
                    btnPlayMatch.setEnabled(false);
                }
            }
        });
        add(btnPlayMatch, gbc);

        // If the match is already played, disable the Play Match button
        if (match.isPlayed()) {
            btnPlayMatch.setEnabled(false);
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        // Sample teams for testing
        Team team1 = new Team("Celtics", "C:\\Users\\Effendi Jabid Kamal\\Documents\\GitHub\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\Pics\\Celtics logo.png");
        Team team2 = new Team("Lakers", "C:\\Users\\Effendi Jabid Kamal\\Documents\\GitHub\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\Pics\\los-angeles-lakers-logo.png");
        

        // Sample match for testing
        Match match = new Match(team1, team2);

        // Start the Match Screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MatchScreen(match);
            }
        });
    }
}
