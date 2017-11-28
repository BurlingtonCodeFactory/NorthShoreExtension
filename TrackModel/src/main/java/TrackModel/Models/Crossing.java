package TrackModel.Models;

public class Crossing extends Block{
    private boolean isCrossingOn;

    public Crossing(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, Iterable<Integer> connectedBlocks, int elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit)
    {
        super(id, line, blockType, beacon, coefficientFriction, connectedBlocks, elevation, grade, isBidirectional, isUnderground, length, speedLimit);
        this.isCrossingOn = false;
    }

    //<editor-fold desc="Getters">

    public boolean isCrossingOn()
    {
        return isCrossingOn;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setCrossingOn(boolean isCrossingOn)
    {
        this.isCrossingOn = isCrossingOn;
    }

    //</editor-fold>
}
