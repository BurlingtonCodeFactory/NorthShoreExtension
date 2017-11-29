package TrackModel;

import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import TrackModel.Interfaces.ITrackModelForTrackController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class acts as a repository for information about the state
// of the track. It should be treated as a singleton in order to
// maintain data consistency in the system.
public class TrackModel implements ITrackModelForCTCOffice, ITrackModelForTrackController, ITrackModelForTrainController, ITrackModelForTrainModel {
    private Map<Integer, Block> blocks; // TODO: utilize a dynamic array instead, need the O(1) lookup using integer, map is overkill
    private List<Block> redLine;
    private List<Block> greenLine;

    public TrackModel() {
        blocks = new HashMap<>();
        redLine = new ArrayList<>();
        greenLine = new ArrayList<>();
    }

    @Override
    public void addBlock(Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Cannot add null block.");
        }

        blocks.put(block.getId(), block);

        if (block.getLine() == Line.GREEN) {
            greenLine.add(block);
        }
        else {
            redLine.add(block);
        }
    }

    @Override
    public Block getBlock(int id) {
        return blocks.get(id);
    }

    @Override
    public Block getBlock(Line line, int id) { return line == Line.GREEN ? greenLine.get(id) : redLine.get(id); }

    @Override
    public List<Block> getBlocks(Line line) {
        return line == Line.GREEN ? greenLine : redLine;
    }

    @Override
    public List<Block> getBlocks() {
        return new ArrayList<>(blocks.values());
    }

    public double getLengthByID(int ID, Line line)
    {
        return getBlock(line, ID).getLength();
    }

    public double getGradeByID(int ID, Line line)
    {
        return getBlock(line, ID).getGrade();
    }

    public double getFrictionByID(int ID, Line line)
    {
        return getBlock(line, ID).getCoefficientFriction();
    }

    public double getAuthorityByID(int ID, Line line)
    {
        double authority = 0;
        for(Block block : getBlock(line, ID).getCommandedAuthority())
        {
            authority += block.getLength();
        }

        return authority;
    }

    public double getSpeedByID(int ID, Line line)
    {
        return getBlock(line, ID).getCommandedSpeed();
    }

    public int getBeaconByID(int ID, Line line)
    {
        return getBlock(line, ID).getBeacon();
    }

    public boolean getUndergroundByID(int ID, Line line)
    {
        return getBlock(line, ID).getIsUnderground();
    }

    public void setOccupancy(int ID, boolean isOccupied, Line line)
    {
        getBlock(line, ID).setIsOccupied(isOccupied);
    }
}
