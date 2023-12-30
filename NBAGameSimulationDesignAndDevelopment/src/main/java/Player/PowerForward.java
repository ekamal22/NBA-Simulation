package main.java.Player;

public class PowerForward extends Player {

    // Constructor
    public PowerForward(String name, double pts, double trb, double ast, double blk, double stl) {
        super(name, "PF", pts, trb, ast, blk, stl);
        initializeWeights(); // Initialize specific weights for Power Forward
    }

    @Override
    protected void initializeWeights() {
        // Setting weights specifically for a Power Forward
        // These values are just examples and should be adjusted based on the specific requirements of your game
        this.weightPts = 0.25; // Points
        this.weightTrb = 0.25; // Total Rebounds
        this.weightAst = 0.15; // Assists
        this.weightBlk = 0.20; // Blocks
        this.weightStl = 0.15; // Steals
    }

    // Additional Power Forward specific methods can be added here...
}
