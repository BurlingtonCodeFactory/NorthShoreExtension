package com.company;

public interface ITrackModelForTrainController {

    public int getNextBlock(int prev, int cur);

    public Double getSpeedLimit(int cur);

    public Double getBlockLength(int cur);
}
