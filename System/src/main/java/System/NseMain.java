package System;

import CTCOffice.CTCModule;
import TrackController.TrackControllerManager;
import TrackController.TrackControllerModule;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import TrackModel.Services.FileService;
import TrackModel.TrackModel;
import TrainModel.TrainModel;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class NseMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Hello System!");


        Stage fileStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select track layout");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        File trackLayoutFile = fileChooser.showOpenDialog(fileStage);
        FileReader fileReader = new FileReader(trackLayoutFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);


        // Set up DI
        Injector injector = Guice.createInjector(new DIModule());

        FileService fileService = new FileService();
        List<Block> blocks = fileService.parseTrackLayout(bufferedReader);

        TrackModel trackModel = injector.getInstance(TrackModel.class);


        for (Block block : blocks) {
            trackModel.addBlock(block);
        }

        // Instantiate Track Controller
        TrackControllerModule trackController = new TrackControllerModule(injector);
        trackController.launch();

        // Instantiate CTC
        CTCModule ctc = new CTCModule(injector);
        ctc.launch();

        Block.addOccupancyChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addSuggestedSpeedChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addSuggestedAuthorityChangeListener(injector.getInstance(TrackControllerManager.class));


        trackModel.getBlock(Line.GREEN, 1).setSuggestedSpeed(20);
        trackModel.getBlock(Line.GREEN, 1).setSuggestedAuthority(new ArrayList<>());

    }
}
