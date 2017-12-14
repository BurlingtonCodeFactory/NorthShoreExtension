//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Chris Duncan
//**************************************************
package TrainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class LaunchGUI extends Application
{
    private ArrayList<TrainController> trainControllers;
    private ArrayList<String> trainNames;
    private GUIController controller;

    public LaunchGUI(ArrayList<TrainController> trainControllers, ArrayList<String> trainNames)
    {
        this.trainControllers = trainControllers;
        this.trainNames = trainNames;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {


        FXMLLoader loader = new FXMLLoader(new File("./build/resources/main/fxml/TrainControllerUI.fxml").toURI().toURL());
        controller = new GUIController(trainControllers, trainNames);
        loader.setController(controller);
        AnchorPane page = loader.load();
        Scene scene = new Scene(page, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TrainController");
        primaryStage.show();
    }

    public GUIController getController()
    {
        return controller;
    }
}
