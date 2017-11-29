package CTCOffice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class CTCOffice extends Application {
    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader;
        try
        {
            fxmlLoader = new FXMLLoader(new File("./build/resources/main/fxml/Main.fxml").toURI().toURL());
        }
        catch(MalformedURLException e)
        {
            System.out.println("CTCOffice - .fxml malformed URL.");
            return;
        }

        fxmlLoader.setControllerFactory(CTCModule.injector::getInstance);

        try {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            System.out.println("Could not load CTCOffice fxml.");
            e.printStackTrace();
            return;
        }

        Parent root = fxmlLoader.getRoot();

        primaryStage.setTitle("CTC Office");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
