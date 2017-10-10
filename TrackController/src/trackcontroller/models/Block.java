package trackcontroller.models;

import java.util.List;

public class Block {
    public int number;
    public Line line;
    public BlockType infrastructure;
    public int size;
    public double speed;
    public boolean trainPresent;
    public List<Block> authority;
    public boolean lightGreen;
    public Block leftNeighbor;
    public Block rightNeighbor;
    public boolean heaterOn = false;
    public boolean crossingOn = false;
    public boolean switchState = false;
    public boolean failure = false;
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
    }
}