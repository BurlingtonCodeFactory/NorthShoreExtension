package TrainModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/main/fxml/StartupUI.fxml"));
        primaryStage.setTitle("Train Model Startup");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {

                launch(args);


//        Train train = new Train(1, 100000.00, 0.00);


//        System.out.println("Acceleration: " + train.getAccelerationString());
//        System.out.println("Velocity: " + train.getVelocityString() + " | " + train.deltaTmillis);
//
//        new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
//            @Override
//                    public void run() {
//
//                        train.update();
//                        System.out.println("Acceleration: " + train.getAccelerationString());
//                        System.out.println("Velocity: " + train.getVelocityString() + " | " + train.deltaTmillis);
//
//
//                    }
//                }, 100,100);


    }
}