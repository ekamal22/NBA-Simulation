package main.java.Player;

public class Center extends Player {

    // Constructor
    public Center(String name, double pts, double trb, double ast, double blk, double stl) {
        super(name, "C", pts, trb, ast, blk, stl);
        initializeWeights(); // Initialize specific weights for Center
    }

    @Override
    protected void initializeWeights() {
        // Setting weights specifically for a Center
        // These values are just examples and should be adjusted based on the specific requirements of your game
        this.weightPts = 0.20; // Points
        this.weightTrb = 0.30; // Total Rebounds
        this.weightAst = 0.15; // Assists
        this.weightBlk = 0.25; // Blocks
        this.weightStl = 0.10; // Steals
    }

    // Additional Center specific methods can be added here...
}
