package CTCOffice.Models;

import CTCOffice.Interfaces.ITrainRepository;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class TrainRepository implements ITrainRepository {
    private HashMap<Integer, Train> trains = new HashMap<>();

    public List<Train> getTrains() {
        return new ArrayList<>(trains.values());
    }

    public Train getTrain(int trainId) {
        return trains.get(trainId);
    }

    @Override
    public Schedule getTrainSchedule(int id) {
        return null;
    }

    public void addTrain(Train train) {
        trains.put(train.getIdentifier(), train);
    }

    @Override
    public Train removeTrain(int id) {
        return null;
    }
}
