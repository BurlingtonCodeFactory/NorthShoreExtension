package CTCOffice.Models;

import CTCOffice.Interfaces.ITrainRepository;
import TrackModel.Models.Line;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class TrainRepository implements ITrainRepository
{
    private HashMap<Integer, Train> redTrains;
    private HashMap<Integer, Train> greenTrains;
    private boolean mode;

    public TrainRepository()
    {
        this.redTrains = new HashMap<>();
        this.greenTrains = new HashMap<>();
        this.mode = false;
    }

    @Override
    public List<Train> getTrains(Line line)
    {
        return line == Line.GREEN ? new ArrayList<>(greenTrains.values()) : new ArrayList<>(redTrains.values());
    }

    @Override
    public boolean getMode()
    {
        return mode;
    }

    @Override
    public void setMode(boolean mode)
    {
        this.mode = mode;
    }

    @Override
    public Train getTrain(Line line, int trainId)
    {
        return line == Line.GREEN ? greenTrains.get(trainId) : redTrains.get(trainId);
    }

    @Override
    public List<Stop> getTrainSchedule(Line line, int id)
    {
        return getTrain(line, id).getSchedule();
    }

    @Override
    public void addTrain(Train train)
    {
        if (train.getLine() == Line.GREEN)
        {
            greenTrains.put(train.getId(), train);
        }
        else
        {
            redTrains.put(train.getId(), train);
        }
    }

    @Override
    public Train removeTrain(Line line, int id)
    {
        Train train;
        if (line == Line.GREEN)
        {
            train = greenTrains.remove(id);
        }
        else
        {
            train = redTrains.remove(id);
        }

        return train;
    }
}
