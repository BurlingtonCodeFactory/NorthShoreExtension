package CTCOffice.Models;

import TrackModel.Models.Block;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class BlockProperties {
    private BooleanProperty isOccupiedProperty;
    private BooleanProperty isUnderMaintenanceProperty;

    public BlockProperties(Block block) {
        isOccupiedProperty = new SimpleBooleanProperty(block.getIsOccupied());
        isUnderMaintenanceProperty = new SimpleBooleanProperty(block.getUnderMaintenance());
    }

    //<editor-fold desc="Getters">

    public BooleanProperty isOccupiedPropertyProperty() {
        return isOccupiedProperty;
    }

    public BooleanProperty isUnderMaintenancePropertyProperty() {
        return isUnderMaintenanceProperty;
    }

    //</editor-fold>
}
