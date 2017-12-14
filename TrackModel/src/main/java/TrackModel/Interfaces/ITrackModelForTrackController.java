//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackModel.Interfaces;


import TrackModel.Models.Block;
import TrackModel.Models.Line;

import java.util.List;

public interface ITrackModelForTrackController
{
    List<Block> getBlocks(Line line);

    Block getBlock(Line line, int id);
}
