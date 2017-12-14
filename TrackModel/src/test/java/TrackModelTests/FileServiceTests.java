/*
package TrackModelTests;

import TrackModel.Services.FileService;
import TrackModel.Models.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FileServiceTests {
    /*
    @Test
    public void ValidStandardBlock_Parses() {
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
    public void ValidStationBlock_Parses() {
        // Arrange
        FileService fileService = new FileService();

        String line = "3,green,station,0,0.5,1;2,1.10,2.1,false,true,40,25,TestStation";
        Station expected = new Station(3, Line.GREEN, BlockType.STATION, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 1.10, 2.1, false, true, 40, 25, "TestStation");

        // Act
        Station result = (Station) fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void ValidCrossingBlock_Parses() {
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
    public void ValidSwitchBlock_Parses() {
        // Arrange
        FileService fileService = new FileService();

        String line = "1,green,switch,0,0.5,2;3,10,1,false,true,40,25,2,3,1";
        Switch expected = new Switch(1, Line.GREEN, BlockType.SWITCH, 0, 0.5, new ArrayList<>(Arrays.asList(2, 3)), 10, 1, false, true, 40, 25, 2, 3, 1);

        // Act
        Switch result = (Switch) fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void InvalidLine_ParseFailure() {
        // Arrange
        FileService fileService = new FileService();

        String line = "1,blue,switch,0,0.5,2;3,10,1,false,true,40,25,2,3,1";
        Block expected = null;

        // Act
        Block result = fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void NonInteger_ParseFailure() {
        // Arrange
        FileService fileService = new FileService();

        String line = "hi,green,switch,0,0.5,2;3,10,1,false,true,40,25,2,3,1";
        Block expected = null;

        // Act
        Block result = fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void NonDouble_ParseFailure() {
        // Arrange
        FileService fileService = new FileService();

        String line = "1,green,switch,0,Lol,2;3,10,1,false,true,40,25,2,3,1";
        Block expected = null;

        // Act
        Block result = fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void NegativeSpeedLimit_ParseFailure() {
        // Arrange
        FileService fileService = new FileService();

        String line = "3,green,standard,0,0.5,1;2,10,1,false,true,40,-25";
        Block expected = null;

        // Act
        Block result = fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void NegativeLength_ParseFailure() {
        // Arrange
        FileService fileService = new FileService();

        String line = "3,green,standard,0,0.5,1;2,10,1,false,true,-40,25";
        Block expected = null;

        // Act
        Block result = fileService.parseBlock(line);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void ValidLayoutWithSingleLine_Parsed() throws IOException {
        // Arrange
        FileService fileService = new FileService();

        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn("3,green,standard,0,0.5,1;2,10,1,false,true,40,25").thenReturn(null);

        List<Block> expected = new ArrayList<>();
        Block block = new Block(3, Line.GREEN, BlockType.STANDARD, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 10, 1, false, true, 40, 25);
        expected.add(block);

        // Act
        List<Block> result = fileService.parseTrackLayout(bufferedReader);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void ValidLayoutWithMultipleLines_Parsed() throws IOException {
        // Arrange
        FileService fileService = new FileService();

        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn("3,green,standard,0,0.5,1;2,10,1,false,true,40,25")
                .thenReturn("3,green,station,0,0.5,1;2,10,1,false,true,40,25,TestStation")
                .thenReturn("3,green,crossing,0,0.5,1;2,10,1,false,true,40,25")
                .thenReturn("1,green,switch,0,0.5,2;3,10,1,false,true,40,25,2,3,1")
                .thenReturn(null);

        List<Block> expected = new ArrayList<>();
        Block block1 = new Block(3, Line.GREEN, BlockType.STANDARD, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 10, 1, false, true, 40, 25);
        Station block2 = new Station(3, Line.GREEN, BlockType.STATION, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 10, 1, false, true, 40, 25, "TestStation");
        Crossing block3 = new Crossing(3, Line.GREEN, BlockType.CROSSING, 0, 0.5, new ArrayList<>(Arrays.asList(1, 2)), 10, 1, false, true, 40, 25);
        Switch block4 = new Switch(1, Line.GREEN, BlockType.SWITCH, 0, 0.5, new ArrayList<>(Arrays.asList(2, 3)), 10, 1, false, true, 40, 25, 2, 3, 1);
        expected.add(block1);
        expected.add(block2);
        expected.add(block3);
        expected.add(block4);

        // Act
        List<Block> result = fileService.parseTrackLayout(bufferedReader);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void GreenLine_Parsed() {
        // Arrange
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("build/resources/test/greenLine.csv");
        } catch (FileNotFoundException e) {
            fail("greenLine.csv not found.");
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        FileService fileService = new FileService();

        // Act
        List<Block> blocks = fileService.parseTrackLayout(bufferedReader);

        // Assert
        assertTrue(blocks != null);
        assertEquals(151, blocks.size());
    }
}*/