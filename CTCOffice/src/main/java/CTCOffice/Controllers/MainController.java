//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package CTCOffice.Controllers;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.Stop;
import CTCOffice.Models.Train;
import TrackModel.Events.*;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import TrackModel.Models.BlockType;
import TrackModel.Models.Line;
import TrackModel.Models.Switch;
import TrainModel.Interfaces.ITrainModelForCTCOffice;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.util.List;

public class MainController implements ClockTickUpdateListener, OccupancyChangeListener, MaintenanceChangeListener, SwitchStateChangeListener, ThroughputUpdateListener
{
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
    public Button blockSwitch;
    @FXML
    public ChoiceBox<Line> trainLine;
    @FXML
    public ChoiceBox<Train> trainIdentifier;
    @FXML
    public Label trainLocation;
    @FXML
    public Label trainDestination;
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
    @FXML
    public Label currentTime;
    @FXML
    public CheckBox mode;
    @FXML
    public Button importSchedule;
    @FXML
    public Label throughput;

    private ITrackModelForCTCOffice trackModel;
    private ITrainRepository trainRepository;
    private ITrainModelForCTCOffice trainModel;
    private IRouteService routeService;
    private IFileService fileService;

    @Inject
    public MainController(ITrackModelForCTCOffice trackModel, ITrainRepository trainRepository, ITrainModelForCTCOffice trainModel, IRouteService routeService, IFileService fileService)
    {
        this.trackModel = trackModel;
        this.trainRepository = trainRepository;
        this.trainModel = trainModel;
        this.routeService = routeService;
        this.fileService = fileService;
    }

    @FXML
    public void initialize()
    {
        // Set multiplier options
        multiplier.setItems(FXCollections.observableArrayList(0, 1, 2, 5, 10, 20, 50, 100));
        // Set handler for multiplier selection changes
        multiplier.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> trackModel.setMultiplier(newValue)
        );
        // Select initial multiplier value (1)
        multiplier.getSelectionModel().select(1);

