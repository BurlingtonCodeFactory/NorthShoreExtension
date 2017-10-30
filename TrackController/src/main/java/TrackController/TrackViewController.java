package TrackController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
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
    ListView<String> block;

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

    @FXML
    private void initialize()
    {
        List<String> controllersList = new ArrayList<String>();
        for (TrackController controller:Main.trackControllers) {
            controllersList.add(controller.name);
        }
        controllersView.addAll(controllersList);
        controllerList.setItems(controllersView);

        lineColumn.setCellValueFactory(new PropertyValueFactory<Block,Line>("line"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Block,Integer>("number"));
        infrastructureColumn.setCellValueFactory(new PropertyValueFactory<Block,BlockType>("infrastructure"));

    }

    @FXML public void controllerSelected(MouseEvent arg0)
    {
        String controllerName = controllerList.getSelectionModel().getSelectedItem();
        TrackController selectedController = Main.trackControllers.get(0);

        blockList.getItems().clear();
        ObservableList<Block> controllerBlocks = FXCollections.observableArrayList();
        controllerBlocks.addAll(selectedController.blocks);
        blockList.setItems(controllerBlocks);

        String plcString = "";
        try {
            Scanner scanner = new Scanner(new File("./build/resources/main/plc/" + selectedController.plc.filename));
            plcString = scanner.useDelimiter("\\A").next();
            scanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        plcContents.setText(plcString);

        List<String> plcFileNames = selectedController.plcFileNames;
        plcList.getItems().clear();
        ObservableList<String> plcNames = FXCollections.observableArrayList();
        plcNames.addAll(plcFileNames);
        plcList.setItems(plcNames);

    }

    @FXML public void blockSelected(MouseEvent arg0)
    {
        Block blockSelected = blockList.getSelectionModel().getSelectedItem();
        blockNumber.setText(String.valueOf(blockSelected.number));
        blockLine.setText(blockSelected.line.toString());
        blockInfrastructure.setText(blockSelected.infrastructure.toString());
        blockSize.setText(String.valueOf(blockSelected.size)+" feet");
        blockSpeed.setText(String.valueOf(blockSelected.speed)+"mph");
        if(blockSelected.authority != null)
        {
            blockAuthority.setText(String.valueOf(blockSelected.authority.size()) +
                    (blockSelected.authority.size() == 1 ? " block" : " blocks" ));
        }
        else
        {
            blockAuthority.setText("0 blocks");
        }
        blockLights.setText(blockSelected.lightGreen ? "Green" : "Red");
        blockHeater.setText(blockSelected.heaterOn ? "On" : "Off");
        blockRailBroken.setText(blockSelected.failure ? "Yes" : "No");  //TODO: change back to railBroken
        blockTrackCircuit.setText(blockSelected.circuitFailure ? "Failed" : "Good");
        blockPowerFailure.setText(blockSelected.powerFailure ? "Yes" : "No");
        blockOccupancy.setText(blockSelected.trainPresent ? "Train" : "Free");

        if(blockSelected.infrastructure == BlockType.SWITCH)
        {
            blockSwitchPosition.setText(blockSelected.switchState ?
                    String.valueOf(blockSelected.switchOne.number):
                    String.valueOf(blockSelected.switchZero.number));
        }
        else
        {
            blockSwitchPosition.setText("N/A");
        }

        if(blockSelected.infrastructure == BlockType.CROSSING)
        {
            blockCrossingBar.setText(blockSelected.crossingOn == true ? "On" : "Off");
        }
        else
        {
            blockCrossingBar.setText("N/A");
        }
    }

    @FXML public void switchOccupancy(MouseEvent arg0)
    {
        Block blockSelected = blockList.getSelectionModel().getSelectedItem();
        blockSelected.trainPresent = !blockSelected.trainPresent;
        blockOccupancy.setText(blockSelected.trainPresent ? "Train" : "Free");

        //List<Block> testAuthority = new ArrayList<>();
        //testAuthority.add(blockSelected.rightNeighbor);

        Main.trackControllers.get(0).evaluateBlocks();
    }

    @FXML public void switchFailure(MouseEvent arg0)
    {
        Block blockSelected = blockList.getSelectionModel().getSelectedItem();
        blockSelected.failure = !blockSelected.failure;
        blockRailBroken.setText(blockSelected.failure ? "Yes" : "No");

        Main.trackControllers.get(0).evaluateBlocks();
    }

    @FXML public void switchAuthority(MouseEvent arg0)
    {
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
        Main.trackControllers.get(0).evaluateBlocks();
        blockSpeed.setText(String.valueOf(blockSelected.speed)+"mph");
        blockAuthority.setText(String.valueOf(blockSelected.authority.size()) +
                (blockSelected.authority.size() == 1 ? " block" : " blocks" ));
    }

    @FXML public void plcSelected(MouseEvent arg0)
    {
        String plcName = plcList.getSelectionModel().getSelectedItem();
        String plcString = "";
        try {
            Scanner scanner = new Scanner(new File("./build/resources/main/plc/" + plcName));
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
        String plcName = plcList.getSelectionModel().getSelectedItem();
        String controllerName = controllerList.getSelectionModel().getSelectedItem();
        TrackController selectedController = Main.trackControllers.get(0);
        selectedController.plc = new PLC(plcName);
        Main.trackControllers.get(0).evaluateBlocks();
    }

}
