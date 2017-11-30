package CTCOffice.Interfaces;

import CTCOffice.Models.Stop;
import CTCOffice.Models.Train;
import TrackModel.Models.Line;

import java.util.List;

public interface ITrainRepository {
    void addTrain(Train train);
    Train removeTrain(Line line, int id);
    Train getTrain(Line line, int id);
    List<Stop> getTrainSchedule(Line line, int id);
    List<Train> getTrains(Line line);
    boolean getMode();
    void setMode(boolean mode);
}
