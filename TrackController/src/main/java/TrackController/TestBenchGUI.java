//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackController;

import TrackModel.TrackModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestBenchGUI extends Application
{
    private final TrackModel track;

    public TestBenchGUI(TrackModel track)
    {
        this.track = track;
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            FXMLLoader loader;
            if (System.getProperty("user.dir").endsWith("System"))
            {
                loader = new FXMLLoader(new File("./build/resources/main/fxml/TestBench.fxml").toURI().toURL());
            }
            else
            {
                loader = new FXMLLoader(new File("./build/resources/main/fxml/TestBench.fxml").toURI().toURL());
            }
            TestBenchController controller = new TestBenchController(track);
            loader.setController(controller);
            AnchorPane page = loader.load();
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Track Controller Test Bench");
            primaryStage.show();
        }
        catch (Exception ex)
        {
            Logger.getLogger(TrackControllerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
