package TrainController;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.*;


public class ControllerInterface extends Application  {
    @FXML
    private GridPane pane;
    @FXML
    private ComboBox train_select;
    @FXML
    public TextField authority_display;
    @FXML
    public TextField current_train;
    @FXML
    public Slider velocity_select;


    Gauge commanded_velocity_gauge;
    Gauge current_velocity_gauge;
    Gauge acceleration_gauge;
    Gauge power_gauge;

    public static void main(String[] args) {
        launch(args);

        Stage stage = new Stage();

    }

    public void addGauges(){
         commanded_velocity_gauge = GaugeBuilder.create()
                .title("Commanded Velocity")
                .unit("MPH")
                 .maxValue(44)
                .build();

         current_velocity_gauge = GaugeBuilder.create()
                .title("Current Velocity")
                .unit("MPH")
                 .maxValue(44)
                .build();



         power_gauge = GaugeBuilder.create()
                .title("Power Output")
                .unit("KW")
                 .maxValue(120)
                .build();




        pane.add(commanded_velocity_gauge, 1,1);
        pane.add(current_velocity_gauge, 2,1);

        pane.add(power_gauge, 3,1);
    }

    public void addDAta(ArrayList<String>train_names){
        train_select.setValue("");
        for(int i =0; i< train_select.getItems().size(); i++){
            train_select.getItems().set(i, "");
        }
        for (int i =0; i< train_names.size(); i++){
            if(i< train_select.getItems().size()){
                train_select.getItems().set(i, train_names.get(i));
            } else {
                train_select.getItems().add(train_names.get(i));
            }
        }


    }

    public void checkStatus(ArrayList<Train> trains, ArrayList<String>train_names){

            String selected = train_select.getValue().toString();


                current_train.setText(selected);
                int index = train_names.indexOf(selected);
                if(index != -1) {
                    Train train = trains.get(index);
                    String auth =Integer.toString(train.authority.intValue());
                    authority_display.setText(auth);

                    commanded_velocity_gauge.setValue(train.commanded_velocity);
                    current_velocity_gauge.setValue(train.current_velocity);
                    power_gauge.setValue(train.get_power_out());

                    checkInput(train);
                }
    }

    public void checkInput(Train train){
        Double v_input =  velocity_select.getValue();
        train.input_velocity = v_input;
        Double v_current = train.current_velocity;
        Double v_adjust = v_input - v_current;


        if(train.brake){
            train.set_power_out(0.0);


        }else if (v_adjust > 0 ){
            train.brake = false;



                Double k_velocity = v_current * 1.6 * 0.277778;
                Double k_desired_velocity = v_input * 1.6 * 0.277778;
                Double k_v_adjust = k_desired_velocity - k_velocity;
                Double time = k_v_adjust *2;

                Double work = Math.pow(k_desired_velocity, 2.0) - Math.pow(k_velocity, 2.0);
                work = work / 2;
                work = work * 46656.511;
                work = work / time;
                Double power = work / 4000;

                train.set_power_out(power);





        }  else  if (v_adjust < 0){

            train.brake = true;
            train.set_power_out(0.0);

        } else if (v_adjust == 0) {
            train.set_power_out(0.0);
            train.brake = false;

        }

    }

    public void moveTrains(ArrayList<Train>trains) {



        for(int i =0; i<trains.size();i++){
            Train train = trains.get(i);
            Double power = train.get_power_out();
            Double velocity = train.current_velocity;

            if(train.current_velocity < train.input_velocity +.6 && train.current_velocity > train.input_velocity - .6) {

            } else {
                if (train.current_velocity <= train.input_velocity) {
                    train.brake = false;
                }
                if (train.brake) {
                    brakeTrain(train);
                } else if (train.input_velocity < velocity) {
                    train.brake = true;
                } else if (velocity == 0 && power > 0) {
                    Double new_velocity;
                    power = power * 4000;
                    new_velocity = Math.sqrt(power / 23328.25);
                    new_velocity = new_velocity * 3.6;
                    train.current_velocity = new_velocity / 1.6;

                    train.brake = false;
                } else if (velocity > 0 && train.input_velocity > train.current_velocity) {

                    velocity = velocity * 1.6 * 0.277778;
                    Double force = (power * 4000) / velocity;
                    Double accel = force / 46656.511;
                    Double new_velocity = (.5 * accel) + velocity;
                    new_velocity = new_velocity * 3.6;
                    new_velocity = new_velocity / 1.6;
                    train.brake = false;

                    train.current_velocity = new_velocity;
                } else if (train.current_velocity <= train.input_velocity) {

                    train.brake = false;
                }

            }

        }
    }

    public void brakeTrain(Train train){
        train.current_velocity =   train.current_velocity-(.6);
    }




    public  void start(Stage primaryStage) throws IOException{







    }
}
