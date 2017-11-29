package com.company;

/*

 * To change this license header, choose License Headers in Project Properties.

 * To change this template file, choose Tools | Templates

 * and open the template in the editor.

 */



        import eu.hansolo.medusa.Gauge;
        import eu.hansolo.medusa.GaugeBuilder;
        import javafx.application.Application;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.layout.GridPane;
        import javafx.stage.Stage;

        import java.awt.*;
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
        ControllerInterface controller = loader.getController();
        primaryStage.setTitle("TrainController");
        primaryStage.setScene(new Scene(root, 1200, 800));

        controller.addGauges();

        ArrayList<Train> trains = new ArrayList<Train>(0);



        ArrayList<String> train_names = new ArrayList<String>(0);

        int chosenTrain = 1;

        Train train_one = new Train("Train One");
        train_one.set_authority(1500.00);
        train_one.set_cmd_vel(23.0);

        trains.add(train_one);
        train_names.add(train_one.name);

        Train train_two = new Train("Train Two");
        train_two.set_authority(500.0);
        train_two.set_cmd_vel(30.0);

        trains.add(train_two);
        train_names.add(train_two.name);

        Train train_three = new Train("Train Three");
        train_three.set_authority(100.0);
        train_three.set_cmd_vel(10.0);

        trains.add(train_three);
        train_names.add(train_three.name);

        String currentTrain = train_names.get(chosenTrain);

        controller.addDAta(train_names);



        primaryStage.show();

        int seconds = 1;

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                controller.checkStatus(trains, train_names);
                try {
                    controller.moveTrains(trains);
                } catch(NullPointerException e){}
            }
        }, 0, 500*seconds);
    }

    public static void main(String[] args)  {

        // TODO code application logic here





        launch(args);

    }











}