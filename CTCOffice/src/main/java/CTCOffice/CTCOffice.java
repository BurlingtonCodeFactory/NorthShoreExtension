package CTCOffice;

import CTCOffice.Interfaces.IFileService;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.TrackModel;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CTCOffice extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader = null;
        try
        {
            fxmlLoader = new FXMLLoader(new File("./build/resources/main/fxml/Main.fxml").toURI().toURL());
        }
        catch(Exception e)
        {

        }

        // Injector injector = Guice.createInjector(new CTCModule());
        fxmlLoader.setControllerFactory(CTCModule.injector::getInstance);

        try {
            fxmlLoader.load();
        }
        catch (IOException e)
        {

        }

        Parent root = fxmlLoader.getRoot();

        primaryStage.setTitle("CTC Office");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
