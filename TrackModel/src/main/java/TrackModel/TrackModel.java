package TrackModel;

import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import TrackModel.Interfaces.ITrackModelForTrackController;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class acts as a repository for information about the state
// of the track. It should be treated as a singleton in order to
// maintain data consistency in the system.
@Singleton
public class TrackModel implements ITrackModelForCTCOffice, ITrackModelForTrackController, ITrackModelForTrainController, ITrackModelForTrainModel {
    private Map<Integer, Block> redLine;
    private Map<Integer, Block> greenLine;

    public TrackModel() {
        redLine = new HashMap<>();
        greenLine = new HashMap<>();
    }

    @Override
    public void addBlock(Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Cannot add null block.");
        }

        if (block.getLine() == Line.GREEN) {
            greenLine.put(block.getId(), block);
        }
        else {
            redLine.put(block.getId(), block);
        }
    }

    @Override
    public Block getBlock(Line line, int id) {
        return line == Line.GREEN ? greenLine.get(id) : redLine.get(id);
    }

    @Override
    public List<Block> getBlocks(Line line) {
        return line == Line.GREEN ? new ArrayList<>(greenLine.values()) : new ArrayList<>(redLine.values());
    }

    @Override
    public double getLengthByID(int ID, Line line)
    {
        return getBlock(line, ID).getLength();
    }

    @Override
    public double getGradeByID(int ID, Line line)
    {
        return getBlock(line, ID).getGrade();
    }

    @Override
    public double getFrictionByID(int ID, Line line)
    {
        return getBlock(line, ID).getCoefficientFriction();
    }

    @Override
    public double getAuthorityByID(int ID, Line line)
    {
        double authority = 0;
        for(Block block : getBlock(line, ID).getCommandedAuthority())
        {
            authority += block.getLength();
        }

        return authority;
    }

    @Override
    public double getSpeedByID(int ID, Line line)
    {
        return getBlock(line, ID).getCommandedSpeed();
    }

    @Override
    public int getBeaconByID(int ID, Line line)
    {
        return getBlock(line, ID).getBeacon();
    }

    @Override
    public boolean getUndergroundByID(int ID, Line line)
    {
        return getBlock(line, ID).getIsUnderground();
    }

    @Override
    public void setOccupancy(int ID, boolean isOccupied, Line line)
    {
        getBlock(line, ID).setIsOccupied(isOccupied);
    }
}
