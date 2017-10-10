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


public class PLC {
    public HashSet<PLCRule> rules;


    public PLC(String file)
    {
        rules = new HashSet<>();
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
                field.equals("heaterOff");
    }

    private class PLCRule
    {
        BlockType infrastructure;
        int offset;
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
                    infrastructure = BlockType.ALL;
                    break;
            }

            offset = Integer.parseInt(command[1]);
            field = command[2];
            value = command[3];
            action = command[4];
        }

        public boolean evaluateRule(CTCCommand command)
        {
            if(this.infrastructure == BlockType.ALL || this.infrastructure == command.block.infrastructure)
            {
                Block intendedBlock = getBlockByOffset(offset, command.block);

                switch(field)
                {
                    case "occupied":
                        if(intendedBlock.trainPresent == Boolean.parseBoolean(value))
                        {
                            performAction(action, command.block, command);
                        }
                        break;
                    case "switch":
                        if(intendedBlock.switchState == (value.equals(1) == true))
                        {
                            performAction(action, command.block, command);
                        }
                        break;
                    case "failure":
                        if(intendedBlock.failure == Boolean.parseBoolean(value))
                        {
                            performAction(action, command.block, command);
                        }
                        break;
                    case "route":
                        if(command.authority.stream().filter(b -> b.number == intendedBlock.switchZero.number).count() == 1)
                        {
                            performAction("switchZero", command.block, command);
                        }
                        else if(command.authority.stream().filter(b -> b.number == intendedBlock.switchOne.number).count() == 1)
                        {
                            performAction("switchOne", command.block, command);
                        }
                        break;
                }
            }

            return true;
        }

        public void performAction(String action, Block block, CTCCommand command)
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
                    break;
                case "switchOne":
                    block.switchState = true;
                    break;
                case "continue":
                    block.speed = command.speed;
                    block.authority = command.authority;
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

    public boolean evaluateBlock(CTCCommand suggestedCommand)
    {
        for (PLCRule rule : rules) {
            rule.evaluateRule(suggestedCommand);
        }
        return true;
    }
}
