package CTCOffice.Services;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Models.Stop;

import java.io.BufferedReader;
import java.util.List;

public class FileService implements IFileService {
    @Override
    public boolean parsePresetScenario(BufferedReader bufferedReader) {
        return false;
    }
    
    public List<Stop> parseTrainSchedule(BufferedReader bufferedReader) {
        return null;
    }
}
