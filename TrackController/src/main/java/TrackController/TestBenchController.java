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
        for (Block block: track.getBlocks(Line.GREEN)) {
            blockList.add(block);
        }
        blocksObservableList.addAll(blockList);
        blockListView.setItems(blocksObservableList);

    }

    public void blockSelected()
    {
        block = blockListView.getSelectionModel().getSelectedItem();

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

    public void occupancyChanged()
    {
        //block.setIsOccupied(block.);
    }

}
