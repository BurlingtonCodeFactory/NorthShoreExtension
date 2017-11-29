package TrainController;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.DoubleToLongFunction;

import static javafx.scene.control.Alert.*;


public class ControllerManager extends Application {
    @FXML
    public GridPane individual_view_pane;
    @FXML
    public ComboBox train_select;
    @FXML
    public TextField authority_display;
    @FXML
    public TextField current_train;
    @FXML
    public Slider velocity_select;
    @FXML
    public GridPane group_grid;



    Gauge commanded_velocity_gauge;
    Gauge current_velocity_gauge;

    Gauge power_gauge;
    ArrayList<Train_display> displays;

    PID pid_ctrl;
    int clicks =0;

    double previousTimestamp;

    public static void main(String[] args) {
        launch(args);

        Stage stage = new Stage();

    }



    public void addGauges() {
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


        individual_view_pane.add(commanded_velocity_gauge, 1, 1);
        individual_view_pane.add(current_velocity_gauge, 2, 1);

        individual_view_pane.add(power_gauge, 3, 1);
    }

    public void addDAta(ArrayList<String> train_names) {
        train_select.setValue("");
        for (int i = 0; i < train_select.getItems().size(); i++) {
            train_select.getItems().set(i, "");
        }
        for (int i = 0; i < train_names.size(); i++) {
            if (i < train_select.getItems().size()) {
                train_select.getItems().set(i, train_names.get(i));
            } else {
                train_select.getItems().add(train_names.get(i));
            }
        }


    }

    public void checkStatus(ArrayList<TrainController> trains, ArrayList<String> train_names) {

        String selected = train_select.getValue().toString();


        current_train.setText(selected);
        int index = train_names.indexOf(selected);
        if (index != -1) {
            TrainController controller = trains.get(index);

            String auth = Integer.toString(controller.authority.intValue());
            authority_display.setText(auth);

            commanded_velocity_gauge.setValue(controller.setpoint_velocity);
            current_velocity_gauge.setValue(controller.current_velocity);
            power_gauge.setValue(controller.getPower()/4000);

            checkInput(controller);
        }
        
        init(trains);


    }

    public void checkInput(TrainController controller) {

        Double v_input = velocity_select.getValue();
        controller.calcSetpointVelocity(v_input);
        Double v_current = controller.current_velocity;





        if(controller.setpoint_velocity != v_current) {
            if(controller.setpoint_velocity< v_current){
                controller.brake = true;
            }
            controller.update();
        }








    }





    public void brakeTrain(TrainController train, double period) {
        train.current_velocity = train.current_velocity - (1.2 * period);
    }


    public void start(Stage primaryStage) throws IOException {


    }

    public void init(ArrayList<TrainController> trains) {

        group_grid.setPrefWidth(1200);
        group_grid.setMaxWidth(group_grid.getPrefWidth());
        group_grid.setMinWidth(group_grid.getPrefWidth());
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        group_grid.getColumnConstraints().add(col);

        displays = new ArrayList<Train_display>();
        RowConstraints row = new RowConstraints();
        row.setPrefHeight(200);
        row.setMaxHeight(row.getPrefHeight());
        row.setMinHeight(row.getPrefHeight());
        group_grid.getRowConstraints().add(row);
        for (int i = 0; i < trains.size(); i++) {

            TrainController train = trains.get(i);
            Train_display display = new Train_display(train);
            GridPane pane = display.getPane();

            displays.add(display);


            group_grid.getRowConstraints().add(row);
            group_grid.addRow(i);


            group_grid.add(pane, 0, i);



        }






    }

    public void addTrain(TrainController train) {

        int rows = getRowCount(group_grid);


        train_select.getItems().add(train.name);

        Train_display display = new Train_display(train);
        GridPane pane = display.getPane();

        displays.add(display);

        group_grid.add(pane, 0, rows - 1);


    }

    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows,rowIndex+1);
                }
            }
        }
        return numRows;
    }
}


