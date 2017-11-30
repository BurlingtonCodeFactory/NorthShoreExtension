package TrainModel;

import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.Models.Line;
import TrainController.TrainController;
import javafx.application.Application;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.lang.Math;

public class Train {

    //All values are are calculated within the program using SI units. Returned values will be converted to U.S customary units.

    //Initialize constructor properties
    int cars, ID;
    int previousBlock, currentBlock;
    TrainController trainController;
    boolean PIDSetupbypass;
    ITrackModelForTrainModel track;
    Line line;

    //Initialize other train properties
    private double g = 9.8;
    private double coeffFriction = 0.001;
    private double gradeForce = 0, frictionForce = 0, brakingForce = 0, powerForce = 0, staticForce = 0, dynamicForce = 0, netForce = 0;
    private SimpleDoubleProperty powerProperty;
    private SimpleDoubleProperty speedProperty;
    private SimpleDoubleProperty accelerationProperty;
    private double grade, mass;
    private double velocity = 0;
    private double  displacement = 0, totalDisplacement = 0, totalBlockLength = 0;
    private double previousAcceleration = 0, brakingAcceleration = 0;
    private double previousTimestamp, deltaTmillis;
    private boolean brakeFailure = false, signalPickupFailure = false, engineFailure = false;
    private double cabinTemp = 67;
    private String RIS = "";

    public Train(int previousBlock, int currentBlock, int cars, TrainController trainController, boolean PIDSetupbypass, int ID, ITrackModelForTrainModel track, Line line)
    {
        //Assign values to fields
        this.previousBlock = previousBlock;
        this.currentBlock = currentBlock;
        this.cars = cars;
        this.trainController = trainController;
        this.PIDSetupbypass = PIDSetupbypass;
        this.ID = ID;
        this.track = track;
        this.line = line;
        this.powerProperty = new SimpleDoubleProperty();
        this.speedProperty = new SimpleDoubleProperty();
        this.accelerationProperty = new SimpleDoubleProperty();
        speedProperty.set(0);
        accelerationProperty.set(0);

        //Calculate Initial Train Mass
        mass = cars * 37096;

        //Initialize block length tracking
        totalBlockLength = track.getLengthByID(currentBlock, line);

        //Get Initial timestamp (wall-clock time)
        previousTimestamp = System.currentTimeMillis();

        System.out.println("Setting occupancy");
        track.setOccupancy(1, true,Line.GREEN);
    }

    public void update()
    {
        //Calculate elapsed time since last update
        deltaTmillis = System.currentTimeMillis() - previousTimestamp;      //TODO: How are we doing this
        previousTimestamp = System.currentTimeMillis();

        //Calculate displacement, and track and report block changes
        displacement = velocity * deltaTmillis / 1000;
        totalDisplacement = totalDisplacement + displacement;

        while(totalDisplacement > totalBlockLength)         //This loop iterates as long as the trains displacement has surpassed the given displacement for this block
        {
            //Get next block and add its length to total block length
            int temp = trainController.nextBlock(previousBlock, currentBlock);
            totalBlockLength = totalBlockLength + track.getLengthByID(temp, line);

            //Update Occupancy of (now) previous block to false
            track.setOccupancy(currentBlock, false, line);

            //Update current and previous blocks
            previousBlock = currentBlock;
            currentBlock = temp;
        }
        track.setOccupancy(currentBlock,true, line); //Update occupancy of current block to true

        //Check if train has just driven into yard
        if(!(previousBlock == -1) && (currentBlock == 0))
        {
            delete();
        }


        //Get physical data from track
        coeffFriction = track.getFrictionByID(currentBlock, line); //TODO: these methods need to take a block ID, talk to Andrew
        grade = track.getGradeByID(currentBlock, line);

        //Get input data from train controller
        setPower(trainController.getPower());

        if(trainController.getServiceBrake())
        {
            brakingAcceleration = 1.2;
        }
        if(trainController.getEmergencyBrake())
        {
            brakingAcceleration = 2.3;
        }
        else if (!(trainController.getServiceBrake() || trainController.getEmergencyBrake()))
        {
            brakingAcceleration = 0;
        }

        //Calculate new acceleration
        double temp = calculateAcceleration();
        previousAcceleration = getAcceleration();
        setAcceleration(temp);

        //Calculate new velocity and speed
        velocity = velocity  + (((previousAcceleration + getAcceleration()) / 2) * (deltaTmillis / 1000)); // Average previous two accelerations, multiply by deltaT and add to existing velocity
        setSpeed(Math.abs(velocity));

        //Relay data to Train Controller
        trainController.updateVelocity(velocity);
        trainController.setBeacon(track.getBeaconByID(currentBlock, line));
        trainController.setAuthority(track.getAuthorityByID(currentBlock, line));
        trainController.setSetpointVelocity(track.getSpeedByID(currentBlock, line));
        trainController.setUnderground(track.getUndergroundByID(currentBlock, line));

        //Other train processes
        if(trainController.getLights())
        {

        }

        if(trainController.getLeftDoors())
        {

        }

        if(trainController.getRightDoors())
        {

        }

        if(cabinTemp != trainController.getCabinTemp()) //TODO: this
        {

        }

        RIS = trainController.getRIS();

    }

