package main.java.Player;

public class Player {
    private String name;
    private String position;
    private double pts; // Points
    private double trb; // Total Rebounds
    private double ast; // Assists
    private double blk; // Blocks
    private double stl; // Steals

    // Weights for each stat
    protected double weightPts;
    protected double weightTrb;
    protected double weightAst;
    protected double weightBlk;
    protected double weightStl;

    // Constructor
    public Player(String name, String position, double pts, double trb, double ast, double blk, double stl) {
        this.name = name;
        this.position = position;
        this.pts = pts;
        this.trb = trb;
        this.ast = ast;
        this.blk = blk;
        this.stl = stl;

        // Initialize weights based on position or some other logic
        initializeWeights();
    }

    protected void initializeWeights() {
        // Example initialization, adjust based on your game logic
        switch (position) {
            case "PG": // Point Guard
                weightPts = 0.30;
                weightTrb = 0.10;
                weightAst = 0.40;
                weightBlk = 0.10;
                weightStl = 0.10;
                break;
            // Add cases for other positions
            default:
                // Default weights (if needed)
                weightPts = 0.20;
                weightTrb = 0.20;
                weightAst = 0.20;
                weightBlk = 0.20;
                weightStl = 0.20;
                break;
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public double getPts() {
        return pts;
    }

    public double getTrb() {
        return trb;
    }

    public double getAst() {
        return ast;
    }

    public double getBlk() {
        return blk;
    }

    public double getStl() {
        return stl;
    }

    // Method to calculate the player's score
    public int calculateScore() {
        double score = pts * weightPts + trb * weightTrb + ast * weightAst + blk * weightBlk + stl * weightStl;
        return (int) Math.round(score);
    }

    // ToString method for debugging
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", pts=" + pts +
                ", trb=" + trb +
                ", ast=" + ast +
                ", blk=" + blk +
                ", stl=" + stl +
                '}';
    }

    // Additional methods can be added as needed...
}
