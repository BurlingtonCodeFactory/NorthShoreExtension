package CTCOffice.Interfaces;

import CTCOffice.Models.Train;

import java.io.BufferedReader;
import java.util.List;

public interface IFileService {
    boolean parsePresetScenario(BufferedReader bufferedReader);
    void parseTrainSchedule(BufferedReader bufferedReader);
}
