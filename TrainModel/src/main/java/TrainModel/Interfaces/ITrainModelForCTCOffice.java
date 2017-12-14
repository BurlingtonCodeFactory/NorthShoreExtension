package TrainModel.Interfaces;

import TrackModel.Models.Line;

public interface ITrainModelForCTCOffice
{
    int createTrain(int previousBlock, int currentBlock, int cars, boolean PIDSetupBypass, Line line);
}
