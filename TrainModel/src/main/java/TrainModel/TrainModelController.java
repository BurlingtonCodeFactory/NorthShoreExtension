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
import java.util.Timer;

public class TrainModelController extends Application {

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

/*    public void engineFailure()
    {
        train.engineFailure();
        POWER.setText(Double.toString(train.getPower()));
    }

    public void signalPickupFailure()
    {
        train.signalPickupFailure();
    }
    public void brakeFailure()
    {
        train.brakeFailure();
    }


    public void EBrake()
    {
        train.activateEBrake();
        BRAKE_INDICATOR.setFill(Paint.valueOf("#f70e12"));
    }*/
}

