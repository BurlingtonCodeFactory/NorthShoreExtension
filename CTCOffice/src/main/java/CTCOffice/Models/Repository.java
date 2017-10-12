package CTCOffice.Models;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class Repository {
    private HashMap<Integer, Block> blocks = new HashMap<>();
    private HashMap<Integer, Train> trains = new HashMap<>();

    public List<Block> getBlocks() {
        List<Block> blockList = new ArrayList<>();
        blockList.addAll(blocks.values());

        return blockList;
    }

    public Block getBlock(int blockId) {
        return blocks.get(blockId);
    }

    public void addBlock(Block block) {
        blocks.put(block.getBlockId(), block);
    }

    public List<Train> getTrains() {
        List<Train> trainList = new ArrayList<>();
        trainList.addAll(trains.values());

        return trainList;
    }

    public Train getTrain(int trainId) {
        return trains.get(trainId);
    }

    public void addTrain(Train train) {
        trains.put(train.getIdentifier(), train);
    }
}
