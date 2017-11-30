package TrackController;

import TrackModel.*;
import TrackModel.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import TrackController.Models.*;
import TrackController.PLC.PLC;

import javax.sound.midi.Track;

public class TrackViewController {

    @FXML
    ListView<String> controllerList;

    @FXML
    TableView<Block> blockList;

    @FXML
    ListView<String> plcList;

    @FXML
    TextArea plcContents;

    ObservableList<String> controllersView = FXCollections.observableArrayList();
    ObservableList<Block> blocksView = FXCollections.observableArrayList();


    @FXML
    Label blockNumber;

    @FXML
    Label blockLine;

    @FXML
    Label blockInfrastructure;

    @FXML
    Label blockSize;

    @FXML
    Label blockSpeed;

    @FXML
    Label blockAuthority;

    @FXML
    Label blockLights;

    @FXML
    Label blockHeater;

    @FXML
    Label blockRailBroken;

    @FXML
    Label blockTrackCircuit;

    @FXML
    Label blockPowerFailure;

    @FXML
    Label blockOccupancy;

    @FXML
    Label blockSwitchPosition;

    @FXML
    Label blockCrossingBar;

    @FXML
    TableColumn lineColumn;

    @FXML
    TableColumn numberColumn;

    @FXML
    TableColumn infrastructureColumn;

    private final TrackControllerManager manager;

    private Block blockSelected;

    public TrackViewController(TrackControllerManager manager)
    {
        this.manager = manager;
    }

