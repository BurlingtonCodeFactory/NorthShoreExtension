package CTCOffice.Services;

import CTCOffice.CTCOffice;
import CTCOffice.Interfaces.IFileService;
import CTCOffice.Models.Schedule;
import TrackModel.Models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService implements IFileService {
    @Override
    public boolean parsePresetScenario(BufferedReader bufferedReader) {
        return false;
    }
    
    public List<Schedule> parseTrainSchedule(BufferedReader bufferedReader) {
        return null;
    }
}
