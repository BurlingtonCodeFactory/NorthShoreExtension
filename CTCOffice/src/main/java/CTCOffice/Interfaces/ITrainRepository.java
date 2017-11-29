package CTCOffice.Interfaces;

import CTCOffice.Models.Schedule;
import CTCOffice.Models.Train;

import java.util.List;

public interface ITrainRepository {
    void addTrain(Train train);
    Train removeTrain(int id);
    Train getTrain(int id);
    Schedule getTrainSchedule(int id);
    List<Train> getTrains();
}
