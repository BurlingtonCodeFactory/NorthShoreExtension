package TrackModel;

import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class acts as a repository for information about the state
// of the track. It should be treated as a singleton in order to
// maintain data consistency in the system.
public class TrackModel implements ITrackModelForCTCOffice, ITrackModelForTrackController {
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
}
