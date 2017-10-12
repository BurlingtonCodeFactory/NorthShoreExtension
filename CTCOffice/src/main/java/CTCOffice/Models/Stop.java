package CTCOffice.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Stop {
    private ObjectProperty<Block> block = new SimpleObjectProperty<>();

    public Stop(Block block) {
        this.block.setValue(block);
    }

    public ObjectProperty<Block> getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block.setValue(block);
    }
}
