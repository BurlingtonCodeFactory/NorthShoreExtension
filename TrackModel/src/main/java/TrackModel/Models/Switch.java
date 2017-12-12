package TrackModel.Models;

import java.util.List;
import java.util.Objects;

public class Switch extends Block {
    private boolean switchState;
    private int switchZero;
    private int switchOne;
    private int switchBase;

    public Switch(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, List<Integer> connectedBlocks, double elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit, int switchZero, int switchOne, int switchBase)
    {
        super(id, line, blockType, beacon, coefficientFriction, connectedBlocks, elevation, grade, isBidirectional, isUnderground, length, speedLimit);
        this.switchState = false;
        this.switchZero = switchZero;
        this.switchOne = switchOne;
        this.switchBase = switchBase;
    }

    public Switch(Block block, int switchZero, int switchOne, int switchBase) {
        super(block.getId(), block.getLine(), block.getBlockType(), block.getBeacon(), block.getCoefficientFriction(), block.getConnectedBlocks(), block.getElevation(), block.getGrade(), block.getIsBidirectional(), block.getIsUnderground(), block.getLength(), block.getSpeedLimit());
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

    public int getSwitchZero()
    {
        return switchZero;
    }

    public int getSwitchOne()
    {
        return switchOne;
    }

    public int getSwitchBase() {
        return switchBase;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setSwitchState(boolean newSwitchState)
    {
        this.switchState = newSwitchState;
        fireSwitchStateChangeEvent(this);
    }

    public void setSwitchStateManual(boolean newSwitchState)
    {
        this.switchState = newSwitchState;
        fireSwitchStateManualChangeEvent(this);
    }

    //</editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Switch aSwitch = (Switch) o;
        return switchState == aSwitch.switchState &&
                switchZero == aSwitch.switchZero &&
                switchOne == aSwitch.switchOne &&
                switchBase == aSwitch.switchBase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), switchState, switchZero, switchOne, switchBase);
    }
}
