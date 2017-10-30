package CTCOffice;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Models.Block;
import CTCOffice.Models.Repository;
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

public class CTCOffice extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../../../resources/main/fxml/Main.fxml"));

        Injector injector = Guice.createInjector(new CTCModule());
        fxmlLoader.setControllerFactory(injector::getInstance);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select track layout");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        File trackLayoutFile = fileChooser.showOpenDialog(primaryStage);
        FileReader fileReader = new FileReader(trackLayoutFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        IFileService fileService = injector.getInstance(IFileService.class);
        Repository repository = injector.getInstance(Repository.class);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Block block = fileService.parseBlock(line);
            repository.addBlock(block);
        }

        fxmlLoader.load();

        Parent root = fxmlLoader.getRoot();

        primaryStage.setTitle("CTC Office");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
