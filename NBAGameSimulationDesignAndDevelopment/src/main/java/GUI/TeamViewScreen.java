package main.java.GUI;

import main.java.Player.Player;
import main.java.Team.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class TeamViewScreen extends JFrame {
    private JTable tablePlayers;
    private DefaultTableModel tableModel;
    private Team team;

    public TeamViewScreen(Team team) {
        this.team = team;

        setTitle(team.getTeamName() + " - Team Roster");
        setSize(500, 300);
        setLocationRelativeTo(null); // Center the window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // Set up the columns for the table model
        String[] columns = new String[]{"Name", "Position", "Points", "Rebounds", "Assists", "Blocks", "Steals"};

        // Create a table model with the data
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // all cells false (non-editable table)
                return false;
            }
        };

        // Fill the table model
        for (Player player : team.getPlayers()) {
            Vector<String> row = new Vector<>();
            row.add(player.getName());
            row.add(player.getPosition());
            row.add(String.format("%.1f", player.getPts()));
            row.add(String.format("%.1f", player.getTrb()));
            row.add(String.format("%.1f", player.getAst()));
            row.add(String.format("%.1f", player.getBlk()));
            row.add(String.format("%.1f", player.getStl()));
            tableModel.addRow(row);
        }

        // Set up the table with the data model
        tablePlayers = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePlayers);
        add(scrollPane, BorderLayout.CENTER);

        // Add a close button at the bottom
        JPanel panelButtons = new JPanel();
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        panelButtons.add(btnClose);
        add(panelButtons, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Assume there's a team instance
        Team team = new Team("Lakers", "C:\\Users\\Effendi Jabid Kamal\\Documents\\GitHub\\NBAGameSimulationDesignAndDevelopment\\src\\main\\resources\\Pics\\los-angeles-lakers-logo.png"); // Replace with actual team instance

        // Add 12 dummy players for the demonstration
        team.addPlayer(new Player("LeBron James", "SF", 25, 7, 7, 0.5, 1));
        team.addPlayer(new Player("Anthony Davis", "PF", 22, 9, 3, 2, 1.2));
        team.addPlayer(new Player("Russell Westbrook", "PG", 20, 5, 8, 0.4, 1.5));
        team.addPlayer(new Player("Carmelo Anthony", "PF", 13, 4, 2, 0.5, 0.8));
        team.addPlayer(new Player("Dwight Howard", "C", 7, 7, 1, 1.5, 0.4));
        team.addPlayer(new Player("Rajon Rondo", "PG", 5, 2, 5, 0.1, 0.6));
        team.addPlayer(new Player("Talen Horton-Tucker", "SG", 9, 3, 3, 0.2, 0.9));
        team.addPlayer(new Player("Avery Bradley", "SG", 6, 2, 1, 0.3, 0.7));
        team.addPlayer(new Player("Malik Monk", "SG", 12, 2, 2, 0.1, 0.8));
        team.addPlayer(new Player("Austin Reaves", "SG", 5, 1, 2, 0.1, 0.5));
        team.addPlayer(new Player("Stanley Johnson", "SF", 4, 3, 1, 0.4, 0.2));
        team.addPlayer(new Player("Kendrick Nunn", "PG", 10, 2, 3, 0.2, 0.9));

        // Launch the team view screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TeamViewScreen(team).setVisible(true);
            }
        });
    }
}
