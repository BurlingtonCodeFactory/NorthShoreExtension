package TrackModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class Line {
    private List<Block> blocks;
    private LineType lineType;

    public Line(LineType line)
    {
        blocks = new ArrayList<Block>();
        lineType = line;
    }

    public void addBlock(Block block)
    {
        blocks.add(block);
    }

    public Block getBlock(int id)
    {
        return blocks.stream().filter(b -> b.getNumber() == id).findFirst().get();
    }
}
