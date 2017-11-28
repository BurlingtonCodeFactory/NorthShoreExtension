package TrackModel.Models;

import java.util.ArrayList;

public class Block {
    private int id;
    private Line line;
    private BlockType blockType;

    private int beacon;
    private boolean circuitFailed;
    private double coefficientFriction;
    private Iterable<Block> commandedAuthority;
    private double commandedSpeed;
    private Iterable<Integer> connectedBlocks;
    private int elevation;
    private boolean failed;
    private double grade;
    private boolean heaterOn;
    private boolean isBidirectional;
    private boolean isOccupied;
    private boolean isUnderground;
    private double length;
    private boolean lightGreen;
    private boolean powerFailed;
    private boolean railBroken;
    private double speedLimit;
    private Iterable<Block> suggestedAuthority;
    private double suggestedSpeed;
    private boolean suggestMaintenance;
    private boolean underMaintenance;


    public Block(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, Iterable<Integer> connectedBlocks, int elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit){
        this.id = id;
        this.line = line;
        this.blockType = blockType;
        this.beacon = beacon;
        this.circuitFailed = false;
        this.coefficientFriction = coefficientFriction;
        this.commandedAuthority = new ArrayList<>();
        this.commandedSpeed = 0;
        this.connectedBlocks = connectedBlocks;
        this.elevation = elevation;
        this.failed = false;
        this.grade = grade;
        this.heaterOn = false;
        this.isBidirectional = isBidirectional;
        this.isOccupied = false;
        this.length = length;
        this.lightGreen = true;
        this.powerFailed = false;
        this.railBroken = false;
        this.speedLimit = speedLimit;
        this.suggestedAuthority = new ArrayList<>();
        this.suggestedSpeed = 0;
        this.suggestMaintenance = false;
        this.underMaintenance = false;
    }

    //<editor-fold desc="Getters">

    public int getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public int getBeacon() {
        return beacon;
    }

    public boolean getCircuitFailed() {
        return circuitFailed;
    }

    public double getCoefficientFriction() {
        return coefficientFriction;
    }

    public Iterable<Block> getCommandedAuthority() {
        return commandedAuthority;
    }

    public double getCommandedSpeed() {
        return commandedSpeed;
    }

    public Iterable<Integer> getConnectedBlocks() {
        return connectedBlocks;
    }

    public int getElevation() {
        return elevation;
    }

    public boolean getFailed() {
        return failed;
    }

    public double getGrade() {
        return grade;
    }

    public boolean getHeaterOn() {
        return heaterOn;
    }

    public boolean getIsBidirectional() {
        return isBidirectional;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public boolean getIsUnderground() {
        return isUnderground;
    }

    public double getLength() {
        return length;
    }

    public boolean getLightGreen() {
        return lightGreen;
    }

    public boolean getPowerFailed() {
        return powerFailed;
    }

    public boolean getRailBroken() {
        return railBroken;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public Iterable<Block> getSuggestedAuthority() {
        return suggestedAuthority;
    }

    public double getSuggestedSpeed() {
        return suggestedSpeed;
    }

    public boolean getSuggestMaintenance() {
        return suggestMaintenance;
    }

    public boolean getUnderMaintenance() {
        return underMaintenance;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setCircuitFailed(boolean circuitFailed) {
        this.circuitFailed = circuitFailed;
    }

    public void setCoefficientFriction(double coefficientFriction) {
        this.coefficientFriction = coefficientFriction;
    }

    public void setCommandedAuthority(Iterable<Block> commandedAuthority) {
        this.commandedAuthority = commandedAuthority;
    }

    public void setCommandedSpeed(double commandedSpeed) {
        this.commandedSpeed = commandedSpeed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public void setHeaterOn(boolean heaterOn) {
        this.heaterOn = heaterOn;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setLightGreen(boolean lightGreen) {
        this.lightGreen = lightGreen;
    }

    public void setPowerFailed(boolean powerFailed) {
        this.powerFailed = powerFailed;
    }

    public void setRailBroken(boolean railBroken) {
        this.railBroken = railBroken;
    }

    public void setSuggestedAuthority(Iterable<Block> suggestedAuthority) {
        this.suggestedAuthority = suggestedAuthority;
    }

    public void setSuggestedSpeed(double suggestedSpeed) {
        this.suggestedSpeed = suggestedSpeed;
    }

    public void setSuggestMaintenance(boolean suggestMaintenance) {
        this.suggestMaintenance = suggestMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    //</editor-fold>
}
