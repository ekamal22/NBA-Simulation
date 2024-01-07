package main.java.Player;

public class ShootingGuard extends Player {

    // Constructor
    public ShootingGuard(String name, double pts, double trb, double ast, double blk, double stl) {
        super(name, "SG", pts, trb, ast, blk, stl);
        initializeWeights(); // Initializes specific weights for Shooting Guard
    }

    @Override
    protected void initializeWeights() {
        // Setting weights specifically for a Shooting Guard
        
        this.weightPts = 0.30; // Emphasis on points
        this.weightTrb = 0.15; // Total Rebounds
        this.weightAst = 0.20; // Assists
        this.weightBlk = 0.10; // Blocks
        this.weightStl = 0.25; // Steals
    }


}
