package TrainModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.*;

//public class StartupController implements Initializable
public class StartupController extends Application
{

    public TextField CARS, POWER, GRADE;

    @Override
//    public void initialize(URL location, ResourceBundle resources)
//    {
//
//    }
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("TrainModelUI.fxml"));
        primaryStage.setTitle("Train Model");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    @FXML
    public void startButton()
    {
//        String s = CARS.getText();
//        try(Integer.parseInt(s))
//        {
//
//        }
//        catch(NumberFormatException e)
//        {
//
//        }

        int cars = Integer.parseInt(CARS.getText());
        double power = Double.parseDouble(POWER.getText());
        double grade = Double.parseDouble(GRADE.getText());

        Train train = new Train(cars, power, grade);

        //Open Train Model UI
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TrainModelUI.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Train Model");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch(Exception e)
        {
        }


    }
}
