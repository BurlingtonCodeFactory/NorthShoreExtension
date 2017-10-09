package CTCOffice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CTCOffice extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Main.fxml"));

        Injector injector = Guice.createInjector(new CTCModule());
        fxmlLoader.setControllerFactory(injector::getInstance);

        fxmlLoader.load();

        Parent root = fxmlLoader.getRoot();

        primaryStage.setTitle("Testing 1-2-3");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
