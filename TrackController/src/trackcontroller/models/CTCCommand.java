package trackcontroller.models;

import java.util.List;

public class CTCCommand {
    public Block block;
    public double speed;
    public List<Block> authority;

    public CTCCommand(Block block, double speed, List<Block> authority)
    {
        this.block = block;
        this.speed = speed;
        this.authority = authority;
    }
}
