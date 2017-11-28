package TrackModel.Interfaces;

import TrackModel.Models.Block;
import TrackModel.Models.Line;

public interface ITrackModelForCTCOffice {
    void addBlock(Block block);
    Block getBlock(int id);
    Iterable<Block> getBlocks(Line line);
    Iterable<Block> getBlocks();
}
