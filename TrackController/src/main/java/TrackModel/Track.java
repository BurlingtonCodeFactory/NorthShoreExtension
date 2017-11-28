package TrackModel;

import TrackController.ITrackModelForTC;

import java.util.ArrayList;
import java.util.List;

public class Track implements ITrackModelForTC{
    List<Line> lines;

    public Track()
    {
        lines = new ArrayList<>();
        lines.add(new Line(LineType.RED));
        lines.get(0).addBlock(new Block(1, LineType.RED, BlockType.STANDARD, 50, 40, null, null, false ));
        lines.get(0).addBlock(new Block(2, LineType.RED, BlockType.STANDARD, 50, 40, null, null, false ));
        lines.get(0).addBlock(new Block(3, LineType.RED, BlockType.STANDARD, 50, 40, null, null, false ));
        lines.get(0).addBlock(new Block(4, LineType.RED, BlockType.STANDARD, 50, 40, null, null, false ));
        lines.get(0).addBlock(new Block(5, LineType.RED, BlockType.STANDARD, 50, 40, null, null, false ));
        lines.get(0).addBlock(new Block(6, LineType.RED, BlockType.STANDARD, 50, 40, null, null, false ));
        lines.get(0).addBlock(new Block(7, LineType.RED, BlockType.STATION, 75, 40, null, null, false ));
        lines.get(0).addBlock(new Block(8, LineType.RED, BlockType.STANDARD, 75, 40, null, null, false ));
        lines.get(0).addBlock(new Switch(9, LineType.RED, BlockType.SWITCH, 75, 40, null,null, null, null, false ));
        lines.get(0).addBlock(new Block(10, LineType.RED, BlockType.STANDARD, 75, 40, null, null, false ));
        lines.get(0).addBlock(new Block(11, LineType.RED, BlockType.STANDARD, 75, 40, null, null, false ));
        lines.get(0).addBlock(new Block(12, LineType.RED, BlockType.STANDARD, 75, 40, null, null, false ));
        lines.get(0).addBlock(new Block(13, LineType.RED, BlockType.STANDARD, 70, 40, null, null, false ));
        lines.get(0).addBlock(new Block(14, LineType.RED, BlockType.STANDARD, 60, 40, null, null, false ));
        lines.get(0).addBlock(new Switch(15, LineType.RED, BlockType.SWITCH, 60, 40, null,null, null, null, false ));
    }

    public Line getLine(LineType line)
    {
        if(line == LineType.RED)
        {
            return lines.get(1);
        }
        else
        {
            return lines.get(0);
        }
    }

    public Block getBlock(LineType line, int id)
    {
        return getLine(line).getBlock(id);
    }
}
