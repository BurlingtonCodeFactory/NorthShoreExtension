package TrainController;


import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;


public class TrainControllerGroupDisplay {

    Gauge command_gauge;
    Gauge speed_gauge;
    Gauge power_gauge;
    TextField authority;
    TextField name;
    Slider velocity_select;
    ToggleButton left_door;
    ToggleButton right_door;
    ToggleButton auto_button;
    Slider e_brake;
    ToggleButton lights;
    GridPane pane;
    String trainName;


    public TrainControllerGroupDisplay(TrainController trainController) {
        trainName = trainController.name;
        pane = new GridPane();
        setConst(pane, 1,6);
        pane.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        String com_gauge_str = trainController.name.concat(" Commanded Velocity");
        String vel_gauge_str = trainController.name.concat(" Current Velocity");
        String pwr_gauge_str = trainController.name.concat(" Power Output");

        command_gauge = GaugeBuilder.create()
                .maxValue(44.0)
                .title(com_gauge_str)
                .unit("MPH")
                .prefHeight(200)
                .prefWidth(200)
                .build();

        speed_gauge = GaugeBuilder.create()
                .maxValue(44.0)
                .title(vel_gauge_str)
                .unit("MPH")
                .prefHeight(200)
                .prefWidth(200)
                .build();

        power_gauge = GaugeBuilder.create()
                .maxValue(120)
                .title(pwr_gauge_str)
                .unit("KW")
                .prefHeight(200)
                .prefWidth(200)
                .build();

        pane.add(command_gauge, 0, 0);
        pane.add(speed_gauge, 1,0);
        pane.add(power_gauge, 2,0);

        GridPane pane_one = new GridPane();
        ColumnConstraints col_one = new ColumnConstraints();
        col_one.setPercentWidth(100);
        RowConstraints row_one = new RowConstraints();
        row_one.setPercentHeight(25);
        pane_one.addRow(0);
        pane_one.addRow(1);
        pane_one.addRow(2);
        pane_one.addRow(3);
        pane_one.addRow(4);
        pane_one.addRow(5);
        pane_one.addRow(6);
        pane_one.setPrefSize(200, 200);

        setConst(pane, 2,1);
        Font font = new Font("Arial", 16);

        Label train_name = new Label();
        train_name.setText("Train Name:");
        train_name.setFont(font);

        pane_one.add(train_name, 0,0);
        train_name.setAlignment(Pos.TOP_CENTER);
        name = new TextField(trainController.name);

        pane_one.add(name, 0, 1);
        name.setAlignment(Pos.CENTER);

        Label auth_label = new Label();
        auth_label.setText("Authority");
        auth_label.setFont(font);
        auth_label.setAlignment(Pos.TOP_CENTER);
        authority = new TextField(Double.toString(trainController.authority));
        pane_one.add(auth_label, 0,2);

        name = new TextField(trainController.name);
        name.setAlignment(Pos.CENTER);
        pane_one.add(name, 0, 3);
        name.setAlignment(Pos.CENTER);
        String tempString = "Cabin temp is " + Double.toString(trainController.cabinTemp);

        TextField temp = new TextField(tempString);
        temp.setAlignment(Pos.CENTER);
        TextField tempValue = new TextField();
        tempValue.setPromptText("Input new Temperature here:");
        tempValue.setAlignment(Pos.CENTER);
        Button tempSubmit = new Button("Change Temp");

        tempSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double temp = Double.parseDouble(tempValue.getText());

                trainController.setCabinTemp(temp);
            }
        });
        tempSubmit.setAlignment(Pos.CENTER);

        pane_one.add(temp, 0, 4);
        pane_one.add(tempValue, 0, 5);
        pane_one.add(tempSubmit, 0, 6);

        pane.add(pane_one, 3,0);

        GridPane pane_two = new GridPane();
        pane_two.setPrefSize(200, 200);

        setConst(pane_two, 6, 1);

        Label door_label = new Label();
        door_label.setText("The next station has doors on the:");
        Font font_two = new Font("Arial",  12);
        door_label.setFont(font_two);
        door_label.setAlignment(Pos.CENTER);
        pane_two.add(door_label, 0, 0);
        door_label.setAlignment(Pos.CENTER);

        TextField door_field = new TextField("Left Side");
        pane_two.add(door_field,0,1);
        door_field.setAlignment(Pos.CENTER);

        left_door = new ToggleButton();

        left_door.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(trainController.doorsOpenLeft){
                    trainController.doorsOpenLeft = false;
                }else{
                    trainController.open_left_doors();
                }
            }
        });
        left_door.setText("Open Left Door");
        left_door.setAlignment(Pos.CENTER);

        right_door = new ToggleButton();
        right_door.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(trainController.doorsOpenRight){
                    trainController.doorsOpenRight = false;
                }else{
                    trainController.open_right_doors();
                }
            }
        });
        right_door.setText("Open Right Door");
        right_door.setAlignment(Pos.CENTER);

        auto_button = new ToggleButton();
        auto_button.setAlignment(Pos.CENTER);
        auto_button.setText("Start Automatic Mode");

        lights = new ToggleButton();
        lights.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(trainController.lights){
                    trainController.lights = false;
                }else{
                    trainController.lights=true;
                }
            }
        });
        lights.setText("Lights On");
        lights.setAlignment(Pos.CENTER);

        pane_two.add(left_door,0,2);
        pane_two.add(right_door, 0,3);
        pane_two.add(lights, 0, 4);
        pane_two.add(auto_button, 0, 5);

        pane.add(pane_two, 4, 0);

        GridPane pane_three = new GridPane();
        pane_three.setPrefSize(200, 200);
        ColumnConstraints col_three = new ColumnConstraints();
        col_three.setPercentWidth(48);

        RowConstraints row_three = new RowConstraints();
        row_three.setPercentHeight(90);

        pane_three.getRowConstraints().add(row_three);
        pane_three.getColumnConstraints().add(col_three);
        pane_three.addRow(0);
        pane_three.addColumn(0);
        pane_three.addColumn(1);

        Label vel_label = new Label();
        vel_label.setText("Velocity Select");
        vel_label.setAlignment(Pos.BASELINE_CENTER);
        vel_label.setFont(font_two);

        velocity_select = new Slider();
        velocity_select.setMax(44.0);
        velocity_select.setMajorTickUnit(5.0);
        velocity_select.setMinorTickCount(1);
        velocity_select.setPrefHeight(150);
        velocity_select.setShowTickLabels(true);
        velocity_select.setSnapToTicks(true);
        velocity_select.orientationProperty().setValue(Orientation.VERTICAL);

        velocity_select.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                trainController.setpoint_velocity = velocity_select.getValue();
            }
        });

        pane_three.add(velocity_select, 0, 0);

        Label e_brake_label = new Label();
        e_brake_label.setText("E_Brake");
        e_brake_label.setFont(font);
        e_brake_label.setTextFill(Color.RED);

        e_brake = new Slider();
        e_brake.setMax(1.0);
        e_brake.setShowTickLabels(true);
        e_brake.setSnapToTicks(true);
        e_brake.setPrefHeight(150);
        e_brake.orientationProperty().setValue(Orientation.VERTICAL);

        e_brake.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(e_brake.getValue() == 1){
                    trainController.emergency_brake = true;
                } else{
                    trainController.emergency_brake = false;
                }
            }
        });

        Label onLabel = new Label();
        onLabel.setText("ON");
        Label offLabel = new Label();
        offLabel.setText("OFF");
        pane_three.add(e_brake, 1,0);

        RowConstraints row_three_two = new RowConstraints();
        row_three_two.setPercentHeight(10);
        pane_three.getRowConstraints().add(row_three_two);
        pane_three.add(vel_label, 0,1);
        pane_three.add(e_brake_label, 1,1);
        pane.add(pane_three, 5,0);
        setValues(trainController);
    }

    public void setConst(GridPane pane, int rowNum, int colNum){
        double height = pane.getPrefHeight();
        double width = pane.getPrefWidth();
        for(int i =0; i< rowNum; i++){
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(height / rowNum);
            pane.getRowConstraints().add(rowConst);
        }
        for(int i =0; i< colNum; i++){
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(width /colNum);
            pane.getColumnConstraints().add(colConst);
        }
    }

    public void setValues(TrainController trainController){
        try {
            command_gauge.setValue(trainController.commanded_velocity);
            speed_gauge.setValue(trainController.current_velocity);
            power_gauge.setValue(trainController.power_out);
            authority.setText(Double.toString(trainController.authority));
        }
        catch (NullPointerException n ){

        }
    }

    public double getHeight(){
        return pane.getPrefHeight();
    }
    public double getWidth(){
        return pane.getPrefWidth();
    }
    public GridPane getPane(){
        return pane;
    }


}
