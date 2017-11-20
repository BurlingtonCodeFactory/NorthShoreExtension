package TrainModel;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.*;


public class TrainModel extends Application {

    //Initialize array of trains
    private ArrayList<Train> trains;

    //Initialize ID
    private int ID;

    //Initialize JavaFX UI Fields
    @FXML
    public TextField POWER;
    public Circle BRAKE_INDICATOR;
    public Pane SPEED_GAUGE_PANE;
    public Pane ACCELERATION_GAUGE_PANE;
    private Train train = null;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void init(Train trn)
    {
        train = trn;
        POWER.setText(Double.toString(train.getPower() / 1000)); //Divide by 1000 here to display as kW

        Gauge speedGauge = GaugeBuilder.create()
                .title("Speed Gauge")
                .subTitle("")
                .unit("mph")
                .build();


        SPEED_GAUGE_PANE.getChildren().add(speedGauge);

        Gauge accelerationGauge = GaugeBuilder.create()
                .title("Acceleration Gauge")
                .subTitle("")
                .unit("mph/s")
                .build();
        accelerationGauge.setMinValue(-15.0);
        accelerationGauge.setMaxValue(15.0);

        ACCELERATION_GAUGE_PANE.getChildren().add(accelerationGauge);

        java.util.TimerTask tt = new java.util.TimerTask() {
            @Override
            public void run() {

                try
                {
                    speedGauge.setValue(train.getVelocity() * 2.23694); // Multiply by constant to convert m/s to mph
                }
                catch(Exception e)
                {
                    System.out.println("Exception in set speed gauge");
                }
                try
                {
                    accelerationGauge.setValue(train.getAcceleration() * 2.23694); // Multiply by constant to convert m/s^2 to mph^2
                }
                catch(Exception e)
                {
                    System.out.println("Exception in set acceleration gauge");
                }
                try
                {
                    train.update();
                }
                catch(Exception e)
                {
                    System.out.println("Exception in set update");
                }

            }
        };

        new java.util.Timer().scheduleAtFixedRate(tt, 250,250);


    }


    public TrainModel()
    {
        //Create trains ArrayList
        trains = new ArrayList<Train>(25);

        //ID of trains will begin at 1
        ID = 1;
    }

    public int createTrain(int previousBlock, int currentBlock, int cars, boolean setupPID)
    {
        //Create Train
        Train train = new Train(previousBlock, currentBlock, cars, setupPID, ID);

        //Add train to trains
        trains.add(train);

        //Increment ID
        ID++;

        //Return Train ID
        return ID - 1;
    }

    public Train getTrain(int ID)
    {
        //Get iterator for trains
        Iterator<Train> itTrains = trains.iterator();

        //Check trains for train by ID
        Train temp = null;
        while(itTrains.hasNext())
        {
            temp = itTrains.next();

            if(temp.getID() == ID)
            {
                break;
            }
        }

        return temp;
    }

    public boolean deleteTrain(int ID)
    {
        //Get iterator for trains
        Iterator<Train> itTrains = trains.iterator();

        //Check trains for train by ID
        Train temp = null;
        while(itTrains.hasNext())
        {
            temp = itTrains.next();

            if(temp.getID() == ID)
            {
               trains.remove(temp);
               return true;
            }
        }

        return false;
    }

    public void updateTrains()
    {
        //Get iterator for trains
        Iterator<Train> itTrains = trains.iterator();

        //Update each train iteratively
        while(itTrains.hasNext())
        {
            itTrains.next().update();
        }
    }


}

