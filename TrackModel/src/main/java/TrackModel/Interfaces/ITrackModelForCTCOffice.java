package TrackModel.Interfaces;

import TrackModel.Models.Block;
import TrackModel.Models.Line;

import java.util.List;

public interface ITrackModelForCTCOffice
{
    void addBlock(Block block);

    Block getBlock(Line line, int id);

    List<Block> getBlocks(Line line);

    void setMultiplier(double multiplier);

    int getPassengersDisembarked();

    double getTime();
}
