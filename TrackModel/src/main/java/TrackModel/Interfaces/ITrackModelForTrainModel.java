package TrackModel.Interfaces;

import TrackModel.Models.Line;

public interface ITrackModelForTrainModel {

    public double getLengthByID(int ID, Line line);

    public double getGradeByID(int ID, Line line);

    public double getFrictionByID(int ID, Line line);

    public double getAuthorityByID(int ID, Line line);

    public double getSpeedByID(int ID, Line line);

    public int getBeaconByID(int ID, Line line);

    public boolean getUndergroundByID(int ID, Line line);

    public void setOccupancy(int ID, boolean isOccupied, Line line);

    public int getNextBlock(int previousBlock, int currentBlock, Line line);

}
