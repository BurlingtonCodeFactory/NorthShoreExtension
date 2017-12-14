//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Chris Duncan
//**************************************************
package TrainController;

import javafx.stage.Stage;

import java.util.ArrayList;

public class TrainControllerManager
{

    private ArrayList<TrainController> trainControllers;
    private ArrayList<String> trainNames;
    private LaunchGUI launch;

    public TrainControllerManager()
    {
        trainControllers = new ArrayList<TrainController>();
        trainNames = new ArrayList<>();
        launch = new LaunchGUI(trainControllers, trainNames);
    }

    public void launch()
    {
        try
        {
            launch.start(new Stage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addTrainController(TrainController train)
    {
        GUIController controller = launch.getController();
        controller.addTrainController(train);

    }

    public void deleteTrainController(TrainController train)
    {
        GUIController controller = launch.getController();
        controller.deleteTrainController(train);

    }
}