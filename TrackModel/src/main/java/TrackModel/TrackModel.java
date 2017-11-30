package TrackModel;

import TrackModel.Events.ClockTickUpdateEvent;
import TrackModel.Events.ClockTickUpdateListener;
import TrackModel.Events.OccupancyChangeEvent;
import TrackModel.Events.OccupancyChangeListener;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.Models.Block;
import TrackModel.Models.BlockType;
import TrackModel.Models.Line;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Models.Switch;
import com.google.inject.Singleton;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class acts as a repository for information about the state
// of the track. It should be treated as a singleton in order to
// maintain data consistency in the system.
@Singleton
public class TrackModel implements ITrackModelForCTCOffice, ITrackModelForTrackController, ITrackModelForTrainController, ITrackModelForTrainModel {
    private Map<Integer, Block> redLine;
    private Map<Integer, Block> greenLine;
    private double time;
    private double multiplier;

    private static List<ClockTickUpdateListener> clockTickUpdateListeners = new ArrayList<>();

    private List<OccupancyChangeListener> occupancyChangeListeners = new ArrayList<>();

    public TrackModel() {
        redLine = new HashMap<>();
        greenLine = new HashMap<>();
        time = 0;
        multiplier = 0;
    }

    public double getTime()
    {
        return time;
    }

    public void addInterval(double interval)
    {
        this.time += interval;
        fireClockTickUpdateEvent(this.time);
    }

    public double getMultiplier()
    {
        return multiplier;
    }

    public void setMultiplier(double multiplier)
    {
        this.multiplier = multiplier;
    }

    @Override
    public void addBlock(Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Cannot add null block.");
        }

        if (block.getLine() == Line.GREEN) {
            greenLine.put(block.getId(), block);
        }
        else {
            redLine.put(block.getId(), block);
        }
    }

    @Override
    public Block getBlock(Line line, int id) {
        return line == Line.GREEN ? greenLine.get(id) : redLine.get(id);
    }

    @Override
    public List<Block> getBlocks(Line line) {
        return line == Line.GREEN ? new ArrayList<>(greenLine.values()) : new ArrayList<>(redLine.values());
    }

    @Override
    public double getLengthByID(int ID, Line line)
    {
        return getBlock(line, ID).getLength();
    }

    @Override
    public double getGradeByID(int ID, Line line)
    {
        return getBlock(line, ID).getGrade();
    }

    @Override
    public double getFrictionByID(int ID, Line line)
    {
        return getBlock(line, ID).getCoefficientFriction();
    }

    @Override
    public double getAuthorityByID(int ID, Line line)
    {
        double authority = 0;
        for(Block block : getBlock(line, ID).getCommandedAuthority())
        {
            authority += block.getLength();
        }
        return authority;
    }

    @Override
    public double getSpeedByID(int ID, Line line)
    {
        return getBlock(line, ID).getCommandedSpeed();
    }

    @Override
    public int getBeaconByID(int ID, Line line)
    {
        return getBlock(line, ID).getBeacon();
    }

    @Override
    public boolean getUndergroundByID(int ID, Line line)
    {
        return getBlock(line, ID).getIsUnderground();
    }

    @Override
    public void setOccupancy(int ID, boolean isOccupied, Line line)
    {
        getBlock(line, ID).setIsOccupied(isOccupied);
    }

    public int getNextBlock(int previousBlock, int currentBlock, Line line)
    {
        Block block = getBlock(line, currentBlock);
        if(block.getBlockType() == BlockType.SWITCH)
        {
            Switch switchBlock = (Switch) block;
            if(switchBlock.getSwitchBase() == previousBlock)
            {
                return switchBlock.getSwitchState() ? switchBlock.getSwitchOne() : switchBlock.getSwitchZero();
            }
            else
            {
                return switchBlock.getSwitchBase();
            }
        }
        else
        {
            for(int b : block.getConnectedBlocks())
            {
                if(b != previousBlock)
                {
                    return b;
                }
            }
        }
        return -2;
    }

    public static synchronized void addClockTickUpdateListener(ClockTickUpdateListener listener) {
        clockTickUpdateListeners.add(listener);
    }

    public static synchronized void removeClockTickUpdateListener(ClockTickUpdateListener listener) {
        clockTickUpdateListeners.remove(listener);
    }

    private static synchronized void fireClockTickUpdateEvent(Object source)
    {
        ClockTickUpdateEvent event = new ClockTickUpdateEvent(source);
        for(ClockTickUpdateListener listener : clockTickUpdateListeners)
        {
            //System.out.println("Sending clock tick event to "+listener.getClass());
            Platform.runLater(
                    () -> listener.clockTickUpdateReceived(event)
            );
        }
    }
}
