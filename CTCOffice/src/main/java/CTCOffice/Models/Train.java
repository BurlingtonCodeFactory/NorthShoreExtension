package CTCOffice.Models;

import TrackModel.Models.Block;
import TrackModel.Models.Line;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Train {
    private int identifier;
    private Line line;
    private ObjectProperty<Block> currentLocation = new SimpleObjectProperty<>();
    private Block previousLocation;
    private IntegerProperty commandedSpeed = new SimpleIntegerProperty();
    private ListProperty<Block> commandedAuthority = new SimpleListProperty<>();
    private ListProperty<Stop> stops = new SimpleListProperty<>();

    public int getIdentifier() {
        return identifier;
    }

    public Line getLine() {
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

    public ListProperty<Block> getCommandedAuthority() {
        return commandedAuthority;
    }

    public void setCommandedAuthority(List<Block> commandedAuthority) {
        this.commandedAuthority.setValue(FXCollections.observableArrayList(commandedAuthority));
    }

    public Train(int identifier, Line line) {
        this.identifier = identifier;
        this.line = line;
    }

    public ObservableList<Stop> getStops() {
        return stops.getValue();
    }

    public void setStops(List<Stop> stops) {
        this.stops.setValue(FXCollections.observableArrayList(stops));
    }

    public void addStop(Stop stop) {
        stops.getValue().add(stop);
    }

    public void removeStop(Stop stop) {
        stops.remove(stop);
    }

    @Override
    public String toString() {
        return line + "-" + identifier;
    }
}
