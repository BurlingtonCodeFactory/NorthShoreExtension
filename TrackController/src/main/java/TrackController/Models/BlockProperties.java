package TrackController.Models;

import TrackModel.Models.Block;
import TrackModel.TrackModel;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class BlockProperties {
    private final Block block;
    private final TrackModel track;

    public BlockProperties(Block block, TrackModel track)
    {
        this.block = block;
        this.track = track;
    }

    private BooleanProperty occupied = new SimpleBooleanProperty() {
        @Override
        public void set(boolean v) {
            super.set(v);
            block.setIsOccupied(v);
        }
    };

    private StringProperty suggestedSpeed = new SimpleStringProperty() {
        @Override
        public void set(String v) {
            super.set(v);
            block.setSuggestedSpeed(Double.parseDouble(v));
        }
    };

    private StringProperty suggestedAuthority = new SimpleStringProperty() {
        @Override
        public void setValue(String v) {
            super.setValue(v);
            String[] blocks = v.split(",");
            List<Block> authority = new ArrayList<>();
            for(String b : blocks)
            {
                int number = Integer.parseInt(b);
                authority.add(track.getBlock(block.getLine(), number));

            }
            block.setSuggestedAuthority(authority);
        }
    };

    private BooleanProperty failure = new SimpleBooleanProperty() {
        @Override
        public void set(boolean v) {
            super.set(v);
            block.setFailed(v);
        }
    };

    public final boolean getOccupied() {
        return occupied.get();
    }
    public final void setOccupied(boolean value) {
        occupied.set(value);
    }
    public BooleanProperty occupiedProperty() {
        return occupied;
    }

    public final String getSuggestedSpeed() {
        return suggestedSpeed.get();
    }
    public final void setSuggestedSpeed(String value) {
        suggestedSpeed.set(value);
    }
    public StringProperty suggestedSpeedProperty() {
        return suggestedSpeed;
    }

    public final String getSuggestedAuthority() {
        return suggestedAuthority.get();
    }
    public final void setOccupied(String value) {
        suggestedAuthority.set(value);
    }
    public StringProperty suggestedAuthorityProperty() {
        return suggestedAuthority;
    }

    public final boolean getFailure() {
        return failure.get();
    }
    public final void setFailure(boolean value) {
        failure.set(value);
    }
    public BooleanProperty failureProperty() {
        return failure;
    }

}
