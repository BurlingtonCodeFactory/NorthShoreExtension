package TrackController.PLC;


import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Models.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PLC {
    public List<PLCRule> standardRules;
    public List<PLCRule> switchRules;
    public List<PLCRule> crossingRules;
    public String filename;
    public ITrackModelForTrackController track;

    public PLC(String file, ITrackModelForTrackController track)
    {
        filename = file;
        standardRules = new ArrayList<>();
        switchRules = new ArrayList<>();
        crossingRules = new ArrayList<>();
        this.track = track;
        BufferedReader reader = null;
        try
        {
            if(System.getProperty("user.dir").endsWith("System"))
            {
                reader = new BufferedReader(new FileReader("./build/resources/main/" + file)); // TODO From Andrew; To Ryan; This path seems weird to me, I worry it's going to need to be different when we deploy?

            }
            else
            {
                reader = new BufferedReader(new FileReader("./TrackController/build/resources/main/" + file)); // TODO From Andrew; To Ryan; This path seems weird to me, I worry it's going to need to be different when we deploy?
            }
            String inCommand = null;
            while ((inCommand = reader.readLine()) != null) {
                PLCRule newRule = processLine(inCommand);
                if(newRule != null)
                {
                    if(newRule.infrastructure == BlockType.STANDARD)
                    {
                        standardRules.add(newRule);
                    }
                    else if(newRule.infrastructure == BlockType.CROSSING)
                    {
                        crossingRules.add(newRule);
                    }
                    else if(newRule.infrastructure == BlockType.SWITCH)
                    {
                        switchRules.add(newRule);
                    }
                }
            }
        }
        catch(FileNotFoundException e)
        {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            System.out.println("Current relative path is: " + s);
            e.printStackTrace();
        }
        catch(IOException e)
        {
            System.out.println("Error reading in file");
        }
    }

    private PLCRule processLine(String inCommand)
    {
            String[] splitCommand = inCommand.split(" ");
            if(checkValidField(splitCommand[2]) && checkValidAction(splitCommand[4]))
            {
                return new PLCRule(splitCommand);
            }

            return null;
    }

    private boolean checkValidField(String field)
    {
        return field.equals("occupied") ||
                field.equals("switch") ||
                field.equals("lock") ||
                field.equals("failure") ||
                field.equals("route");
    }

    private boolean checkValidAction(String field)
    {
        return field.equals("crossingOn") ||
                field.equals("crossingOff") ||
                field.equals("switchZero") ||
                field.equals("switchOne") ||
                field.equals("lock") ||
                field.equals("unlock") ||
                field.equals("continue") ||
                field.equals("stop") ||
                field.equals("heaterOn") ||
                field.equals("heaterOff") ||
                field.equals("route");
    }

    private class PLCRule
    {
        BlockType infrastructure;
        String offset;
        String field;
        String value;
        String action;

        public PLCRule(String[] command)
        {
            switch(command[0])
            {
                case "switch":
                    infrastructure = BlockType.SWITCH;
                    break;
                case "standard":
                    infrastructure = BlockType.STANDARD;
                    break;
                case "crossing":
                    infrastructure = BlockType.CROSSING;
                    break;
                default:
                    infrastructure = BlockType.STANDARD;
                    break;
            }


            offset = command[1];
            field = command[2];
            value = command[3];
            action = command[4];
        }

        public boolean evaluateRule(Block block)
        {
            if(this.infrastructure == BlockType.STANDARD || this.infrastructure == block.getBlockType())
            {
                Block tempBlock =  null;
                try
                {
                    int off = Integer.parseInt(offset);
                    tempBlock = getBlockByOffset(off, block);
                }
                catch(NumberFormatException e)
                {
                    if(offset.equals("L"))
                    {
                        //tempBlock = block.switchZero;
                    }
                    else
                    {
                        //tempBlock = block.switchOne;
                    }
                }
                final Block intendedBlock = tempBlock;
                /*
                if(intendedBlock == null)
                {
                    return true;
                }
                */
                /*
                switch(field)
                {
                    case "occupied":
                        if(intendedBlock.isTrainPresent() == Boolean.parseBoolean(value))
                        {
                            performAction(action, block);
                        }
                        break;
                    case "switch":
                        if(intendedBlock.switchState == (value.equals(1) == true))
                        {
                            performAction(action, block);
                        }
                        break;
                    case "failure":
                        if(intendedBlock.isFailed() == Boolean.parseBoolean(value))
                        {
                            performAction(action, block);
                        }
                        break;
                    case "route":
                        if(intendedBlock.getSuggestedAuthority().contains(block.switchZero))
                        {
                            performAction("switchZero", block);
                        }
                        else if(intendedBlock.getSuggestedAuthority().contains(block.switchOne))
                        {
                            performAction("switchOne", block);
                        }
                        break;
                }
                */
            }

            return true;
        }

        public void evaluateStandardRule(Block block)
        {
            int off = Integer.parseInt(offset);
            Block intendedBlock = getBlockByOffset(off, block);
            switch(field)
            {
                case "occupied":
                    if(intendedBlock.getIsOccupied() == Boolean.parseBoolean(value))
                    {
                        performAction(action, block);
                    }
                    break;
                case "failure":
                    for(int num : block.getConnectedBlocks())
                    {
                        if(track.getBlock(block.getLine(),num).getFailed() == Boolean.parseBoolean(value))
                        {
                            performAction(action, block);
                            return;
                        }
                    }
                case "lock":
                    if(intendedBlock.hasLock() && intendedBlock.getLock() == Boolean.parseBoolean(value))
                    {
                        System.out.println("Performing "+action+" on "+block.getId());
                        performAction(action, block);
                    }
            }

        }


        public void evaluateSwitchRule(Switch block)
        {
            int off = Integer.parseInt(offset);
            int blockNum;
            if(off == 1)
            {
                blockNum = block.getSwitchOne();
            }
            else if(off == 0)
            {
                blockNum = block.getSwitchZero();
            }
            else
            {
                blockNum = block.getSwitchBase();
            }

            Block intendedBlock = track.getBlock(Line.GREEN, blockNum);

            switch(field)
            {
                case "occupied":
                    if(intendedBlock.getIsOccupied() == Boolean.parseBoolean(value)) {
                        if (off == 1 || off == 0)
                        {
                            System.out.println("Performing "+action+" on "+block.getId());
                            performAction(action, block);
                        }
                        else
                        {
                            if(action.equals("lock"))
                            {
                                System.out.println("Performing "+action+" on "+block.getId());
                                performAction(action, block);
                            }
                            else if (intendedBlock.getSuggestedAuthority().contains(track.getBlock(Line.GREEN, block.getSwitchOne()))) {
                                performAction("switchOne", block);
                            } else {
                                performAction("switchZero", block);
                            }
                        }
                    }
                    break;
            }

        }


        public void evaluateCrossingRule(Crossing block)
        {
            int off = Integer.parseInt(offset);
            Block intendedBlock = getBlockByOffset(off, block);
            switch(field)
            {
                case "occupied":
                    if(intendedBlock.getIsOccupied() == Boolean.parseBoolean(value))
                    {
                        performAction(action, block);
                    }
                    break;
            }

        }

        public void performAction(String action, Switch block)
        {
            switch(action)
            {
                case "switchZero":
                    block.setSwitchState(false);
                    break;
                case "switchOne":
                    block.setSwitchState(true);
                    break;
                case "lock":
                    block.setLock(true);
                    break;
                case "unlock":
                    block.setLock(false);
                    break;
            }
        }

        public void performAction(String action, Crossing block)
        {
            switch(action)
            {
                case "crossingOn":
                    block.setCrossingOn(true);
                    break;
                case "crossingOff":
                    block.setCrossingOn(false);
                    break;
            }
        }


        public void performAction(String action, Block block)
        {
            switch(action)
            {
                case "continue":
                    block.setCommandedSpeed(block.getSuggestedSpeed());
                    block.setCommandedAuthority(block.getSuggestedAuthority());
                    block.setLightGreen(true);
                    break;
                case "stop":
                    block.setCommandedSpeed(0);
                    block.setCommandedAuthority(new ArrayList<>());
                    block.setLightGreen(false);
                    break;
                /*
                case "crossingOn":
                    block.set = true;
                    break;
                case "crossingOff":
                    block.crossingOn = false;
                    break;
                case "switchZero":
                    block.switchState = false;
                    //block.rightNeighbor = block.switchZero;
                    break;
                case "switchOne":
                    block.switchState = true;
                    //block.rightNeighbor = block.switchOne;
                    break;
                case "continue":
                    block.speed = block.suggestedSpeed;
                    block.authority = block.suggestedAuthority;
                    block.lightGreen = true;
                    break;
                case "stop":
                    block.speed = 0;
                    block.authority = new ArrayList<>();
                    block.lightGreen = false;
                    break;
                case "heaterOn":
                    block.heaterOn = true;
                    break;
                case "heaterOff":
                    block.heaterOn = false;
                    break;
                    */
            }
        }

        public Block getBlockByOffset(int offset, Block base)
        {
            boolean right = offset > 0;
            offset = Math.abs(offset);

            while(offset > 0) {
                if (right)
                {
                    base = track.getBlock(Line.GREEN, base.getNextBlock());
                }
                else
                {
                    base = track.getBlock(Line.GREEN, base.getPreviousBlock());
                }
                offset--;
            }
            return base;
        }
    }

    public boolean evaluateBlock(Block block)
    {
        if(block.getSuggestedAuthority().size() == 0)
            //return true;

        if(block instanceof Switch)
        {
            for (PLCRule rule : switchRules) {
                rule.evaluateSwitchRule((Switch) block);
            }
        }
        else if (block instanceof Crossing)
        {
            for (PLCRule rule : crossingRules) {
                rule.evaluateCrossingRule((Crossing) block);
            }
        }

        for (PLCRule rule : standardRules) {
            rule.evaluateStandardRule(block);
        }
        return true;
    }

}
