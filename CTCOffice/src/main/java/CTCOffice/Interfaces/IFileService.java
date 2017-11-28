package CTCOffice.Interfaces;


import CTCOffice.Models.Schedule;
import TrackModel.Models.Block;

import java.io.BufferedReader;
import java.util.List;

public interface IFileService {
    boolean parsePresetScenario(BufferedReader bufferedReader);
    List<Block> parseTrackLayout(BufferedReader bufferedReader);
    List<Schedule> parseTrainSchedule(BufferedReader bufferedReader);
}
