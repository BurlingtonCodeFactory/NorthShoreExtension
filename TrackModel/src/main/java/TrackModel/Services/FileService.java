//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package TrackModel.Services;

import TrackModel.Interfaces.IFileService;
import TrackModel.Models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService implements IFileService
{

    @Override
    public List<Block> parseTrackLayout(BufferedReader bufferedReader)
    {
        if (bufferedReader == null)
        {
            throw new IllegalArgumentException("Cannot parse track layout from null bufferedReader.");
        }

        List<Block> blocks = new ArrayList<>();

        String line;
        try
        {
            line = bufferedReader.readLine();
        }
        catch (IOException e)
        {
            return null;
        }

        while (line != null)
        {
            Block block = parseBlock(line);
            if (block == null)
            {
                return null;
            }
            else
            {
                blocks.add(block);
            }

            try
            {
                line = bufferedReader.readLine();
            }
            catch (IOException e)
            {
                return null;
            }
        }

        return blocks;
    }

    public Block parseBlock(String input)
    {
        if (input == null)
        {
            throw new IllegalArgumentException("Cannot parse block from null string.");
        }

        String[] tokens = input.split(",");

        switch (tokens[2].toLowerCase())
        {
            case "standard":
                return parseStandard(tokens);
            case "switch":
                return parseSwitch(tokens);
            case "crossing":
                return parseCrossing(tokens);
            case "station":
                return parseStation(tokens);
            default:
                return null;
        }
    }

    private Block parseStandard(String[] tokens)
    {
        if (tokens.length < 12)
        {
            return null;
        }

        int id;
        try
        {
            id = Integer.parseInt(tokens[0]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        Line line;
        switch (tokens[1].toLowerCase())
        {
            case "green":
                line = Line.GREEN;
                break;
            case "red":
                line = Line.RED;
                break;
            default:
                return null;
        }

        BlockType blockType;
        switch (tokens[2].toLowerCase())
        {
            case "standard":
                blockType = BlockType.STANDARD;
                break;
            case "switch":
                blockType = BlockType.SWITCH;
                break;
            case "crossing":
                blockType = BlockType.CROSSING;
                break;
            case "station":
                blockType = BlockType.STATION;
                break;
            default:
                return null;
        }

        int beacon;
        try
        {
            beacon = Integer.parseInt(tokens[3]);
        }
        catch (NumberFormatException e)
        {

            return null;
        }


        double coefficientFriction;
        try
        {
            coefficientFriction = Double.parseDouble(tokens[4]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        if (coefficientFriction < 0)
        {
            return null;
        }

        String[] connectedIds = tokens[5].split(";");
        List<Integer> connectedBlocks = new ArrayList<>();
        for (String val : connectedIds)
        {
            try
            {
                connectedBlocks.add(Integer.parseInt(val));
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }

        double elevation;
        try
        {
            elevation = Double.parseDouble(tokens[6]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        double grade;
        try
        {
            grade = Double.parseDouble(tokens[7]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        boolean isBidirectional;
        switch (tokens[8].toLowerCase())
        {
            case "true":
                isBidirectional = true;
                break;
            case "false":
                isBidirectional = false;
                break;
            default:
                return null;
        }

        boolean isUnderground;
        switch (tokens[9].toLowerCase())
        {
            case "true":
                isUnderground = true;
                break;
            case "false":
                isUnderground = false;
                break;
            default:
                return null;
        }

        double length;
        try
        {
            length = Double.parseDouble(tokens[10]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        if (length < 0)
        {
            return null;
        }

        double speedLimit;
        try
        {
            speedLimit = Double.parseDouble(tokens[11]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        if (speedLimit < 0)
        {
            return null;
        }

        return new Block(id, line, blockType, beacon, coefficientFriction, connectedBlocks, elevation, grade, isBidirectional, isUnderground, length, speedLimit);
    }

    private Crossing parseCrossing(String[] tokens)
    {
        if (tokens.length != 12)
        {
            return null;
        }

        Block block = parseStandard(tokens);
        if (block == null)
        {
            return null;
        }

        return new Crossing(block);
    }

    private Station parseStation(String[] tokens)
    {
        if (tokens.length != 13)
        {
            return null;
        }

        Block block = parseStandard(tokens);
        if (block == null)
        {
            return null;
        }

        return new Station(block, tokens[12]);
    }

    private Switch parseSwitch(String[] tokens)
    {
        if (tokens.length != 15)
        {
            return null;
        }

        Block block = parseStandard(tokens);
        if (block == null)
        {
            return null;
        }

        int stationZero;
        try
        {
            stationZero = Integer.parseInt(tokens[12]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        int stationOne;
        try
        {
            stationOne = Integer.parseInt(tokens[13]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        int stationBase;
        try
        {
            stationBase = Integer.parseInt(tokens[14]);
        }
        catch (NumberFormatException e)
        {
            return null;
        }

        if (stationZero == stationOne || stationZero == stationBase || stationOne == stationBase)
        {
            return null;
        }

        return new Switch(block, stationZero, stationOne, stationBase);
    }

}
