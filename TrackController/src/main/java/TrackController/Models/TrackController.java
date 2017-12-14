package TrackController.Models;

import TrackController.PLC.PLC;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Models.Block;
import TrackModel.Models.Line;

import java.util.ArrayList;
import java.util.List;

public class TrackController {
    public int id;
    public String name;
    public List<Block> blocks;
    public Line line;
    public PLC plc;
    public List<String> plcFileNames;

    public TrackController(int id, String name, String plcFilename, ITrackModelForTrackController track)
    {
        this.id = id;
        this.name = name;
        this.blocks = new ArrayList<Block>();
        this.plc = new PLC(plcFilename, track);
        this.plcFileNames = new ArrayList<>();
        this.plcFileNames.add(plcFilename);
    }

    public boolean addBlock(Block block)
    {
        return blocks.add(block);
    }


    public void evaluateBlocks()
    {
        for (Block block : blocks)
        {
            plc.evaluateBlock(block);
        }
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
