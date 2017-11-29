package TrainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.application.Application.launch;

public class LaunchGUI {
    ArrayList<TrainController> trains;
    ArrayList<String> trainNames;

    public void start(Stage primaryStage, ArrayList<TrainController> trains, ArrayList<String> train_names) throws Exception{

        this.trains = trains;
        this.trainNames = train_names;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        GUIController controller = loader.getController();
        primaryStage.setTitle("TrainController");
        primaryStage.setScene(new Scene(root, 1200, 800));

        controller.addGauges();



        controller.addDAta(train_names);

        controller.init(trains);


        primaryStage.show();

        int seconds = 1;

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    controller.checkStatus(trains, train_names);
                } catch(NullPointerException e){}
            }
        }, 0, 500*seconds);
    }

    public void update(ArrayList<TrainController> newTrains, ArrayList<String> newTrain_names){
        this.trainNames=newTrain_names;
        this.trains=newTrains;
    }
}
