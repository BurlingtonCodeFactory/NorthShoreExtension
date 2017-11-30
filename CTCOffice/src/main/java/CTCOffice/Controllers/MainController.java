package CTCOffice.Controllers;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.Stop;
import CTCOffice.Models.Train;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import TrainModel.Interfaces.ITrainModelForCTCOffice;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    public ChoiceBox<Integer> multiplier;
    @FXML
    public ChoiceBox<Line> blockLine;
    @FXML
    public ChoiceBox<Block> blockName;
    @FXML
    public Label blockOccupied;
    @FXML
    public Label blockSpeedLimit;
    @FXML
    public Button blockMaintenance;
    @FXML
    public ChoiceBox<Line> trainLine;
    @FXML
    public ChoiceBox<Train> trainIdentifier;
    @FXML
    public Label trainLocation;
    @FXML
    public Label trainSpeed;
    @FXML
    public Label trainAuthorityBlocks;
    @FXML
    public ListView<Stop> trainStops;
    @FXML
    public ChoiceBox<Block> trainAuthoritySelect;
    @FXML
    public ChoiceBox<Block> trainStopBlock;
    @FXML
    public Button trainStopButton;
    @FXML
    public Button trainSpeedButton;
    @FXML
    public TextField trainSpeedValue;
    @FXML
    public Button trainDispatch;
    @FXML
    public Button trainAuthorityButton;
    @FXML
    public Button trainCreate;

    private ITrackModelForCTCOffice trackModel;
    private ITrainRepository trainRepository;
    private ITrainModelForCTCOffice trainModel;
    private IRouteService routeService;
    private IFileService fileService;

    @Inject
    public MainController(ITrackModelForCTCOffice trackModel, ITrainRepository trainRepository, ITrainModelForCTCOffice trainModel, IRouteService routeService, IFileService fileService) {
        this.trackModel = trackModel;
        this.trainRepository = trainRepository;
        this.trainModel = trainModel;
        this.routeService = routeService;
        this.fileService = fileService;
    }

    @FXML
    public void initialize() {
        // Set multiplier options
        multiplier.setItems(FXCollections.observableArrayList(0, 1, 2, 5, 10, 20, 50, 100));
        // Set handler for multiplier selection changes
        multiplier.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> trackModel.setMultiplier(newValue)
        );
        // Select initial multiplier value (1)
        multiplier.getSelectionModel().select(1);

        // Set blockLine options
        blockLine.setItems(FXCollections.observableArrayList(Line.values()));
        // Set handler for blockLine selection changes
        blockLine.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> blockName.setItems(FXCollections.observableArrayList(trackModel.getBlocks(newValue)))
        );

        // Set handler for blockName selection changes
        blockName.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // TODO: this is not an observable! need to listen to UI update event from Occupancy changed handler (and others -> maintenance)
                    blockOccupied.setText(Boolean.toString(newValue.getIsOccupied()));
                    blockSpeedLimit.setText(Double.toString(newValue.getSpeedLimit()));
                    blockMaintenance.setText(Boolean.toString(newValue.getUnderMaintenance()));
                    blockMaintenance.setDisable(false);
                }
        );

        // Set onAction handler for blockMaintenance
        blockMaintenance.setOnAction(this::blockMaintenanceButton);

        // Set trainLine options
        trainLine.setItems(FXCollections.observableArrayList(Line.values()));
        // Set handler for trainLine selection changes
        trainLine.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    trainIdentifier.setItems(FXCollections.observableArrayList(trainRepository.getTrains(newValue)));
                    trainStopBlock.setItems(FXCollections.observableArrayList(trackModel.getBlocks(newValue)));
                    trainAuthoritySelect.setItems(FXCollections.observableArrayList(trackModel.getBlocks(newValue)));

                    trainCreate.setDisable(false);
                }
        );

        // Set handler for trainIdentifier selection changes
        trainIdentifier.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    trainLocation.textProperty().bind(newValue.getCurrentBlockProperty().asString());
                    trainSpeed.textProperty().bind(newValue.getSuggestedSpeedProperty().asString());
                    trainAuthorityBlocks.textProperty().bind(newValue.getSuggestedAuthorityProperty().asString());
                    trainStops.setItems(newValue.getScheduleProperty());

                    trainStopButton.setDisable(false);

                    if (newValue.getPreviousBlock() == null) {
                        trainDispatch.setDisable(false);
                        trainAuthorityButton.setDisable(true);
                    }
                    else {
                        trainDispatch.setDisable(true);
                        trainAuthorityButton.setDisable(false);
                    }
                }
        );

        // Set cell factory for trainStops
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

        trainStopButton.setOnAction(this::trainStopAdd);
        trainSpeedButton.setOnAction(this::trainSpeedSet);
        trainAuthorityButton.setOnAction(this::trainAuthoritySet);
        trainDispatch.setOnAction(this::trainDispatchButton);
        trainCreate.setOnAction(this::trainCreateButton);
    }

    public void blockMaintenanceButton(ActionEvent e) {
        Block block = blockName.getSelectionModel().selectedItemProperty().getValue();

        if (block == null) {
            return;
        }

        block.setSuggestMaintenance(!block.getUnderMaintenance()); // TODO: need UnderMaintenanceChangeEvent, when TC changes value it fires, pick that up and refresh blockMaintenance text
    }

    public void trainCreateButton(ActionEvent e) {
        Line currentLine = trainLine.getSelectionModel().getSelectedItem();
        int newIdentifier = trainRepository.getTrains(currentLine).size() + 1;
        Train train = new Train(newIdentifier, currentLine, null, trackModel.getBlock(currentLine, 0));

        trainRepository.addTrain(train);

        trainIdentifier.setItems(FXCollections.observableArrayList(trainRepository.getTrains(currentLine)));
        trainIdentifier.getSelectionModel().select(train);
    }

    public void trainDispatchButton(ActionEvent e) {
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();
        train.setPreviousBlock(trackModel.getBlock(train.getLine(), 0));

        trainModel.createTrain(-1, 0, 2, true, train.getLine());
        train.setSuggestedSpeed(train.getSuggestedSpeed());

        trainDispatch.setDisable(true);
        trainAuthorityButton.setDisable(false);
    }

    public void trainSpeedSet(ActionEvent e) {
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();

        try {
            double speed = Double.parseDouble(trainSpeedValue.textProperty().getValue());
            System.out.println(speed);
            double speedLimit = train.getCurrentBlock().getSpeedLimit();
            System.out.println(speedLimit);
            System.out.println("1");
            if (speed > speedLimit) {
                train.setSuggestedSpeed(speedLimit);

            }
            else if (speed < 0) {
                train.setSuggestedSpeed(0);
            }
            else {
                train.setSuggestedSpeed(speed); // TODO: set suggestedSpeed on trackModel.block corresponding to train location, etc
            }
        }
        catch (NumberFormatException exception) {
            trainSpeedValue.textProperty().setValue("Double speed only");
        }
    }

    public void trainAuthoritySet(ActionEvent e) {
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();

        List<Block> authority = routeService.getShortestPath(train.getPreviousBlock(), train.getCurrentBlock(), trainAuthoritySelect.getSelectionModel().getSelectedItem());

        if (authority != null) {
            train.setSuggestedAuthority(authority); // TODO: set suggestedAuthority on trackModel.block corresponding to train location, etc
        }
    }

    public void trainStopAdd(ActionEvent e) {
        Block block = trainStopBlock.getSelectionModel().getSelectedItem();
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();

        train.addStop(new Stop(block));
    }

    private Parent loadItemFxml(Stop stop, Train train) {
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(new File("./build/resources/main/fxml/StopView.fxml").toURI().toURL());

            StopController stopController = new StopController(stop, train);
            fxmlLoader.setController(stopController);

            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return fxmlLoader.getRoot();
    }
}
