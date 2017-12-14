//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package TrackModel.Interfaces;

import TrackModel.Models.Block;

import java.io.BufferedReader;
import java.util.List;

public interface IFileService
{
    List<Block> parseTrackLayout(BufferedReader reader);
}
