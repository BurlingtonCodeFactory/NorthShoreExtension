package CTCOffice.Interfaces;

import CTCOffice.Models.Block;
import CTCOffice.Models.Schedule;

import java.util.List;

public interface IFileService {
    List<Schedule> parseSchedule(String schedule);
    Block parseBlock(String trackLayout);
}
