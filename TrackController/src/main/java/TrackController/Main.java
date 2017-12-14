package TrackController;

import TrackModel.Models.Block;
import TrackModel.Services.FileService;
import TrackModel.TrackModel;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class Main extends Application
{
    public static void main(String[] args) throws Exception
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select track layout");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        File trackLayoutFile = fileChooser.showOpenDialog(fileStage);
        FileReader fileReader = new FileReader(trackLayoutFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);


        // Set up DI
        Injector injector = Guice.createInjector(new TrackControllerDIModule());

        FileService fileService = new FileService();
        List<Block> blocks = fileService.parseTrackLayout(bufferedReader);

        TrackModel trackModel = injector.getInstance(TrackModel.class);

        for (Block block : blocks)
        {
            trackModel.addBlock(block);
        }

        // Instantiate Track Controller
        TrackControllerModule trackController = new TrackControllerModule(injector);
        trackController.launch();

        //Instantiate Test Bench
        TrackControllerTestBench testBench = new TrackControllerTestBench(injector);
        testBench.launch();


        Block.addOccupancyChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addSuggestedSpeedChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addSuggestedAuthorityChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addFailureChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addSwitchStateManualChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addMaintenanceRequestListener(injector.getInstance(TrackControllerManager.class));

    }
}