        // Set handler for mode change
        mode.selectedProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    trainRepository.setMode(newValue);
                    routeService.RouteTrains(Line.GREEN);
                    routeService.RouteTrains(Line.RED);
                }
        );

        // Set blockLine options
        blockLine.setItems(FXCollections.observableArrayList(Line.values()));
        // Set handler for blockLine selection changes
        blockLine.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> blockName.setItems(FXCollections.observableArrayList(trackModel.getBlocks(newValue)))
        );

        // Set handler for blockName selection changes
        blockName.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    blockOccupied.setText(Boolean.toString(newValue.getIsOccupied()));
                    blockSpeedLimit.setText(String.format("%1$.2f", newValue.getSpeedLimit() * 2.23694));
                    blockMaintenance.setText(Boolean.toString(newValue.getUnderMaintenance()));
                    blockMaintenance.setDisable(false);

                    if (newValue.getBlockType() == BlockType.SWITCH)
                    {
                        blockSwitch.setDisable(false);
                        blockSwitch.setText(switchString((Switch) newValue));
                    }
                    else
                    {
                        blockSwitch.setDisable(true);
                        blockSwitch.setText("N/A");
                    }
                }
        );

        // Set onAction handler for blockMaintenance
        blockMaintenance.setOnAction(this::blockMaintenanceButton);

        // Set onAction handler for blockSwitch
        blockSwitch.setOnAction(this::blockSwitchButton);

        // Set onAction handler for importSchedule
        importSchedule.setOnAction(this::importScheduleButton);

        // Set trainLine options
        trainLine.setItems(FXCollections.observableArrayList(Line.values()));
        // Set handler for trainLine selection changes
        trainLine.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    trainIdentifier.setItems(FXCollections.observableArrayList(trainRepository.getTrains(newValue)));
                    trainStopBlock.setItems(FXCollections.observableArrayList(trackModel.getBlocks(newValue)));
                    trainAuthoritySelect.setItems(FXCollections.observableArrayList(trackModel.getBlocks(newValue)));

                    trainCreate.setDisable(false);
                }
        );

        // Set handler for trainIdentifier selection changes
        trainIdentifier.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    if (newValue != null)
                    {
                        trainLocation.textProperty().bind(newValue.getCurrentBlockProperty().asString());
                        trainDestination.textProperty().bind(newValue.getDestinationBlockProperty().asString());
                        trainSpeed.textProperty().bind(newValue.getSuggestedSpeedProperty().multiply(2.23694).asString("%.2f"));
                        trainAuthorityBlocks.textProperty().bind(newValue.getSuggestedAuthorityProperty().asString());
                        trainStops.setItems(newValue.getScheduleProperty());

                        trainStopButton.setDisable(false);

                        if (newValue.getPreviousBlock() == null)
                        {
                            if (!trackModel.getBlock(newValue.getLine(), 0).getIsOccupied())
                            {
                                trainDispatch.setDisable(false);
                            }
                            else
                            {
                                trainDispatch.setDisable(true);
                            }
                            trainAuthorityButton.setDisable(true);
                        }
                        else
                        {
                            trainDispatch.setDisable(true);
                            trainAuthorityButton.setDisable(false);
                        }
                    }
                }
        );

        // Set cell factory for trainStops
        trainStops.setCellFactory(new Callback<ListView<Stop>, ListCell<Stop>>()
        {
            @Override
            public ListCell<Stop> call(ListView<Stop> param)
            {
                return new ListCell<Stop>()
                {
                    @Override
                    protected void updateItem(Stop stop, boolean empty)
                    {
                        super.updateItem(stop, empty);
                        if (empty)
                        {
                            setText(null);
                            setGraphic(null);
                        }
                        else
                        {
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

    public void blockMaintenanceButton(ActionEvent e)
    {
        Block block = blockName.getSelectionModel().selectedItemProperty().getValue();

        if (block == null)
        {
            return;
        }

        block.setSuggestMaintenance(!block.getUnderMaintenance());
    }

    public void blockSwitchButton(ActionEvent e)
    {
        Block block = blockName.getSelectionModel().getSelectedItem();

        if (block == null || block.getBlockType() != BlockType.SWITCH)
        {
            return;
        }

        ((Switch) block).setSwitchStateManual(!((Switch) block).getSwitchState());
        blockSwitch.setText(switchString((Switch) block));
    }

    public void trainCreateButton(ActionEvent e)
    {
        Line currentLine = trainLine.getSelectionModel().getSelectedItem();
        int newIdentifier = trainRepository.getTrains(currentLine).size() + 1;
        Train train = new Train(newIdentifier, currentLine, null, trackModel.getBlock(currentLine, 0));

        trainRepository.addTrain(train);

        trainIdentifier.setItems(FXCollections.observableArrayList(trainRepository.getTrains(currentLine)));
        trainIdentifier.getSelectionModel().select(train);
    }

    public void trainDispatchButton(ActionEvent e)
    {
        Line line = trainLine.getSelectionModel().getSelectedItem();
        if (trackModel.getBlock(line, 0).getIsOccupied())
        {
            return;
        }

        Train train = trainIdentifier.getSelectionModel().getSelectedItem();
        train.setPreviousBlock(trackModel.getBlock(train.getLine(), 0));

        trainModel.createTrain(-1, 0, 2, true, train.getLine());
        train.setSuggestedSpeed(train.getSuggestedSpeed());

        trainDispatch.setDisable(true);
        trainAuthorityButton.setDisable(false);
    }

    public void trainSpeedSet(ActionEvent e)
    {
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();

        try
        {
            double speed = Double.parseDouble(trainSpeedValue.textProperty().getValue()) * 0.44704;
            double speedLimit = train.getCurrentBlock().getSpeedLimit();
            if (speed > speedLimit)
            {
                train.setSuggestedSpeed(speedLimit);

            }
            else if (speed < 0)
            {
                train.setSuggestedSpeed(0);
            }
            else
            {
                train.setSuggestedSpeed(speed);
            }
        }
        catch (NumberFormatException exception)
        {
            trainSpeedValue.textProperty().setValue("Double speed only");
        }
    }

    public void trainAuthoritySet(ActionEvent e)
    {
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();

        train.setDestinationBlock(trainAuthoritySelect.getSelectionModel().getSelectedItem());
        List<Block> authority = routeService.getShortestPath(train.getPreviousBlock(), train.getCurrentBlock(), train.getDestinationBlock());

        if (authority != null)
        {
            train.setSuggestedAuthority(authority);
        }
    }

    public void trainStopAdd(ActionEvent e)
    {
        Block block = trainStopBlock.getSelectionModel().getSelectedItem();
        Train train = trainIdentifier.getSelectionModel().getSelectedItem();

        train.addStop(new Stop(block));

        routeService.RouteTrains(train.getLine());
    }

    public void importScheduleButton(ActionEvent e)
    {
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select train schedule to import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        File trackLayoutFile = fileChooser.showOpenDialog(fileStage);
        FileReader fileReader = null;
        try
        {
            fileReader = new FileReader(trackLayoutFile);
        }
        catch (FileNotFoundException e1)
        {
            System.out.println("Could not read file for train schedule import.");
            return;
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        fileService.parseTrainSchedule(bufferedReader);

        Line currentLine = trainLine.getSelectionModel().getSelectedItem();
        trainIdentifier.setItems(FXCollections.observableArrayList(trainRepository.getTrains(currentLine)));
        //trainIdentifier.getSelectionModel().select(train);

        routeService.RouteTrains(Line.GREEN);
        routeService.RouteTrains(Line.RED);
    }

    @Override
    public void clockTickUpdateReceived(ClockTickUpdateEvent event)
    {
        currentTime.setText(Double.toString(((double) event.getSource()) / 1000));
    }

    @Override
    public void occupancyChangeReceived(OccupancyChangeEvent event)
    {
        Block changedBlock = (Block) event.getSource();
        Block selectedBlock = blockName.getSelectionModel().getSelectedItem();

        if (selectedBlock != null && changedBlock.getId() == selectedBlock.getId() && changedBlock.getLine() == selectedBlock.getLine())
        {
            blockOccupied.setText(Boolean.toString(changedBlock.getIsOccupied()));
        }

        Train selectedTrain = trainIdentifier.getSelectionModel().getSelectedItem();

        if (selectedTrain != null && selectedTrain.getPreviousBlock() == null && !trackModel.getBlock(selectedTrain.getLine(), 0).getIsOccupied())
        {
            trainDispatch.setDisable(false);
        }
        else
        {
            trainDispatch.setDisable(true);
        }
    }

    @Override
    public void maintenanceChangeReceived(MaintenanceChangeEvent event)
    {
        Block changedBlock = (Block) event.getSource();
        Block selectedBlock = blockName.getSelectionModel().getSelectedItem();

        if (selectedBlock != null && changedBlock.getId() == selectedBlock.getId() && changedBlock.getLine() == selectedBlock.getLine())
        {
            blockMaintenance.setText(Boolean.toString(changedBlock.getUnderMaintenance()));
        }
    }

    @Override
    public void switchStateChangeReceived(SwitchStateChangeEvent event)
    {
        Switch changedSwitch = (Switch) event.getSource();
        Block selectedBlock = blockName.getSelectionModel().getSelectedItem();

        if (selectedBlock != null && changedSwitch.getId() == selectedBlock.getId() && changedSwitch.getLine() == selectedBlock.getLine())
        {
            System.out.println("Setting output for switch " + changedSwitch.getId());
            blockSwitch.setText(switchString(changedSwitch));
        }
    }

    @Override
    public void throughputUpdateReceived(ThroughputUpdateEvent event)
    {
        System.out.println(trackModel.getPassengersDisembarked() + " / " + trackModel.getTime() / 1000 / 60 / 60 + " = " + trackModel.getPassengersDisembarked() / (trackModel.getTime() / 1000 / 60 / 60));
        throughput.setText(String.format("%.2f", trackModel.getPassengersDisembarked() / (trackModel.getTime() / 1000 / 60 / 60)));
    }

    private Parent loadItemFxml(Stop stop, Train train)
    {
        FXMLLoader fxmlLoader = null;
        try
        {
            fxmlLoader = new FXMLLoader(new File("./build/resources/main/fxml/StopView.fxml").toURI().toURL());

            StopController stopController = new StopController(stop, train);
            fxmlLoader.setController(stopController);

            fxmlLoader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return fxmlLoader.getRoot();
    }

    private String switchString(Switch block)
    {
        return block.getSwitchBase() + " <-> " + (block.getSwitchState() ? block.getSwitchOne() : block.getSwitchZero());
    }
}
