//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Evan Ozaroff
//**************************************************
package TrainModel;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;

public class TrainGUI extends Application
{

    private Train train;

    public TrainGUI(Train train)
    {
        this.train = train;
    }

    @Override
    public void start(Stage primaryStage)
    {
        //Open and Initialize Train UI
        try
        {
            /*
            //Open UI
            FXMLLoader fxmlLoader = new FXMLLoader(new File("./build/resources/main/fxml/TrainUI.fxml").toURI().toURL());
            fxmlLoader.load();
            Parent root1 = fxmlLoader.getRoot();
            fxmlLoader.setController(new TrainViewController(train));
            Stage stage = new Stage();
            stage.setTitle("Train " + train.getID());
            stage.setScene(new Scene(root1));
            stage.show();
            */

            FXMLLoader loader = new FXMLLoader(new File("./build/resources/main/fxml/TrainUI.fxml").toURI().toURL());
            loader.setController(new TrainViewController(train));
            AnchorPane page = loader.load();
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Train " + train.getID());
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
