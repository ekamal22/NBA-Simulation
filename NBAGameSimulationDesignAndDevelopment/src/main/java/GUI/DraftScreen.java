package main.java.GUI;

import main.java.Player.Player;
import main.java.Team.Team;
import main.java.Team.TeamManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class DraftScreen extends JFrame {
    private JComboBox<String> playerComboBox;
    private JButton btnDraftPlayer;
    private JButton btnCancel;
    private TeamManager teamManager;
    private Team userTeam;

    public DraftScreen(TeamManager teamManager, Team userTeam) {
        this.teamManager = teamManager;
        this.userTeam = userTeam;

        setTitle("Player Draft");
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Player selection combo box
        add(new JLabel("Select Player:"), gbc);
        gbc.gridx++;
        playerComboBox = new JComboBox<>();
        populatePlayerComboBox();
        add(playerComboBox, gbc);

        // Draft Player Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        btnDraftPlayer = new JButton("Draft Player");
        btnDraftPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draftPlayer();
            }
        });
        add(btnDraftPlayer, gbc);

        // Cancel Button
        gbc.gridy++;
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DraftScreen.this.dispose(); // Close the draft screen
            }
        });
        add(btnCancel, gbc);
    }

    private void populatePlayerComboBox() {
        Map<String, Player> availablePlayers = teamManager.getAvailablePlayers();
        for (String playerName : availablePlayers.keySet()) {
            playerComboBox.addItem(playerName);
        }
    }

    private void draftPlayer() {
        String selectedPlayerName = (String) playerComboBox.getSelectedItem();
        if (selectedPlayerName != null) {
            Player draftedPlayer = teamManager.getAvailablePlayers().get(selectedPlayerName);
            if (draftedPlayer != null) {
                // Add the player to the user's team and remove from available players
                userTeam.addPlayer(draftedPlayer);
                teamManager.draftPlayerToTeam(selectedPlayerName, userTeam.getTeamName());
                JOptionPane.showMessageDialog(this, "Player drafted successfully!");

                // Maybe, refresh the combo box or close the screen
                playerComboBox.removeItem(selectedPlayerName);
            } else {
                JOptionPane.showMessageDialog(this, "Player is not available.");
            }
        }
    }

    public static void main(String[] args) {
        
        TeamManager teamManager = new TeamManager(); 
        Team userTeam = new Team("User Team", "/NBAGameSimulationDesignAndDevelopment/src/main/resources/Pics/Default pfp.png"); 

        // Launch the draft screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DraftScreen(teamManager, userTeam).setVisible(true);
            }
        });
    }
}
