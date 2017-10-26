package trackmodel;

public class Crossing extends Block{
    private boolean crossingOn;

    public Crossing(int number, LineType lineType, BlockType infra, int size, Block previousBlock, Block nextBlock)
    {
        super(number, lineType, infra, size, previousBlock, nextBlock);
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