    public boolean delete() //TODO: How to implement?
    {
        return false;
    }

    public void embarkDebark()
    {
        //Debark passengers some value less than current passengers

        //Embark passengers within capacity of train

        //Recalculate train mass

        //Relay throughput to CTC
    }

    public double calculateAcceleration()
    {
        //Calculate forces
        frictionForce = mass * g * coeffFriction * Math.cos(Math.toRadians(grade)); // We've got to convert this to degrees
        brakingForce = brakingAcceleration * mass;
        gradeForce = -(mass * g * Math.sin(Math.toRadians(grade))); //Negative here as a positive grade will reduce forward force
        powerForce = Math.sqrt((getPower() * mass * 2) / (deltaTmillis / 1000)); // I'll explain whats going on here in lecture

        staticForce = frictionForce + brakingForce;
        dynamicForce = powerForce + gradeForce;

        //One last velocity check
        if(velocity < 0 && staticForce > dynamicForce)
        {
            velocity = 0;
        }

        //Calculate net force
        if(velocity > 0)                                //Forward movement
        {
            netForce = dynamicForce - staticForce;
        }
        else if (velocity < 0 && gradeForce < 0)        //Backwards movement
        {
            netForce = dynamicForce + staticForce;
        }
        else if(velocity == 0)
        {
            if(dynamicForce > staticForce)              //Acceleration from stop
            {
                netForce = dynamicForce - staticForce;
            }
            else if(dynamicForce <= staticForce)
            {

                if(Math.abs(gradeForce) > staticForce)  //Downhill roll from standstill
                {
                    netForce = dynamicForce + staticForce;
                }
                else                                    //Standstill
                {
                    netForce = 0;
                }
            }
        }

        setAcceleration(netForce / mass);
        return getAcceleration();
    }

    //Setters//////////////////////////////////////////////////

    public void setEmergencyBrake()
    {
        if(brakeFailure == false)
        {
            brakingAcceleration = 2.73;
        }

        trainController.setEmergencyBrake();
    }

    public void setEngineFailure()
    {
        setPower(0);
    }

    public void setSignalPickupFailure()
    {

    }

    public void setBrakeFailure()
    {
        brakeFailure = true;
    }

    public void setPower(double power){ powerProperty.set(power);}

    public void setSpeed(double speed){ speedProperty.set(speed);}

    public void setAcceleration(double acceleration){ accelerationProperty.set(acceleration);}


    //Getters//////////////////////////////////////////////////

    public int getID()
    {
        return ID;
    }

    public double getVelocity()
    {
        return velocity;
    }

    public double getAcceleration()
    {
        return accelerationProperty.doubleValue();
    }

    public double getPower()
    {
        return powerProperty.doubleValue();
    }

    public SimpleDoubleProperty getPowerProperty() {
        return powerProperty;
    }

    public double getSpeed(){ return speedProperty.doubleValue(); }

    public SimpleDoubleProperty getSpeedProperty() {
        return speedProperty;
    }

    public SimpleDoubleProperty getAccelerationProperty() {
        return accelerationProperty;
    }
}
