package trackcontroller;

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
import trackcontroller.models.*;

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

    @FXML public void controllerSelected(MouseEvent arg0) {
        String controllerName = controllerList.getSelectionModel().getSelectedItem();
        TrackController selectedController = Main.trackControllers.stream().filter(c -> c.name.equals(controllerName)).findFirst().get();

        blockList.getItems().clear();
        ObservableList<Block> controllerBlocks = FXCollections.observableArrayList();
        controllerBlocks.addAll(selectedController.blocks);
        blockList.setItems(controllerBlocks);

        String plcString = "";
        try {
            Scanner scanner = new Scanner(new File(selectedController.plc.filename));
            plcString = scanner.useDelimiter("\\A").next();
            scanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        plcContents.setText(plcString);

        String plcName = selectedController.plc.filename;
        plcList.getItems().clear();
        ObservableList<String> plcNames = FXCollections.observableArrayList();
        plcNames.add(plcName);
        plcList.setItems(plcNames);

    }

    @FXML public void blockSelected(MouseEvent arg0) {
        Block blockSelected = blockList.getSelectionModel().getSelectedItem();
        blockNumber.setText(String.valueOf(blockSelected.number));
        blockLine.setText(blockSelected.line.toString());
        blockInfrastructure.setText(blockSelected.infrastructure.toString());
        blockSize.setText(String.valueOf(blockSelected.size)+"m");
        blockSpeed.setText(String.valueOf(blockSelected.speed)+"m/s");
        if(blockSelected.authority != null)
        {
            blockAuthority.setText(String.valueOf(blockSelected.authority.size()) + "blocks");
        }
        else
        {
            blockAuthority.setText("0 blocks");
        }
        blockLights.setText(blockSelected.lightGreen ? "Green" : "Red");
        blockHeater.setText(blockSelected.heaterOn ? "On" : "Off");
        blockRailBroken.setText(blockSelected.railBroken ? "Yes" : "No");
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

    @FXML public void switchOccupancy(MouseEvent arg0) {
        Block blockSelected = blockList.getSelectionModel().getSelectedItem();
        blockSelected.trainPresent = !blockSelected.trainPresent;
        blockOccupancy.setText(blockSelected.trainPresent ? "Train" : "Free");

        List<Block> testAuthority = new ArrayList<>();
        testAuthority.add(blockSelected.rightNeighbor);

        Main.trackControllers.get(0).evaluateBlocks();
    }
}
