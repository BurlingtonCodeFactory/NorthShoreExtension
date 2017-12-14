package TrainController;

import java.util.List;

public class SkinnyBlock
{

    boolean station;

    int previous;

    int ID;
    double speedLimit;
    double length;
    List<Integer> connectedBlocks;


    public SkinnyBlock(double length, double speed, boolean station, int id, List<Integer> connected)
    {

        this.length = length;
        this.speedLimit = speed;
        this.connectedBlocks = connected;

        this.station = station;
        this.ID = id;

    }

    public List getConnected()
    {
        return connectedBlocks;
    }

    public double getLength()
    {
        return length;
    }

    public double getSpeedLimit()
    {
        return this.speedLimit;
    }

    public void setPrev(int prev)
    {
        this.previous = prev;
    }

    public int getID()
    {
        return this.ID;
    }

    public int getNext()
    {
        if (connectedBlocks.size() == 1)
        {
            return connectedBlocks.get(0);
        }
        else if (connectedBlocks.size() == 2)
        {
            if (previous == connectedBlocks.get(0))
            {
                return connectedBlocks.get(1);
            }
            else
            {
                return connectedBlocks.get(0);
            }
        }
        else if (connectedBlocks.size() == 3)
        {
            return -3;
        }
        return -1;

    }


    public int getPrev()
    {
        return this.previous;
    }
}
