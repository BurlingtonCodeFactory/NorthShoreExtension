package CTCOffice.Services;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Models.Block;
import CTCOffice.Models.Schedule;

import java.util.ArrayList;
import java.util.List;

public class FileService implements IFileService {
    @Override
    public List<Schedule> parseSchedule(String schedule) {
        return null;
    }

    @Override
    public Block parseBlock(String trackLayout) {
        String[] tokens = trackLayout.split(",");

        Block block = new Block();
        block.setBlockId(Integer.parseInt(tokens[0]));
        block.setLine(tokens[1]);
        block.setSpeedLimit(Integer.parseInt(tokens[2]));

        String[] connectsTo = tokens[3].split(";");
        List<Integer> blocks = new ArrayList<>();
        for (String connect : connectsTo) {
            blocks.add(Integer.parseInt(connect));
        }
        block.setTravelTo(blocks);

        if (tokens.length == 5) {
            block.setStation(tokens[4]);
        }

        return block;
    }
}
