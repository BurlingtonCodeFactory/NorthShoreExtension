package TrackModel.Interfaces;

import TrackModel.Models.Block;
import TrackModel.Models.Line;

import java.util.List;

public interface ITrackModelForTrainController {
    List<Block> getBlocks(Line line);
}
