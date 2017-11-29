package TrainController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TrainControllerManager {

    ArrayList<TrainController> trains;
    ArrayList<String>train_names;
    LaunchGUI launch = new LaunchGUI();

    public TrainControllerManager() throws Exception{

        launch.start(new Stage(), trains, train_names);
    }

    public void addTrainController(TrainController train){
        trains.add(train);
        train_names.add(train.name);
        launch.update(trains, train_names);
    }

    public void deleteTrainController(TrainController train){
        int index = train.ID;
        trains.remove(index);
        train_names.remove(index);
        launch.update(trains, train_names);
    }



}
