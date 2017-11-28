package CTCOfficeTests.ServicesTests;

import CTCOffice.Services.FileService;
import TrackModel.Models.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FileServiceTests {
    @Test
    public void ValidStandardBlock_Parses() throws IOException {
        // Arrange
        FileService fileService = new FileService();

        String line = "3,green,standard,0,0.5,1;2,10,1,false,true,40,25";
        Block expected = new Block(3, Line.GREEN, BlockType.STANDARD, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 10, 1, false, true, 40, 25);

        // Act
        Block result = fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void ValidStationBlock_Parses() throws IOException {
        // Arrange
        FileService fileService = new FileService();

        String line = "3,green,station,0,0.5,1;2,10,1,false,true,40,25,TestStation";
        Station expected = new Station(3, Line.GREEN, BlockType.STATION, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 10, 1, false, true, 40, 25, "TestStation");

        // Act
        Station result = (Station) fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void ValidCrossingBlock_Parses() throws IOException {
        // Arrange
        FileService fileService = new FileService();

        String line = "3,green,crossing,0,0.5,1;2,10,1,false,true,40,25";
        Crossing expected = new Crossing(3, Line.GREEN, BlockType.CROSSING, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 10, 1, false, true, 40, 25);

        // Act
        Crossing result = (Crossing) fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void ValidSwitchBlock_Parses() throws IOException {
        // Arrange
        FileService fileService = new FileService();

        String line = "1,green,switch,0,0.5,2;3,10,1,false,true,40,25,2,3,1";
        Switch expected = new Switch(1, Line.GREEN, BlockType.SWITCH, 0, 0.5, new ArrayList<>(Arrays.asList(2, 3)), 10, 1, false, true, 40, 25, 2, 3, 1);

        // Act
        Switch result = (Switch) fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }
}
