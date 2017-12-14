package TrackModel.Models;

import java.util.List;
import java.util.Objects;

public class Crossing extends Block
{
    private boolean isCrossingOn;

    public Crossing(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, List<Integer> connectedBlocks, double elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit)
    {
        super(id, line, blockType, beacon, coefficientFriction, connectedBlocks, elevation, grade, isBidirectional, isUnderground, length, speedLimit);
        this.isCrossingOn = false;
    }

    public Crossing(Block block)
    {
        super(block.getId(), block.getLine(), block.getBlockType(), block.getBeacon(), block.getCoefficientFriction(), block.getConnectedBlocks(), block.getElevation(), block.getGrade(), block.getIsBidirectional(), block.getIsUnderground(), block.getLength(), block.getSpeedLimit());
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        Crossing crossing = (Crossing) o;
        return isCrossingOn == crossing.isCrossingOn;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isCrossingOn);
    }
}
