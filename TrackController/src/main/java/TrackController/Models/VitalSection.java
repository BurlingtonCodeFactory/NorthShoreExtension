package TrackController.Models;


import TrackModel.Models.Block;

import java.util.ArrayList;
import java.util.List;

public class VitalSection {

    private boolean direction;
    private List<Block> blocks;

    public VitalSection()
    {
        direction = false;
        blocks = new ArrayList<Block>();
    }

    public boolean getDirection()
    {
        return direction;
    }

    public List<Block> getBlocks()
    {
        return blocks;
    }

    public void setDirection(boolean newDirection)
    {
        direction = newDirection;
    }

    public boolean addBlock(Block newBlock)
    {
        blocks.add(newBlock);
        return true;
    }
}
