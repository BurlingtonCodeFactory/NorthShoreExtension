package CTCOffice.Interfaces;

import CTCOffice.Models.Block;

import java.util.List;

public interface IRouteService {
    List<Block> getShortestPath(Block previousBlock, Block currentBlock, Block destination, List<Block> blocks);
}
