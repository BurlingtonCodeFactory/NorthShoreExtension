package CTCOffice.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;

public class Train {
    private int identifier;
    private String line;
    private ObjectProperty<Block> currentLocation = new SimpleObjectProperty<>();
    private Block previousLocation;
    private IntegerProperty commandedSpeed = new SimpleIntegerProperty();
    private ObjectProperty<List<Block>> commandedAuthority = new SimpleObjectProperty<>();

    public int getIdentifier() {
        return identifier;
    }

    public String getLine() {
        return line;
    }

    public ObjectProperty<Block> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Block currentLocation) {
        this.currentLocation.setValue(currentLocation);
    }

    public Block getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(Block previousLocation) {
        this.previousLocation = previousLocation;
    }

    public IntegerProperty getCommandedSpeed() {
        return commandedSpeed;
    }

    public void setCommandedSpeed(int commandedSpeed) {
        this.commandedSpeed.setValue(commandedSpeed);
    }

    public ObjectProperty<List<Block>> getCommandedAuthority() {
        return commandedAuthority;
    }

    public void setCommandedAuthority(List<Block> commandedAuthority) {
        this.commandedAuthority.setValue(commandedAuthority);
    }

    public Train(int identifier, String line) {
        this.identifier = identifier;
        this.line = line;
    }

    @Override
    public String toString() {
        return line + "-" + identifier;
    }
}
