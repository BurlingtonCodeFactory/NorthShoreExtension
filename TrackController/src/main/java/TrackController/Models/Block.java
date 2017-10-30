package TrackController.Models;

import java.util.ArrayList;
import java.util.List;

public class Block {
    public int number;
    public Line line;
    public BlockType infrastructure;
    public int size;
    public double speed;
    public double suggestedSpeed;
    public boolean trainPresent;
    public List<Block> authority;
    public List<Block> suggestedAuthority;
    public boolean lightGreen;
    public Block leftNeighbor;
    public Block rightNeighbor;
    public boolean heaterOn = false;
    public boolean crossingOn = false;
    public boolean switchState = false;
    public boolean failure = false;
    public boolean railBroken = false;
    public boolean circuitFailure = false;
    public boolean powerFailure = false;
    public Block switchZero;
    public Block switchOne;


    public Block(int number, Line line, BlockType infra, int size, double speed, boolean trainPresent, List<Block> authority, boolean lightGreen){
        this.number = number;
        this.line = line;
        this.infrastructure = infra;
        this.size = size;
        this.speed = speed;
        this.trainPresent = trainPresent;
        this.authority = authority;
        this.lightGreen = lightGreen;
        this.suggestedAuthority = new ArrayList<Block>();
    }

    public int getNumber()
    {
        return number;
    }

    public Line getLine()
    {
        return line;
    }

    public BlockType getInfrastructure()
    {
        return infrastructure;
    }
}