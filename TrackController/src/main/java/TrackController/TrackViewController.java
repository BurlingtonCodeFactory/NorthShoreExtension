package TrackController;

import TrackController.Events.RefreshUIEvent;
import TrackController.Events.RefreshUIListener;
import TrackModel.*;
import TrackModel.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import TrackController.Models.*;
import TrackController.PLC.PLC;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.midi.Track;

public class TrackViewController implements RefreshUIListener{

    @FXML
    ListView<TrackController> controllerList;

    @FXML
    TableView<Block> blockList;

    @FXML
    ListView<String> plcList;

    @FXML
    TextArea plcContents;

    ObservableList<TrackController> controllersView = FXCollections.observableArrayList();
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
    Label blockMaintenance;

    @FXML
    TableColumn lineColumn;

    @FXML
    TableColumn numberColumn;

    @FXML
    TableColumn infrastructureColumn;

    private final TrackControllerManager manager;

    private Block blockSelected;

    private TrackController selectedController;

    public TrackViewController(TrackControllerManager manager)
    {
        this.manager = manager;
    }

    @FXML
    private void initialize()
    {
        System.out.println("Here in TRack Controller init");
        List<TrackController> controllersList = new ArrayList<>();
        for (TrackController controller: manager.controllers) {
            controllersList.add(controller);
        }
        controllersView.addAll(controllersList);
        controllerList.setItems(controllersView);
        selectedController = manager.controllers.get(0);

        lineColumn.setCellValueFactory(new PropertyValueFactory<Block, Line>("line"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Block,Integer>("id"));
        infrastructureColumn.setCellValueFactory(new PropertyValueFactory<Block, BlockType>("BlockType"));

        controllerSelected(null);

    }

    @FXML public void controllerSelected(MouseEvent arg0)
    {
        selectedController = controllerList.getSelectionModel().getSelectedItem();

        if(selectedController == null)
        {
            selectedController = manager.controllers.get(0);
        }

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
        catch(Exception e)
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
        if(blockSelected == null)
        {
            blockSelected = blockList.getItems().get(0);
        }
        blockNumber.setText(String.valueOf(blockSelected.getId()));
        blockLine.setText(blockSelected.getLine().toString());
        blockInfrastructure.setText(blockSelected.getBlockType().toString());
        blockSize.setText(String.valueOf(blockSelected.getLength())+" feet");
        blockSpeed.setText(String.valueOf(blockSelected.getCommandedSpeed()*2.23694)+"mph");
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
        blockRailBroken.setText(blockSelected.getRailBroken() ? "Yes" : "No");  //TODO: change back to railBroken
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

        blockOccupancy.setText(String.valueOf(blockSelected.getUnderMaintenance()));

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

    @FXML
    public void addPLC(MouseEvent arg0)
    {
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PLC file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PLC files (*.plc)", "*.plc"));

        File plcFile = fileChooser.showOpenDialog(fileStage);
        if(plcFile == null)
        {
            return;
        }
        File destSystem = new File("./build/resources/main/"+plcFile.getName());
        File destTC = new File("./TrackController/build/resources/main/"+plcFile.getName());

        try
        {
            if(!destSystem.exists())
            {
                Files.copy(plcFile.toPath(), destSystem.toPath());
                Files.copy(plcFile.toPath(), destTC.toPath());
            }
            List<String> plcFileNames = selectedController.plcFileNames;
            plcFileNames.add(plcFile.getName());
            plcList.getItems().clear();
            ObservableList<String> plcNames = FXCollections.observableArrayList();
            plcNames.addAll(plcFileNames);
            plcList.setItems(plcNames);
        }
        catch(IOException e)
        {
            System.out.println("File not found");
        }
    }

    @FXML public void plcActivated(MouseEvent arg0)
    {

        String plcName = plcList.getSelectionModel().getSelectedItem();
        selectedController.plc = new PLC(plcName, manager.getTrack());
        selectedController.evaluateBlocks();
    }

    @FXML public void flipSwitch(MouseEvent arg0)
    {
        if(blockSelected instanceof Switch)
        {
            System.out.println("Flipping switch "+blockSelected.getId());
            Switch switchBlock = (Switch) blockSelected;
            switchBlock.setSwitchStateManual(!switchBlock.getSwitchState());
        }

        refreshUI();
    }


    @Override
    public void refreshUIReceived(RefreshUIEvent event) {
        refreshUI();
    }
}
