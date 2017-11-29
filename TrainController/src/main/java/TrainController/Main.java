package TrainController;

/*

 * To change this license header, choose License Headers in Project Properties.

 * To change this template file, choose Tools | Templates

 * and open the template in the editor.

 */



        import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.stage.Stage;

        import java.util.ArrayList;
        import java.util.Timer;
        import java.util.TimerTask;


/**

 *

 * @author ecelabs

 */

public class Main extends Application{







    /**



     */

    public void start(Stage primaryStage) throws Exception{


        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        ControllerManager controller = loader.getController();
        primaryStage.setTitle("TrainController");
        primaryStage.setScene(new Scene(root, 1200, 800));

        controller.addGauges();

        ArrayList<TrainController> trains = new ArrayList<TrainController>(0);

        ArrayList<String> train_names = new ArrayList<String>(0);

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

    public static void main(String[] args)  {

        // TODO code application logic here





        launch(args);

    }











}