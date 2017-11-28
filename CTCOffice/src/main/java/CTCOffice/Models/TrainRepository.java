package CTCOffice.Models;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class TrainRepository {
    private HashMap<Integer, Train> trains = new HashMap<>();

    public List<Train> getTrains() {
        List<Train> trainList = new ArrayList<>();
        trainList.addAll(trains.values());

        return trainList;
    }

    public Train getTrain(int trainId) {
        return trains.get(trainId);
    }

    public void addTrain(Train train) {
        trains.put(train.getIdentifier(), train);
    }
}
