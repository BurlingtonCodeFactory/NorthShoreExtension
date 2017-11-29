package TrainModel;


import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;


public class TrainViewController  {

    //All values are are calculated within the program using SI units. Returned values will be converted to U.S customary units.

    //Initialize JavaFX UI Fields
    @FXML
    public TextField POWER;
    @FXML
    public Circle BRAKE_INDICATOR;
    @FXML
    public Pane SPEED_GAUGE_PANE;
    @FXML
    public Pane ACCELERATION_GAUGE_PANE;
    @FXML
    Button EMERGENCY_BRAKE;
    @FXML
    RadioButton ENGINE_FAILURE;
    @FXML
    RadioButton SIGNAL_FAILURE;
    @FXML
    RadioButton BRAKE_FAILURE;

    private Train train;

    public Gauge speedGauge;
    public Gauge accelerationGauge;

    @FXML
    private void initialize()
    {
        System.out.println("Here in init");
        EMERGENCY_BRAKE.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setEmergencyBrake();
            }
        });

        ENGINE_FAILURE.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setEngineFailure();
            }
        });

        SIGNAL_FAILURE.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setSignalPickupFailure();
            }
        });

        BRAKE_FAILURE.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setBrakeFailure();
            }
        });

        //Set up gauges
        speedGauge = GaugeBuilder.create()
                .title("Speed Gauge")
                .subTitle("")
                .unit("mph")
                .build();

        SPEED_GAUGE_PANE.getChildren().add(speedGauge);

        accelerationGauge = GaugeBuilder.create()
                .title("Acceleration Gauge")
                .subTitle("")
                .unit("mph/s")
                .build();
        accelerationGauge.setMinValue(-15.0);
        accelerationGauge.setMaxValue(15.0);

        ACCELERATION_GAUGE_PANE.getChildren().add(accelerationGauge);

        POWER.textProperty().bind(train.getPowerProperty().multiply(0.001).asString());
        train.getSpeedProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                speedGauge.setValue(newValue.doubleValue() * 2.23694);
            }
        });

        train.getAccelerationProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                accelerationGauge.setValue(newValue.doubleValue() * 2.23694);
            }
        });
    }

    @FXML
    public void setEmergencyBrake()
    {
        train.setEmergencyBrake();
    }

    @FXML
    public void setEngineFailure()
    {
        train.setEngineFailure();
    }

    @FXML
    public void setSignalPickupFailure()
    {
        train.setSignalPickupFailure();
    }

    @FXML
    public void setBrakeFailure()
    {
        train.setBrakeFailure();
    }



    public TrainViewController(Train train)
    {
        this.train = train;
    }

}

