package CTCOffice.Interfaces;

import CTCOffice.Models.Stop;

import java.io.BufferedReader;
import java.util.List;

public interface IFileService {
    boolean parsePresetScenario(BufferedReader bufferedReader);
    List<Stop> parseTrainSchedule(BufferedReader bufferedReader);
}
