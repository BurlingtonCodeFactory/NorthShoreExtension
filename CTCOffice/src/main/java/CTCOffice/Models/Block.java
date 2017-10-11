package CTCOffice.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;

public class Block {
    private int blockId;
    private String line;
    private int speedLimit;
    private List<Integer> travelTo;
    private String station;
    private BooleanProperty isUnderMaintenance = new SimpleBooleanProperty(false);
    private BooleanProperty isOccupied = new SimpleBooleanProperty(false) ;

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public List<Integer> getTravelTo() {
        return travelTo;
    }

    public void setTravelTo(List<Integer> travelTo) {
        this.travelTo = travelTo;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public BooleanProperty getIsUnderMaintenance() {
        return isUnderMaintenance;
    }

    public void setIsUnderMaintenance(boolean isUnderMaintenance) {
        this.isUnderMaintenance.setValue(isUnderMaintenance);
    }

    public BooleanProperty getOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied.setValue(occupied);
    }

    @Override
    public String toString() {
        return station == null ? Integer.toString(blockId) : station;
    }
}


