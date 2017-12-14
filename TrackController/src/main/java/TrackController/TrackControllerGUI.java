//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrackControllerGUI extends Application
{

    private final TrackControllerManager manager;

    public TrackControllerGUI(TrackControllerManager manager)
    {
        this.manager = manager;
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            FXMLLoader loader;
            if (System.getProperty("user.dir").endsWith("System"))
            {
                loader = new FXMLLoader(new File("./build/resources/main/fxml/TrackController.fxml").toURI().toURL());
            }
            else
            {
                loader = new FXMLLoader(new File("./build/resources/main/fxml/TrackController.fxml").toURI().toURL());
            }
            TrackViewController controller = new TrackViewController(manager);
            manager.addRefreshUIListener(controller);
            loader.setController(controller);
            AnchorPane page = loader.load();
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Track Controller");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Logger.getLogger(TrackControllerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

