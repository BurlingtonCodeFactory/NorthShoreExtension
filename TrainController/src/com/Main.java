package com.company;

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

        int chosenTrain = 1;

        TrainController train_one = new TrainController(true, 0, 0, 1);
        train_one.set_authority(1500.00);
        train_one.calcSetpointVelocity(23.0);

        trains.add(train_one);
        train_names.add(train_one.name);

        TrainController train_two = new TrainController(true, 0, 0, 2);
        train_two.set_authority(500.0);
        train_two.calcSetpointVelocity(30.0);

        trains.add(train_two);
        train_names.add(train_two.name);

        TrainController train_three = new TrainController(true, 0, 0, 3);
        train_three.set_authority(100.0);
        train_three.calcSetpointVelocity(10.0);

        trains.add(train_three);
        train_names.add(train_three.name);

        String currentTrain = train_names.get(chosenTrain);

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