package TrackController;

import TrackController.Models.BlockProperties;
import TrackController.Models.TrackController;
import TrackModel.Models.*;
import TrackModel.TrackModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class TestBenchController {

    ObservableList<Block> blocksObservableList = FXCollections.observableArrayList();

    @FXML
    ListView<Block> blockListView;

    @FXML
    Label blockID;

    @FXML
    Label blockLine;

    @FXML
    Label blockType;

    @FXML
    Label blockUnderground;

    @FXML
    Label blockLight;

    @FXML
    Label blockLength;

    @FXML
    Label blockHeater;

    @FXML
    Label blockDirection;

    @FXML
    Label blockRailBroken;

    @FXML
    Label blockCircuitFailed;

    @FXML
    Label blockPowerFailure;

    @FXML
    Label blockSpeed;

    @FXML
    Label blockAuthority;

    @FXML
    Label blockCrossing;

    @FXML
    Label blockSwitchState;

    @FXML
    Label blockSwitchZero;

    @FXML
    Label blockSwitchOne;

    @FXML
    Label blockSwitchBase;

    @FXML
    Label blockOccupied;

    @FXML
    Label blockSuggestedSpeed;

    @FXML
    Label blockSuggestedAuthority;

    @FXML
    Label blockFailure;

    @FXML
    Label blockLock;

    @FXML
    Label blockConnectedBlocks;

    @FXML
    Label blockSuggestedMaintenance;

    @FXML
    Label blockMaintenance;

    @FXML
    TextField suggestedAuthorityValue;

    @FXML
    TextField suggestedSpeedValue;




    private final TrackModel track;
    private Block block;

    public TestBenchController(TrackModel track)
    {
        this.track = track;
        this.block = null;
    }

    @FXML
    private void initialize()
    {
        List<Block> blockList = new ArrayList<>();
        if(track.getBlocks(Line.GREEN).size() > 0)
        {
            for (Block block: track.getBlocks(Line.GREEN)) {
                blockList.add(block);
            }
        }
        else
        {
            for (Block block: track.getBlocks(Line.RED)) {
                blockList.add(block);
            }
        }

        blocksObservableList.addAll(blockList);
        blockListView.setItems(blocksObservableList);
        block = blockListView.getItems().get(0);

    }

    public void blockSelected()
    {
        block = blockListView.getSelectionModel().getSelectedItem();
        refreshUI();
    }

    public void occupancyChanged()
    {
        block.setIsOccupied(!block.getIsOccupied());
        refreshUI();
    }

    public void failureChanged()
    {
        block.setFailed(!block.getFailed());
        refreshUI();
    }

    public void setSuggestedSpeed()
    {
        double value;
        if(suggestedSpeedValue.getText().trim().equals("")) {
            value = 0;
        }
        else {
            value = Double.parseDouble(suggestedSpeedValue.getText());
        }
        block.setSuggestedSpeed(value);
        suggestedSpeedValue.setText("");
        refreshUI();
    }

    public void setSuggestedAuthority()
    {
        List<Block> authority = new ArrayList<>();
        String[] blockStrings = suggestedAuthorityValue.getText().trim().split(",");
        for(String blockString : blockStrings)
        {
            if(blockString.trim().equals("")) {
                break;
            }
            authority.add(track.getBlock(block.getLine(), Integer.parseInt(blockString.trim())));
        }
        block.setSuggestedAuthority(authority);
        suggestedAuthorityValue.setText("");
        refreshUI();
    }

    public void setSuggestedMaintenance()
    {
        block.setSuggestMaintenance(!block.getSuggestMaintenance());
        refreshUI();
    }

    public void refreshUI()
    {
        blockOccupied.setText(String.valueOf(block.getIsOccupied()));
        blockSuggestedSpeed.setText(String.valueOf(block.getSuggestedSpeed()));
        blockSuggestedAuthority.setText(block.getSuggestedAuthorityString());
        blockFailure.setText(String.valueOf(block.getFailed()));
        blockID.setText(String.valueOf(block.getId()));
        blockLine.setText(block.getLine().toString());
        blockType.setText(block.getBlockType().toString());
        blockUnderground.setText(String.valueOf(block.getIsUnderground()));
        blockLight.setText(block.getLightGreen() ? "Green" : "Red");
        blockLength.setText(String.valueOf(block.getLength()));
        blockHeater.setText(block.getHeaterOn() ? "On" : "Off");
        blockDirection.setText(block.getIsBidirectional() ? "Yes" : "No");
        blockRailBroken.setText(String.valueOf(block.getRailBroken()));
        blockPowerFailure.setText(String.valueOf(block.getPowerFailed()));
        blockCircuitFailed.setText(String.valueOf(block.getCircuitFailed()));
        blockSpeed.setText(String.valueOf(block.getCommandedSpeed()));
        blockAuthority.setText(String.valueOf(block.getCommandedAuthorityString()));
        blockConnectedBlocks.setText(block.getConnectedBlocksString());
        blockLock.setText(block.hasLock() ? String.valueOf(block.getLock()) : "No Lock");
        blockSuggestedMaintenance.setText(String.valueOf(block.getSuggestMaintenance()));
        blockMaintenance.setText(String.valueOf(block.getUnderMaintenance()));


        if(block instanceof Crossing)
        {
            Crossing crossingBlock = (Crossing) block;
            blockCrossing.setText(crossingBlock.isCrossingOn() ? "On" : "Off");
        }
        else
        {
            blockCrossing.setText("N/A");
        }

        if(block instanceof Switch)
        {
            Switch switchBlock = (Switch) block;
            blockSwitchState.setText(switchBlock.getSwitchState() ? "1" : "0");
            blockSwitchZero.setText(String.valueOf(switchBlock.getSwitchZero()));
            blockSwitchOne.setText(String.valueOf(switchBlock.getSwitchOne()));
            blockSwitchBase.setText(String.valueOf(switchBlock.getSwitchBase()));
        }
        else
        {
            blockSwitchState.setText("N/A");
            blockSwitchZero.setText("N/A");
            blockSwitchOne.setText("N/A");
            blockSwitchBase.setText("N/A");
        }
    }

}
