//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package CTCOffice.Interfaces;

import java.io.BufferedReader;

public interface IFileService
{
    boolean parsePresetScenario(BufferedReader bufferedReader);

    void parseTrainSchedule(BufferedReader bufferedReader);
}
