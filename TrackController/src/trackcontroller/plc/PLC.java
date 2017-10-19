package trackcontroller.plc;

import trackcontroller.models.Block;
import trackcontroller.models.BlockType;
import trackcontroller.models.CTCCommand;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class PLC {
    public List<PLCRule> rules;
    public String filename;

    public PLC(String file)
    {
        filename = file;
        rules = new ArrayList<>();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String inCommand = null;

            while ((inCommand = reader.readLine()) != null) {
                PLCRule newRule = processLine(inCommand);
                if(newRule != null)
                {
                    rules.add(newRule);
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
                field.equals("failure") ||
                field.equals("route");
    }

    private boolean checkValidAction(String field)
    {
        return field.equals("crossingOn") ||
                field.equals("crossingOff") ||
                field.equals("switchZero") ||
                field.equals("switchOne") ||
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
                    infrastructure = BlockType.ALL;
                    break;
                case "crossing":
                    infrastructure = BlockType.CROSSING;
                    break;
                default:
                    infrastructure = BlockType.ALL;
                    break;
            }


            offset = command[1];
            field = command[2];
            value = command[3];
            action = command[4];
        }

        public boolean evaluateRule(Block block)
        {
            if(this.infrastructure == BlockType.ALL || this.infrastructure == block.infrastructure)
            {
                Block tempBlock;
                try
                {
                    int off = Integer.parseInt(offset);
                    tempBlock = getBlockByOffset(off, block);
                }
                catch(NumberFormatException e)
                {
                    if(offset.equals("L"))
                    {
                        tempBlock = block.switchZero;
                    }
                    else
                    {
                        tempBlock = block.switchOne;
                    }
                }
                final Block intendedBlock = tempBlock;

                if(intendedBlock == null)
                {
                    return true;
                }

                switch(field)
                {
                    case "occupied":
                        if(intendedBlock.trainPresent == Boolean.parseBoolean(value))
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
                        if(intendedBlock.failure == Boolean.parseBoolean(value))
                        {
                            performAction(action, block);
                        }
                        break;
                    case "route":
                        if(intendedBlock.suggestedAuthority.contains(block.switchZero))
                        {
                            performAction("switchZero", block);
                        }
                        else if(intendedBlock.suggestedAuthority.contains(block.switchOne))
                        {
                            performAction("switchOne", block);
                        }
                        break;
                }
            }

            return true;
        }

        public void performAction(String action, Block block)
        {
            switch(action)
            {
                case "crossingOn":
                    block.crossingOn = true;
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
            }
        }

        public Block getBlockByOffset(int offset, Block base)
        {
            boolean right = offset > 0;
            offset = Math.abs(offset);

            while(offset > 0) {
                if (right)
                {
                    base = base.rightNeighbor;
                }
                else
                {
                    base = base.leftNeighbor;
                }
                offset--;
            }
            return base;
        }
    }

    public boolean evaluateBlock(Block block)
    {
        for (PLCRule rule : rules) {
            rule.evaluateRule(block);
        }
        return true;
    }
}
