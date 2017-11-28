package TrackModel.Models;

public class Switch extends Block {
    private boolean switchState;
    private Block switchZero;
    private Block switchOne;
    private Block switchBase;

    public Switch(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, Iterable<Integer> connectedBlocks, int elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit, boolean switchState, Block switchZero, Block switchOne, Block switchBase)
    {
        super(id, line, blockType, beacon, coefficientFriction, connectedBlocks, elevation, grade, isBidirectional, isUnderground, length, speedLimit);
        this.switchState = false;
        this.switchZero = switchZero;
        this.switchOne = switchOne;
        this.switchBase = switchBase;
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

    public Block getSwitchBase() {
        return switchBase;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setSwitchState(boolean newSwitchState)
    {
        this.switchState = newSwitchState;
    }

    //</editor-fold>
}
