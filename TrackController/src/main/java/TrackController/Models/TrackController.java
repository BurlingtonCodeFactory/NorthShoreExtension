package TrackController.Models;

import TrackController.PLC.PLC;
import TrackModel.LineType;
import TrackController.ITrackModelForTC;
import TrackModel.Block;

import java.util.ArrayList;
import java.util.List;

public class TrackController {
    public int id;
    public String name;
    public List<Block> blocks;
    public LineType line;
    public PLC plc;
    public List<String> plcFileNames;

    public TrackController(int id, String name, String plcFilename)
    {
        this.id = id;
        this.name = name;
        this.blocks = new ArrayList<Block>();
        this.plc = new PLC(plcFilename);
        this.plcFileNames = new ArrayList<>();
        this.plcFileNames.add(plcFilename);
        this.plcFileNames.add("controller2.PLC");
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
}