    @FXML
    private void initialize()
    {
        System.out.println("Here in TRack Controller init");
        List<String> controllersList = new ArrayList<String>();
        for (TrackController controller: manager.greenControllers) {
            controllersList.add(controller.name);
        }
        controllersView.addAll(controllersList);
        controllerList.setItems(controllersView);

        lineColumn.setCellValueFactory(new PropertyValueFactory<Block, Line>("line"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Block,Integer>("id"));
        infrastructureColumn.setCellValueFactory(new PropertyValueFactory<Block, BlockType>("BlockType"));

    }

    @FXML public void controllerSelected(MouseEvent arg0)
    {

        String controllerName = controllerList.getSelectionModel().getSelectedItem();
        TrackController selectedController = manager.greenControllers.get(0);

        blockList.getItems().clear();
        ObservableList<Block> controllerBlocks = FXCollections.observableArrayList();
        controllerBlocks.addAll(selectedController.blocks);
        blockList.setItems(controllerBlocks);

        String plcString = "";
        File file;
        if(System.getProperty("user.dir").endsWith("System"))
        {
            file = new File("./build/resources/main/" + selectedController.plc.filename);
        }
        else
        {
            file = new File("./TrackController/build/resources/main/" + selectedController.plc.filename);
        }
        try {
            Scanner scanner = new Scanner(file);
            plcString = scanner.useDelimiter("\\A").next();
            scanner.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("File not found");
        }
        plcContents.setText(plcString);

        List<String> plcFileNames = selectedController.plcFileNames;
        plcList.getItems().clear();
        ObservableList<String> plcNames = FXCollections.observableArrayList();
        plcNames.addAll(plcFileNames);
        plcList.setItems(plcNames);

        blockSelected = blockList.getItems().get(0);
    }

    @FXML public void blockSelected(MouseEvent arg0)
    {
        blockSelected = blockList.getSelectionModel().getSelectedItem();
        refreshUI();
    }

    @FXML
    public void refreshUI()
    {
        blockNumber.setText(String.valueOf(blockSelected.getId()));
        blockLine.setText(blockSelected.getLine().toString());
        blockInfrastructure.setText(blockSelected.getBlockType().toString());
        blockSize.setText(String.valueOf(blockSelected.getLength())+" feet");
        blockSpeed.setText(String.valueOf(blockSelected.getCommandedSpeed())+"mph");
        if(blockSelected.getCommandedAuthority() != null)
        {
            blockAuthority.setText(String.valueOf(blockSelected.getCommandedAuthority().size()) +
                    (blockSelected.getCommandedAuthority().size() == 1 ? " block" : " blocks" ));
        }
        else
        {
            blockAuthority.setText("0 blocks");
        }
        blockLights.setText(blockSelected.getLightGreen() ? "Green" : "Red");
        blockHeater.setText(blockSelected.getHeaterOn() ? "On" : "Off");
        blockRailBroken.setText(blockSelected.getFailed() ? "Yes" : "No");  //TODO: change back to railBroken
        blockTrackCircuit.setText(blockSelected.getCircuitFailed() ? "Failed" : "Good");
        blockPowerFailure.setText(blockSelected.getPowerFailed() ? "Yes" : "No");
        blockOccupancy.setText(blockSelected.getIsOccupied() ? "Train" : "Free");

        if(blockSelected.getBlockType() == BlockType.SWITCH)
        {
            Switch switchBlock = (Switch) blockSelected;
            blockSwitchPosition.setText(switchBlock.getSwitchState() ?
                    String.valueOf(switchBlock.getSwitchOne()):
                    String.valueOf(switchBlock.getSwitchZero()));
        }
        else
        {
            blockSwitchPosition.setText("N/A");
        }

        if(blockSelected.getBlockType() == BlockType.CROSSING)
        {
            Crossing crossingBlock = (Crossing) blockSelected;
            blockCrossingBar.setText(crossingBlock.isCrossingOn() == true ? "On" : "Off");
        }
        else
        {
            blockCrossingBar.setText("N/A");
        }

    }

    @FXML public void switchOccupancy(MouseEvent arg0)
    {


    }

    @FXML public void switchFailure(MouseEvent arg0)
    {
        /*
        Block blockSelected = blockList.getSelectionModel().getSelectedItem();
        blockSelected.failure = !blockSelected.failure;
        blockRailBroken.setText(blockSelected.failure ? "Yes" : "No");

        Main.trackControllers.get(0).evaluateBlocks();
        */
    }

    @FXML public void switchAuthority(MouseEvent arg0)
    {
        /*
        Block blockSelected = blockList.getSelectionModel().getSelectedItem();
        if(blockSelected.speed == 0)
        {
            blockSelected.suggestedSpeed = 10;
            blockSelected.suggestedAuthority = new ArrayList<>();
            blockSelected.suggestedAuthority.add(blockSelected.rightNeighbor);
            if(blockSelected.rightNeighbor.rightNeighbor != null)
            {
                blockSelected.suggestedAuthority.add(blockSelected.rightNeighbor.rightNeighbor);
            }
        }
        else
        {
            blockSelected.suggestedSpeed = 0;
            blockSelected.suggestedAuthority = new ArrayList<>();
        }
        //Main.trackControllers.get(0).evaluateBlocks();
        blockSpeed.setText(String.valueOf(blockSelected.speed)+"mph");
        blockAuthority.setText(String.valueOf(blockSelected.authority.size()) +
                (blockSelected.authority.size() == 1 ? " block" : " blocks" ));
                */
    }

    @FXML public void plcSelected(MouseEvent arg0)
    {
        String plcName = plcList.getSelectionModel().getSelectedItem();
        File file;
        if(System.getProperty("user.dir").endsWith("System"))
        {
            file = new File("./build/resources/main/" + plcName);
        }
        else
        {
            file = new File("./TrackController/build/resources/main/" + plcName);
        }


        String plcString = "";
        try {
            Scanner scanner = new Scanner(file);
            plcString = scanner.useDelimiter("\\A").next();
            scanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        plcContents.setText(plcString);

    }

    @FXML public void plcActivated(MouseEvent arg0)
    {
        /*
        String plcName = plcList.getSelectionModel().getSelectedItem();
        String controllerName = controllerList.getSelectionModel().getSelectedItem();
        TrackController selectedController = Main.trackControllers.get(0);
        //selectedController.plc = new PLC(plcName);
        //Main.trackControllers.get(0).evaluateBlocks();
        */

    }

}
