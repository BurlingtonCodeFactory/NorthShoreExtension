//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackModel.Models;

import java.util.List;
import java.util.Objects;

public class Station extends Block
{
    private String stationName;

    public Station(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, List<Integer> connectedBlocks, double elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit, String stationName)
    {
        super(id, line, blockType, beacon, coefficientFriction, connectedBlocks, elevation, grade, isBidirectional, isUnderground, length, speedLimit);
        this.stationName = stationName;
    }

    public Station(Block block, String stationName)
    {
        super(block.getId(), block.getLine(), block.getBlockType(), block.getBeacon(), block.getCoefficientFriction(), block.getConnectedBlocks(), block.getElevation(), block.getGrade(), block.getIsBidirectional(), block.getIsUnderground(), block.getLength(), block.getSpeedLimit());
        this.stationName = stationName;
    }

    //<editor-fold desc="Getters">

    public String getStationName()
    {
        return stationName;
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
        Station station = (Station) o;
        return Objects.equals(stationName, station.stationName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), stationName);
    }
}
