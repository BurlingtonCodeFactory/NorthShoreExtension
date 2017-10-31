package TrackModel;

public class Switch extends Block {
    private boolean switchState;
    private Block switchZero;
    private Block switchOne;

    public Switch(int number, LineType lineType, BlockType infra, int size, double speedLimit, Block previousBlock, Block switchZero, Block switchOne, boolean underground)
    {
        super(number, lineType, infra, size, speedLimit, previousBlock, switchZero, underground);
        this.switchState = false;
        this.switchZero = switchZero;
        this.switchOne = switchOne;
    }

    //<editor-fold desc="Getters">

    public boolean getSwitchState()
    {
        return switchState;
    }

    public Block getSwitchZero()
    {
        return switchZero;
    }

    public Block getSwitchOne()
    {
        return switchOne;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setSwitchState(boolean newSwitchState)
    {
        this.switchState = newSwitchState;
        this.setNextBlock(newSwitchState ? switchOne : switchZero);
    }

    //</editor-fold>
}
