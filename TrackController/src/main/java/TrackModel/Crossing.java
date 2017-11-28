package TrackModel;

public class Crossing extends Block{
    private boolean crossingOn;

    public Crossing(int number, LineType lineType, BlockType infra, int size, double speedLimit, Block previousBlock, Block nextBlock, boolean underground)
    {
        super(number, lineType, infra, size, speedLimit, previousBlock, nextBlock, underground);
        this.crossingOn = false;
    }

    //<editor-fold desc="Getters">

    public boolean isCrossingOn()
    {
        return crossingOn;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setCrossingOn(boolean newCrossingOn)
    {
        this.crossingOn = newCrossingOn;
    }

    //</editor-fold>
}
