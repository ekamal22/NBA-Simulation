package main.java.Player;

public class PointGuard extends Player {

    // Constructor
    public PointGuard(String name, double pts, double trb, double ast, double blk, double stl) {
        super(name, "PG", pts, trb, ast, blk, stl);
    }

    @Override
    protected void initializeWeights() {
        // Setting weights specifically for a Point Guard
        this.weightPts = 0.25;
        this.weightTrb = 0.15;
        this.weightAst = 0.35;
        this.weightBlk = 0.10;
        this.weightStl = 0.15;
    }

   
}
