package System;

import CTCOffice.CTCEventHandler;
import CTCOffice.CTCModule;
import TrackController.TrackControllerManager;
import TrackController.TrackControllerModule;
import TrackController.TrackControllerTestBench;
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
import java.util.concurrent.TimeUnit;

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

        // Instantiate TrainModel
        TrainModel trainModel = injector.getInstance(TrainModel.class);

        Block.addOccupancyChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addSuggestedSpeedChangeListener(injector.getInstance(TrackControllerManager.class));
        Block.addSuggestedAuthorityChangeListener(injector.getInstance(TrackControllerManager.class));

        Block.addOccupancyChangeListener(injector.getInstance(CTCEventHandler.class));

        //Instantiate Test Bench
        TrackControllerTestBench testBench = new TrackControllerTestBench(injector);
        testBench.launch();

        trackModel.setMultiplier(30);

        Thread thread = new Thread(() -> {
            while(true)
            {
                if(trackModel.getMultiplier() != 0)
                {
                    trackModel.addInterval(500);
                    trainModel.updateTrains(500);
                    try {
                        Thread.sleep((long)(1000 / trackModel.getMultiplier()));                 //1000 milliseconds is one second.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    try {
                        Thread.sleep((long)(1000));                 //1000 milliseconds is one second.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();

    }
}
