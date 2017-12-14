package TrainController;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class GUIController {

    @FXML
    public GridPane individual_view_pane;

    @FXML
    public ScrollPane groupViewScroll;

    private GridPane groupViewGrid = new GridPane();
    private Gauge commanded_velocity_gauge;
    private Gauge current_velocity_gauge;
    private Gauge power_gauge;
    private ArrayList<TrainController> trainControllers;
    private ArrayList<String> trainNames;

    public GUIController(ArrayList<TrainController> trainControllers, ArrayList<String> trainNames) {
        this.trainControllers = trainControllers;
        this.trainNames = trainNames;

    }

    @FXML
    public void initialize() {
        groupViewGrid.setPrefWidth(groupViewScroll.getPrefWidth());
        groupViewScroll.setContent(groupViewGrid);

    }


    private void addGauges() {
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





    public void changeDisplay(String trainName){

        try {
            commanded_velocity_gauge.valueProperty().unbind();
            current_velocity_gauge.valueProperty().unbind();
            power_gauge.valueProperty().unbind();
        } catch (NullPointerException n){}

        int i = trainNames.indexOf(trainName);

        try {
            TrainController trainController = trainControllers.get(i);



        }catch (ArrayIndexOutOfBoundsException e){}

    }



    public void addTrainController(TrainController train) {
        System.out.println("addtrain");

        trainControllers.add(train);
        trainNames.add(train.name);


        RowConstraints constraints = new RowConstraints();
        constraints.setPrefHeight(200);
        AnchorPane anchor = new AnchorPane();
        anchor.setPrefHeight(200);
        anchor.setPrefWidth(groupViewGrid.getPrefWidth());

        groupViewGrid.getRowConstraints().add(constraints);
        groupViewGrid.addRow(trainControllers.size());
        groupViewGrid.add(anchor, 0, trainControllers.size()-1);
        createIndividualDisplay(anchor, train);



    }
    public void deleteTrainController(TrainController train) {
        trainControllers.remove(train);
        trainNames.remove(train.name);
        /*for(TrainControllerGroupDisplay d : trainControllerDisplayObservableList){
            if(d.trainName == train.name){
                trainControllerDisplayObservableList.remove(d);
            }
        }*/


    }

    public void createIndividualDisplay(AnchorPane anchor, TrainController trainController){
        Gauge command_gauge;
        Gauge speed_gauge;
        Gauge power_gauge;
        TextField authority;
        TextField name;
        Slider velocity_select;
        ToggleButton left_door;
        ToggleButton right_door;
        ToggleButton auto_button;
        ToggleButton e_brake;
        ToggleButton lights;
        GridPane pane;
        String trainName;
        trainName = trainController.name;
        pane = new GridPane();
        setConst(pane, 1,6);
        pane.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        String com_gauge_str = "Commanded Velocity";
        String vel_gauge_str = "Current Velocity";
        String pwr_gauge_str = "Power Output";

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

        trainController.getCurrentVelocityProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                speed_gauge.setValue(newValue.doubleValue()*2.2369);
            }
        });

        trainController.getPowerProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                power_gauge.setValue(newValue.doubleValue()/1000);
            }
        });

        trainController.getSetpointVelocityProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                command_gauge.setValue(newValue.doubleValue()*2.2369);
            }
        });
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
        auth_label.setText("Authority (yards)");
        auth_label.setFont(font);
        auth_label.setAlignment(Pos.TOP_CENTER);

        authority = new TextField();

        authority.textProperty().bind(trainController.getAuthorityProperty().multiply(1.09).asString("%.3f"));
        pane_one.add(auth_label, 0,2);


        pane_one.add(authority, 0, 3);



        TextField temp = new TextField();
        temp.textProperty().bind(trainController.getCabinTempProperty());
        temp.setAlignment(Pos.CENTER);
        TextField tempValue = new TextField();
        tempValue.setPromptText("Input new Temperature here:");
        tempValue.setAlignment(Pos.CENTER);
        Button tempSubmit = new Button("Change Temp");

        tempSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               try {
                   double temp = Double.parseDouble(tempValue.getText());


                   trainController.setDesiredCabinTemp(temp);
               } catch (NumberFormatException n){

               }
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

        TextField door_field = new TextField();
        door_field.textProperty().bind(trainController.getDoorSide());
        pane_two.add(door_field,0,1);
        door_field.setAlignment(Pos.CENTER);

        left_door = new ToggleButton();
        left_door.selectedProperty().bindBidirectional(trainController.getLeleftOpenDoorProperty());
        left_door.setText("Open Left Door");
        left_door.setAlignment(Pos.CENTER);

        right_door = new ToggleButton();
        right_door.selectedProperty().bindBidirectional(trainController.getRightOpenDoorProperty());
        right_door.setText("Open Right Door");
        right_door.setAlignment(Pos.CENTER);

        auto_button = new ToggleButton();
        auto_button.setAlignment(Pos.CENTER);
        auto_button.setText("Automatic Mode");
        auto_button.selectedProperty().bindBidirectional(trainController.getAutoModeProperty());

        lights = new ToggleButton();
        lights.selectedProperty().bindBidirectional(trainController.getLightsProperty());
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
                if(!trainController.getAutoModeProperty().getValue()) {
                    trainController.calcSetpointVelocity(newValue.doubleValue());
                }
            }
        });

        pane_three.add(velocity_select, 0, 0);

        GridPane smallPane = new GridPane();
        smallPane.setPrefWidth(100);
        smallPane.setPrefHeight(200);
        RowConstraints smallRow = new RowConstraints();
        smallRow.setPrefHeight(100);
        smallPane.getRowConstraints().add(smallRow);
        smallPane.addRow(0);
        smallPane.addRow(1);

        ToggleButton serviceBrake = new ToggleButton();
        serviceBrake.setText("Turn on Service Brake");
        serviceBrake.setPrefHeight(80);
        serviceBrake.setWrapText(true);
        e_brake = new ToggleButton();
        e_brake.setText("Turn on \nEmergency \nBrake");

        e_brake.setAlignment(Pos.CENTER);
        e_brake.setPrefHeight(80);
        e_brake.setWrapText(true);
        e_brake.selectedProperty().bindBidirectional(trainController.getEmergencyBrakeProperty());
        serviceBrake.selectedProperty().bindBidirectional(trainController.getServiceBrakeProperty());


        smallPane.add(e_brake, 0,0);
        smallPane.add(serviceBrake,0,1);


        pane_three.add(smallPane, 1,0);

        RowConstraints row_three_two = new RowConstraints();
        row_three_two.setPercentHeight(10);
        pane_three.getRowConstraints().add(row_three_two);
        pane_three.add(vel_label, 0,1);

        pane.add(pane_three, 5,0);

        anchor.getChildren().add(pane);
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

}


