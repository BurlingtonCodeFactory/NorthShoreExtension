package CTCOffice.Interfaces;

import TrackModel.Models.Block;

import java.util.List;

public interface IRouteService {
    List<Block> getShortestPath(Block previousBlock, Block currentBlock, Block destination);
    List<Block> getShortestPathWithMidpoint(Block previousBlock, Block currentBlock, Block midpointBlock, Block destination);
}
