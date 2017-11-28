package TrackController;

import TrackModel.Block;
import TrackModel.Line;
import TrackModel.LineType;

public interface ITrackModelForTC {
    Line getLine(LineType line);
    Block getBlock(LineType line, int id);
}
