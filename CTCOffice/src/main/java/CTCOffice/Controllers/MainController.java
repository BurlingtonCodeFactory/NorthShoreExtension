package CTCOffice.Controllers;

import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.Stop;
import CTCOffice.Models.Train;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {
    private final ITrackModelForCTCOffice trackModel;
    private final ITrainRepository trainRepository;
    private final IRouteService routeService;

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

    @FXML
    public ListView<Stop> trainStops;

    @FXML
    public ChoiceBox<Block> trainStopBlock;

    @FXML
    public Button trainStopButton;

    @Inject
    public MainController(ITrackModelForCTCOffice trackModel, ITrainRepository trainRepository, IRouteService routeService) {
        this.trackModel = trackModel;
        this.trainRepository = trainRepository;
        this.routeService = routeService;
    }

    public void initialize() {
        multiplier.setItems(FXCollections.observableArrayList("1", "2", "5", "10", "100"));
        multiplier.getSelectionModel().selectFirst();

        List<String> lines = Stream.of(Line.values())
                .map(Line::name)
                .collect(Collectors.toList());

        ObservableList<String> observableLines = FXCollections.observableArrayList(lines);
        blockLine.setItems(observableLines);
        //blockLine.getSelectionModel().selectFirst();

        blockLine.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // TODO: This shouldn't be done each time as blocks are static after launch
                List<Block> blocks = new ArrayList<>();
                for (Block block : trackModel.getBlocks()) {
                    if (block.getLine().equals(lines.get(newValue.intValue())) || block.getLine().equals("Yard")) { // Assuming the Yard is connected to all lines
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

                /*blockOccupied.textProperty().bind(block.getIsOccupied().asString());
                blockSpeedLimit.textProperty().setValue(Integer.toString(block.getSpeedLimit()));
                blockMaintenance.textProperty().bind(block.getUnderMaintenance().asString());
                blockBeacon.setDisable(block.getStation() == null);*/
            }
        });

        trainLine.setItems(observableLines);

        trainLine.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                List<Train> trains = new ArrayList<>();
                for (Train train : trainRepository.getTrains()) {
                    if (train.getLine().equals(lines.get(newValue.intValue()))) {
                        trains.add(train);
                    }
                }
                trainIdentifier.setItems(FXCollections.observableArrayList(trains));

                trainCreate.setDisable(false);

                List<Block> blocks = new ArrayList<>();
                for (Block block : trackModel.getBlocks()) {
                    if (block.getLine().equals(lines.get(newValue.intValue())) || block.getLine().equals("Yard")) {
                        blocks.add(block);
                    }
                }

                ObservableList<Block> observableBlocks = FXCollections.observableArrayList(blocks);
                trainAuthoritySelect.setItems(observableBlocks);
                trainStopBlock.setItems(observableBlocks);
            }
        });

        trainIdentifier.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() < 0) {
                    return;
                }

                Train train = trainIdentifier.getItems().get(newValue.intValue());

                trainLocation.textProperty().bind(train.getCurrentLocation().asString());
                trainSpeed.textProperty().bind(train.getCommandedSpeed().asString());
                trainAuthorityBlocks.textProperty().bind(train.getCommandedAuthority().asString());

                if (train.getCurrentLocation().getValue() == null) {
                    trainDispatch.setDisable(false);
                    trainAuthorityButton.setDisable(true);
                }
                else {
                    trainDispatch.setDisable(true);
                    trainAuthorityButton.setDisable(false);
                }

                trainStopButton.setDisable(false);
                trainStops.setItems(train.getStops());
            }
        });

        trainStops.setCellFactory(new Callback<ListView<Stop>, ListCell<Stop>>() {
            @Override
            public ListCell<Stop> call(ListView<Stop> param) {
                return new ListCell<Stop>() {
                    @Override
                    protected void updateItem(Stop stop, boolean empty) {
                        super.updateItem(stop, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        }
                        else {
                            setText(null);

                            final Parent parent = loadItemFxml(stop, trainIdentifier.getSelectionModel().getSelectedItem());
                            setGraphic(parent);
                        }
                    }
                };
            }
        });
    }

    public void blockMaintenanceButton(ActionEvent e) {
        Block block = blockName.getValue();
        //block.setSuggestMaintenance(!block.getUnderMaintenance().getValue());
    }

    public void trainCreateButton(ActionEvent e) {
        /*int newIdentifier = trainRepository.getTrains().size() + 1;
        Train newTrain = new Train(newIdentifier, trainLine.getValue());
        newTrain.setStops(new ArrayList<>());

        trainRepository.addTrain(newTrain);

        List<Train> trains = new ArrayList<>();
        for (Train train : trainRepository.getTrains()) {
            if (train.getLine().equals(trainLine.getValue())) {
                trains.add(train);
            }
        }
        trainIdentifier.setItems(FXCollections.observableArrayList(trains));

        trainIdentifier.getSelectionModel().select(newTrain);*/
    }

    public void trainDispatchButton(ActionEvent e) {
        Train train = trainIdentifier.getValue();

        List<Block> blocks = trackModel.getBlocks();
        Block yard = null;
        for (Block block : blocks) {
            /*if (block.getStation() != null && block.getStation().equals("Yard")) {
                yard = block;
            }*/
        }

        train.setCurrentLocation(yard); // This assumes that a station named yard will always be present
        trainDispatch.setDisable(true);
        trainAuthorityButton.setDisable(false);
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

        List<Block> authority = routeService.getShortestPath(train.getPreviousLocation(), train.getCurrentLocation().getValue(), trainAuthoritySelect.getSelectionModel().getSelectedItem());

        train.setCommandedAuthority(authority);
    }

    public void trainStopAdd(ActionEvent e) {
        Block block = trainStopBlock.getSelectionModel().getSelectedItem();
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();

        /*List<Stop> stops = train.getStops();
        stops.add(new Stop(block));
        train.setStops(stops);*/
        train.addStop(new Stop(block));
    }

    private Parent loadItemFxml(Stop stop, Train train) {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../../../../resources/main/fxml/StopView.fxml"));

        try {

            StopController stopController = new StopController(stop, train);

            fxmlLoader.setController(stopController);
            fxmlLoader.load();

            return fxmlLoader.getRoot();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
