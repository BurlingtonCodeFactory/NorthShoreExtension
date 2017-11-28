package TrackModel.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {
    private int id;
    private Line line;
    private BlockType blockType;

    private int beacon;
    private boolean circuitFailed;
    private double coefficientFriction;
    private List<Block> commandedAuthority;
    private double commandedSpeed;
    private List<Integer> connectedBlocks;
    private double elevation;
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
    private List<Block> suggestedAuthority;
    private double suggestedSpeed;
    private boolean suggestMaintenance;
    private boolean underMaintenance;


    public Block(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, List<Integer> connectedBlocks, double elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit){
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
        this.isUnderground = isUnderground;
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

    public List<Block> getCommandedAuthority() {
        return commandedAuthority;
    }

    public double getCommandedSpeed() {
        return commandedSpeed;
    }

    public List<Integer> getConnectedBlocks() {
        return connectedBlocks;
    }

    public double getElevation() {
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

    public List<Block> getSuggestedAuthority() {
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

        public void setCommandedAuthority(List<Block> commandedAuthority) {
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

        public void setSuggestedAuthority(List<Block> suggestedAuthority) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return id == block.id &&
                beacon == block.beacon &&
                circuitFailed == block.circuitFailed &&
                Double.compare(block.coefficientFriction, coefficientFriction) == 0 &&
                Double.compare(block.commandedSpeed, commandedSpeed) == 0 &&
                elevation == block.elevation &&
                failed == block.failed &&
                Double.compare(block.grade, grade) == 0 &&
                heaterOn == block.heaterOn &&
                isBidirectional == block.isBidirectional &&
                isOccupied == block.isOccupied &&
                isUnderground == block.isUnderground &&
                Double.compare(block.length, length) == 0 &&
                lightGreen == block.lightGreen &&
                powerFailed == block.powerFailed &&
                railBroken == block.railBroken &&
                Double.compare(block.speedLimit, speedLimit) == 0 &&
                Double.compare(block.suggestedSpeed, suggestedSpeed) == 0 &&
                suggestMaintenance == block.suggestMaintenance &&
                underMaintenance == block.underMaintenance &&
                line == block.line &&
                blockType == block.blockType &&
                Objects.equals(commandedAuthority, block.commandedAuthority) &&
                Objects.equals(connectedBlocks, block.connectedBlocks) &&
                Objects.equals(suggestedAuthority, block.suggestedAuthority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, blockType, beacon, circuitFailed, coefficientFriction, commandedAuthority, commandedSpeed, connectedBlocks, elevation, failed, grade, heaterOn, isBidirectional, isOccupied, isUnderground, length, lightGreen, powerFailed, railBroken, speedLimit, suggestedAuthority, suggestedSpeed, suggestMaintenance, underMaintenance);
    }
}
