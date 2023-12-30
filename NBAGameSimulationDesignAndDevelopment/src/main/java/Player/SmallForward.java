package main.java.Player;

public class SmallForward extends Player {

    // Constructor
    public SmallForward(String name, double pts, double trb, double ast, double blk, double stl) {
        super(name, "SF", pts, trb, ast, blk, stl);
        initializeWeights(); // Initialize specific weights for Small Forward
    }

    @Override
    protected void initializeWeights() {
        // Setting weights specifically for a Small Forward
        // These values are examples and should be adjusted based on your game's requirements
        this.weightPts = 0.25; // Points
        this.weightTrb = 0.20; // Total Rebounds
        this.weightAst = 0.20; // Assists
        this.weightBlk = 0.15; // Blocks
        this.weightStl = 0.20; // Steals
    }

    // Additional Small Forward specific methods can be added here...
}
