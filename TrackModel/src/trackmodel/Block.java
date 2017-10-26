package trackmodel;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private int number;
    private LineType lineType;
    private BlockType infrastructure;
    private int size;
    private boolean trainPresent;
    private double speed;
    private double suggestedSpeed;
    private List<Block> authority;
    private List<Block> suggestedAuthority;
    private boolean lightGreen;
    private Block previousBlock;
    private Block nextBlock;
    private boolean heaterOn;
    private boolean failed;
    private boolean railBroken;
    private boolean circuitFailed;
    private boolean powerFailed;


    public Block(int number, LineType lineType, BlockType infra, int size, Block previousBlock, Block nextBlock){
        this.number = number;
        this.lineType = lineType;
        this.infrastructure = infra;
        this.size = size;
        this.trainPresent = false;
        this.speed = 0;
        this.suggestedSpeed = 0;
        this.authority = new ArrayList<>();
        this.suggestedAuthority = new ArrayList<>();
        this.lightGreen = true;
        this.previousBlock = previousBlock;
        this.nextBlock = nextBlock;
        this.heaterOn = false;
        this.failed = false;
        this.railBroken = false;
        this.circuitFailed = false;
        this.powerFailed = false;
    }

    //<editor-fold desc="Getters">

    public int getNumber()
    {
        return number;
    }

    public LineType getLineType()
    {
        return lineType;
    }

    public BlockType getInfrastructure()
    {
        return infrastructure;
    }

    public int getSize()
    {
        return size;
    }

    public boolean isTrainPresent()
    {
        return trainPresent;
    }

    public double getSpeed()
    {
        return speed;
    }

    public double getSuggestedSpeed()
    {
        return suggestedSpeed;
    }

    public List<Block> getAuthority()
    {
        return authority;
    }

    public List<Block> getSuggestedAuthorityAuthority()
    {
        return suggestedAuthority;
    }

    public boolean getLightGreen()
    {
        return lightGreen;
    }

    public Block getPreviousBlock()
    {
        return previousBlock;
    }

    public Block getNextBlock()
    {
        return nextBlock;
    }

    public boolean isHeaterOn()
    {
        return heaterOn;
    }

    public boolean isFailed()
    {
        return failed;
    }

    public boolean isRailBroken()
    {
        return railBroken;
    }

    public boolean isCircuitFailed()
    {
        return circuitFailed;
    }

    public boolean isPowerFailed()
    {
        return powerFailed;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setSize(int newSize)
    {
        this.size = newSize;
    }

    public void setTrainPresent(boolean newTrainPresent)
    {
        this.trainPresent = newTrainPresent;
    }

    public void setSpeed(double newSpeed)
    {
        speed = newSpeed;
    }

    public void setSuggestedSpeed(double newSuggestedSpeed)
    {
        suggestedSpeed = newSuggestedSpeed;
    }

    public void setAuthority(List<Block> newAuthority)
    {
        authority = newAuthority;
    }

    public void setLightGreen(boolean newLightGreen)
    {
        lightGreen = newLightGreen;
    }

    public void setNextBlock(Block newNextBlock)
    {
        this.nextBlock = newNextBlock;
    }

    public void setHeaterOn(boolean newHeaterOn)
    {
        heaterOn = newHeaterOn;
    }

    public void setFailed(boolean newSetFailed)
    {
        failed = newSetFailed;
    }

    public void setRailBroken(boolean newRailBroken)
    {
        railBroken = newRailBroken;
    }

    public void setCircuitFailed(boolean newCircuitFailed)
    {
        circuitFailed = newCircuitFailed;
    }

    public void setPowerFailed(boolean newPowerFailed)
    {
        powerFailed = newPowerFailed;
    }

    //</editor-fold>



}
