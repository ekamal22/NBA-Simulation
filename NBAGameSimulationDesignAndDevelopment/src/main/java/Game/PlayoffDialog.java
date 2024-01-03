package main.java.Game;

import javax.swing.*;

import main.java.Game.GameUtilities;
import main.java.Game.Match;
import main.java.Game.Season;
import main.java.Team.Team;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlayoffDialog extends JDialog {
    private JButton continueButton;
    private Season currentSeason;
    public PlayoffDialog(JFrame parent, String playoffTree, Season season) {
        super(parent, true);
        this.currentSeason = season;
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent); // Use parent instead of parentFrame

        // TextArea to display playoff tree
        JTextArea playoffTreeTextArea = new JTextArea(playoffTree); // Use playoffTree instead of playoffTreeText
        playoffTreeTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(playoffTreeTextArea);
        add(scrollPane);

        // Continue Button
        continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onContinue();
            }
        });

        // Panel for button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(continueButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void onContinue() {
        // Logic to simulate the playoffs goes here
        simulatePlayoffs();

        // Dispose the dialog after handling continue
        dispose();
    }

    private void simulatePlayoffs() {
        List<Team> playoffTeams = this.currentSeason.determinePlayoffTeams();

        while (playoffTeams.size() > 1) {
            List<Team> winners = new ArrayList<>();

            for (int i = 0; i < playoffTeams.size(); i += 2) {
                Team team1 = playoffTeams.get(i);
                Team team2 = playoffTeams.get(i + 1);
                Match match = new Match(team1, team2);

                // Ensure the match is played and results are set here
                match.playMatch(); // Assuming playMatch() method simulates the match and sets the scores

                winners.add(match.getWinner());
                updateMatchResults(match); // Now the match has been played and scores are set
            }

            playoffTeams = winners;
            updateGUIForNewRound(playoffTeams);
        }

        Team champion = playoffTeams.get(0);
        announceChampion(champion);
    }

    
 // This method updates the match results in the GUI.
    private void updateMatchResults(Match match) {
        // Assuming you have a JLabel for each team's score
        JLabel labelTeam1Score = new JLabel("Score: " + match.getScoreTeam1());
        JLabel labelTeam2Score = new JLabel("Score: " + match.getScoreTeam2());

        // Update the labels with the scores from the match
        labelTeam1Score.setText(match.getTeam1().getTeamName() + " Score: " + match.getScoreTeam1());
        labelTeam2Score.setText(match.getTeam2().getTeamName() + " Score: " + match.getScoreTeam2());

        // Assuming you have a panel to display match results
        JPanel matchResultsPanel = new JPanel();
        matchResultsPanel.add(new JLabel(match.getTeam1().getTeamName() + " VS " + match.getTeam2().getTeamName()));
        matchResultsPanel.add(labelTeam1Score);
        matchResultsPanel.add(labelTeam2Score);

        // Add the results panel to your main GUI frame or dialog
        this.add(matchResultsPanel);

        // Refresh the GUI
        this.validate();
        this.repaint();
    }

    // This method updates the GUI to reflect the new round of playoffs.
    private void updateGUIForNewRound(List<Team> teamsInNewRound) {
        // Assuming you have a JList or some component to display the teams
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Team team : teamsInNewRound) {
            listModel.addElement(team.getTeamName());
        }
        
        JList<String> teamsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(teamsList);

        // Replace the old list in your GUI with the new one
        JPanel roundPanel = new JPanel();
        roundPanel.setBorder(BorderFactory.createTitledBorder("Next Round Teams"));
        roundPanel.add(scrollPane);

        // Assuming you have a main panel or frame to which you add the round panel
        this.add(roundPanel);

        // Refresh the GUI
        this.validate();
        this.repaint();
    }

    // This method announces the champion at the end of the playoffs.
    private void announceChampion(Team champion) {
        // Assuming you want to show a dialog with the champion
        JOptionPane.showMessageDialog(this,
            "Congratulations, " + champion.getTeamName() + "!\nYou are the champions!",
            "Champions",
            JOptionPane.INFORMATION_MESSAGE);

        // Optionally, update a label in the GUI to reflect the champion
        JLabel labelChampion = new JLabel("Champion: " + champion.getTeamName());
        this.add(labelChampion);

        // Refresh the GUI
        this.validate();
        this.repaint();
    }

}
