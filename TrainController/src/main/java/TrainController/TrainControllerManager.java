package TrainController;

import javafx.stage.Stage;

import java.util.ArrayList;

public class TrainControllerManager {

    private ArrayList<TrainController> trainControllers;
    private ArrayList<String> trainNames;
    private LaunchGUI launch;

    public TrainControllerManager() {
        trainControllers = new ArrayList<TrainController>();
        trainNames = new ArrayList<>();
        launch = new LaunchGUI(trainControllers, trainNames);
    }

    public void launch() {
        try {
            launch.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTrainController(TrainController train){
        trainControllers.add(train);
        trainNames.add(train.name);
        launch.update(trainControllers, trainNames);
    }

    public void deleteTrainController(TrainController train){
        int index = train.ID;
        trainControllers.remove(index);
        trainNames.remove(index);
        launch.update(trainControllers, trainNames);
    }
}