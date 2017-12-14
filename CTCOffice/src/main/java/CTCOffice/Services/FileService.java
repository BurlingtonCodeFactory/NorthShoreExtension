//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package CTCOffice.Services;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.Stop;
import CTCOffice.Models.Train;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import com.google.inject.Inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService implements IFileService
{
    private ITrackModelForCTCOffice trackModel;
    private ITrainRepository trainRepository;

    @Inject
    public FileService(ITrackModelForCTCOffice trackModel, ITrainRepository trainRepository)
    {
        this.trackModel = trackModel;
        this.trainRepository = trainRepository;
    }

    @Override
    public boolean parsePresetScenario(BufferedReader bufferedReader)
    {
        return false;
    }

    public void parseTrainSchedule(BufferedReader bufferedReader)
    {
        String line;
        try
        {
            line = bufferedReader.readLine();
        }
        catch (IOException e)
        {
            return;
        }

        while (line != null)
        {
            Train train = parseTrain(line);
            if (train != null)
            {
                trainRepository.addTrain(train);
            }
            try
            {
                line = bufferedReader.readLine();
            }
            catch (IOException e)
            {
                return;
            }
        }
    }

    private Train parseTrain(String line)
    {
        String[] tokens = line.split(",");

        if (tokens.length < 2)
        {
            return null;
        }

        Line trainLine;
        switch (tokens[0].toLowerCase())
        {
            case "green":
                trainLine = Line.GREEN;
                break;
            case "red":
                trainLine = Line.RED;
                break;
            default:
                return null;
        }

        List<Stop> stops = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++)
        {
            int blockId;
            try
            {
                blockId = Integer.parseInt(tokens[i]);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
            Block block = trackModel.getBlock(trainLine, blockId);
            if (block == null)
            {
                return null;
            }

            stops.add(new Stop(block));
        }

        int newIdentifier = trainRepository.getTrains(trainLine).size() + 1;
        Train train = new Train(newIdentifier, trainLine, null, trackModel.getBlock(trainLine, 0));
        train.setSchedule(stops);

        return train;
    }
}
