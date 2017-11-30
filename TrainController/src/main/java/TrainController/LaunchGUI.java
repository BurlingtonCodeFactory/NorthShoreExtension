package TrainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.application.Application.launch;

public class LaunchGUI extends Application {
    private ArrayList<TrainController> trainControllers;
    private ArrayList<String> trainNames;

    public LaunchGUI(ArrayList<TrainController> trainControllers, ArrayList<String> trainNames) {
        this.trainControllers = trainControllers;
        this.trainNames = trainNames;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*FXMLLoader loader = new FXMLLoader(new File("./build/resources/main/fxml/sample.fxml").toURI().toURL());
        Parent root = loader.load();
        GUIController controller = new GUIController(trainControllers, trainNames);
        loader.setController(controller);
        primaryStage.setTitle("TrainController");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();*/

        FXMLLoader loader = new FXMLLoader(new File("./build/resources/main/fxml/sample.fxml").toURI().toURL());
        loader.setController(new GUIController(trainControllers, trainNames));
        AnchorPane page = loader.load();
        Scene scene = new Scene(page, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TrainController");
        primaryStage.show();
    }

    public void update(ArrayList<TrainController> newTrains, ArrayList<String> newTrain_names){
        this.trainNames=newTrain_names;
        this.trainControllers=newTrains;
    }
}
