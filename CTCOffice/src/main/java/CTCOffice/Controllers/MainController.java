package CTCOffice.Controllers;

import CTCOffice.Models.Block;
import CTCOffice.Models.Repository;
import CTCOffice.Models.Train;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private final Repository repository;

    @FXML
    public ChoiceBox<String> multiplier;

    @FXML
    public ChoiceBox<String> blockLine;

    @FXML
    public ChoiceBox<Block> blockName;

    @FXML
    public Label blockOccupied;

    @FXML
    public Label blockSpeedLimit;

    @FXML
    public Button blockMaintenance;

    @FXML
    public Button blockBeacon;

    @FXML
    public Button trainCreate;

    @FXML
    public Button trainDispatch;

    @FXML
    public ChoiceBox<String> trainLine;

    @FXML
    public ChoiceBox<Train> trainIdentifier;

    @FXML
    public Label trainLocation;

    @FXML
    public Label trainSpeed;

    @FXML
    public TextField trainSpeedValue;

    @FXML
    public Button trainSpeedButton;

    @FXML
    public Label trainAuthorityBlocks;

    @FXML
    public ChoiceBox<Block> trainAuthoritySelect;

    @FXML
    public Button trainAuthorityButton;

    @Inject
    public MainController(Repository repository) {
        this.repository = repository;
    }

    public void initialize() {
        multiplier.setItems(FXCollections.observableArrayList("1", "2", "5", "10", "100"));
        multiplier.getSelectionModel().selectFirst();

        List<String> lines = new ArrayList<>();
        for (Block block : repository.getBlocks()) {
            if (!lines.contains(block.getLine())) {
                lines.add(block.getLine());
            }
        }

        ObservableList<String> observableLines = FXCollections.observableArrayList(lines);
        blockLine.setItems(observableLines);
        //blockLine.getSelectionModel().selectFirst();

        blockLine.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // TODO: This shouldn't be done each time as blocks are static after launch
                List<Block> blocks = new ArrayList<>();
                for (Block block : repository.getBlocks()) {
                    if (block.getLine().equals(lines.get(newValue.intValue()))) {
                        blocks.add(block);
                    }
                }
                blockName.setItems(FXCollections.observableArrayList(blocks));
            }
        });

        blockName.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Block block = blockName.getItems().get(newValue.intValue());

                blockOccupied.textProperty().bind(block.getOccupied().asString());
                blockSpeedLimit.textProperty().setValue(Integer.toString(block.getSpeedLimit()));
                blockMaintenance.textProperty().bind(block.getIsUnderMaintenance().asString());
                blockBeacon.setDisable(block.getStation() == null);
            }
        });

        trainLine.setItems(observableLines);

        trainLine.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                List<Train> trains = new ArrayList<>();
                for (Train train : repository.getTrains()) {
                    if (train.getLine().equals(lines.get(newValue.intValue()))) {
                        trains.add(train);
                    }
                }
                trainIdentifier.setItems(FXCollections.observableArrayList(trains));

                trainCreate.setDisable(false);

                List<Block> blocks = new ArrayList<>();
                for (Block block : repository.getBlocks()) {
                    if (block.getLine().equals(lines.get(newValue.intValue()))) {
                        blocks.add(block);
                    }
                }
                trainAuthoritySelect.setItems(FXCollections.observableArrayList(blocks));
            }
        });

        trainIdentifier.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Train train = trainIdentifier.getItems().get(newValue.intValue());

                trainLocation.textProperty().bind(train.getCurrentLocation().asString());
                trainSpeed.textProperty().bind(train.getCommandedSpeed().asString());
                trainAuthorityBlocks.textProperty().bind(train.getCommandedAuthority().asString());

                if (train.getCurrentLocation().getValue() == null) {
                    trainDispatch.setDisable(false);
                }
                else {
                    trainDispatch.setDisable(true);
                }
            }
        });
    }

    public void blockMaintenanceButton(ActionEvent e) {
        Block block = blockName.getValue();
        block.setIsUnderMaintenance(!block.getIsUnderMaintenance().getValue());
    }

    public void trainCreateButton(ActionEvent e) {
        int newIdentifier = repository.getTrains().size() + 1;
        Train newTrain = new Train(newIdentifier, trainLine.getValue());

        repository.addTrain(newTrain);

        List<Train> trains = new ArrayList<>();
        for (Train train : repository.getTrains()) {
            if (train.getLine().equals(trainLine.getValue())) {
                trains.add(train);
            }
        }
        trainIdentifier.setItems(FXCollections.observableArrayList(trains));

        trainIdentifier.getSelectionModel().select(newTrain);
    }

    public void trainDispatchButton(ActionEvent e) {
        Train train = trainIdentifier.getValue();

        List<Block> blocks = repository.getBlocks();
        Block yard = null;
        for (Block block : blocks) {
            if (block.getStation() != null && block.getStation().equals("Yard")) {
                yard = block;
            }
        }

        train.setCurrentLocation(yard); // This assumes that a station named yard will always be present
        trainDispatch.setDisable(true);
    }

    public void trainSpeedSet(ActionEvent e) {
        Train train = trainIdentifier.getValue();

        try {
            train.setCommandedSpeed(Integer.parseInt(trainSpeedValue.textProperty().getValue()));
        }
        catch (NumberFormatException exception) {
            trainSpeedValue.textProperty().setValue("Integer speed only");
        }
    }

    public void trainAuthoritySet(ActionEvent e) {
        Train train = trainIdentifier.getValue();
        List<Block> blocks = new ArrayList<>();
        blocks.add(trainAuthoritySelect.getSelectionModel().getSelectedItem()); // Call to service to perform shortest path algorithm and return (sending where you are, where you were, and list of blocks available [on line])

        train.setCommandedAuthority(blocks);
    }
}
