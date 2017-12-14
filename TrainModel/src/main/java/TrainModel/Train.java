package TrainModel;

import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.Models.Line;
import TrainController.TrainController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.Math;
import java.util.Random;

public class Train {

    //All values are are calculated within the program using SI units. Returned values will be converted to U.S customary units.

    //Initialize constructor properties
    int ID;
    int previousBlock, currentBlock;
    TrainController trainController;
    boolean PIDSetupbypass;
    ITrackModelForTrainModel track;
    Line line;

    //Initialize other train properties
    private SimpleIntegerProperty carsProperty;
    private SimpleDoubleProperty powerProperty;
    private SimpleDoubleProperty speedProperty;
    private SimpleDoubleProperty accelerationProperty;
    private SimpleDoubleProperty massProperty, lengthProperty, heightProperty, widthProperty;
    private SimpleDoubleProperty authorityProperty;
    private SimpleDoubleProperty cabinTempProperty;
    private SimpleStringProperty RISProperty;
    private SimpleBooleanProperty lightsProperty;
    private SimpleBooleanProperty leftDoorsProperty, rightDoorsProperty;
    private SimpleBooleanProperty brakesProperty;
    private SimpleIntegerProperty passengerCountProperty = new SimpleIntegerProperty();

    private double g = 9.8;
    private double coeffFriction = 0.001;
    private double gradeForce = 0, frictionForce = 0, brakingForce = 0, powerForce = 0, staticForce = 0, dynamicForce = 0, netForce = 0;
    private double grade;
    private double velocity = 0;
    private double  displacement = 0, totalDisplacement = 0, totalBlockLength = 0;
    private double previousAcceleration = 0, brakingAcceleration = 0;
    private double deltaTmillis;
    private boolean brakeFailure = false, signalPickupFailure = false, engineFailure = false;
    private int capacity;
    private boolean delete = false;
    private int it = 0;

    public Train(int previousBlock, int currentBlock, int cars, TrainController trainController, boolean PIDSetupbypass, int ID, ITrackModelForTrainModel track, Line line)
    {
        //Assign values to fields
        this.previousBlock = previousBlock;
        this.currentBlock = currentBlock;
        this.trainController = trainController;
        this.PIDSetupbypass = PIDSetupbypass;
        this.ID = ID;
        this.track = track;
        this.line = line;
        this.carsProperty = new SimpleIntegerProperty();
        this.powerProperty = new SimpleDoubleProperty();
        this.authorityProperty = new SimpleDoubleProperty();
        this.speedProperty = new SimpleDoubleProperty();
        this.massProperty = new SimpleDoubleProperty();
        this.heightProperty = new SimpleDoubleProperty();
        this.lengthProperty = new SimpleDoubleProperty();
        this.widthProperty = new SimpleDoubleProperty();
        this.accelerationProperty = new SimpleDoubleProperty();
        this.cabinTempProperty = new SimpleDoubleProperty();
        this.leftDoorsProperty = new SimpleBooleanProperty();
        this.rightDoorsProperty = new SimpleBooleanProperty();
        this.lightsProperty = new SimpleBooleanProperty();
        this.brakesProperty = new SimpleBooleanProperty();
        this.RISProperty = new SimpleStringProperty();
        carsProperty.set(cars);
        speedProperty.set(0);
        accelerationProperty.set(0);
        cabinTempProperty.set(67);
        heightProperty.set(11.22);
        widthProperty.set(8.69);
        lengthProperty.set(cars * 105);
        passengerCountProperty.set(0);
        capacity = carsProperty.get() * 222;

        //Calculate Initial Train Mass
        setMass(carsProperty.getValue() * 37103);

        //Initialize block length tracking
        totalBlockLength = track.getLengthByID(currentBlock, line);

        track.setOccupancy(currentBlock, true, line);
    }

    public void update(double elapsedTime)
    {
        //Calculate elapsed time since last update
        deltaTmillis = elapsedTime;

        //Calculate displacement, and track and report block changes
        displacement = velocity * deltaTmillis / 1000;
        totalDisplacement = totalDisplacement + displacement;

        while(totalDisplacement > totalBlockLength)         //This loop iterates as long as the trains displacement has surpassed the given displacement for this block
        {
            //Get next block and add its length to total block length
            trainController.nextBlock();
            int temp = track.getNextBlock(previousBlock, currentBlock, line);
            if(temp == -2)
            {
                System.out.println("This is not okay");
            }
            totalBlockLength = totalBlockLength + track.getLengthByID(temp, line);

            //Update Occupancy of (now) previous block to false
            track.setOccupancy(currentBlock, false, line);

            track.setOccupancy(temp, true, line);

            //Update current and previous blocks
            previousBlock = currentBlock;
            currentBlock = temp;
        }

        //Check if train has just driven into yard
        if((previousBlock != -1) && (currentBlock == 0))
        {
            if(it == 1)
            {
                delete();
            }
            it++;
        }

        //Get physical data from track
        coeffFriction = track.getFrictionByID(currentBlock, line);
        grade = track.getGradeByID(currentBlock, line);

        //Get input data from train controller
        setPower(trainController.getPower(deltaTmillis));

        if(trainController.getServiceBrake())
        {
            brakingAcceleration = 1.2;
            setBrake(true);
        }
        if(trainController.getEmergencyBrake())
        {
            brakingAcceleration = 2.3;
            setBrake(true);
        }
        else if (!(trainController.getServiceBrake() || trainController.getEmergencyBrake()))
        {
            brakingAcceleration = 0;
            setBrake(false);
        }

        //Calculate new acceleration
        double temp = calculateAcceleration();
        previousAcceleration = getAcceleration();
        setAcceleration(temp);

        //Calculate new velocity and speed
        velocity = velocity  + (((previousAcceleration + getAcceleration()) / 2) * (deltaTmillis / 1000)); // Average previous two accelerations, multiply by deltaT and add to existing velocity
        setSpeed(Math.abs(velocity));

        //System.out.println("Train Velocity="+velocity +" Power="+getPower() +" Location="+currentBlock + " Distance="+(totalDisplacement-totalBlockLength));

        //Relay data to Train Controller
        setAuthority(track.getAuthorityByID(currentBlock, line));
        trainController.updateVelocity(velocity);
        trainController.setAuthority(track.getAuthorityByID(currentBlock, line));
        trainController.calcSetpointVelocity(track.getSpeedByID(currentBlock, line));
        trainController.setBeacon(track.getBeaconByID(currentBlock, line));
        trainController.setUnderground(track.getUndergroundByID(currentBlock, line));

        //Other train processes
        if(trainController.getLights()) //TODO: all of this
        {

        }

        if(trainController.getLeftDoors())
        {

        }

        if(trainController.getRightDoors())
        {

        }

        if(getCabinTemp() != trainController.getCabinTemp()) //TODO: this
        {

        }
//
//        if(trainController.isStoppedAtStation())
//        {
//            embarkDebark();
//        }

        setRIS(trainController.getRIS());
    }

    public boolean delete() //TODO: How to implement?
    {
        System.out.println("Train: Marking for deletion: " + ID);

        track.setOccupancy(currentBlock, false, line);

        this.delete = true;

        it = 0;

        return true;
    }

    public void embarkDebark()
    {
        //Debark passengers some value less than current passengers
        Random rand = new Random();
        int debark = rand.nextInt(passengerCountProperty.get() + 1);
        passengerCountProperty.set(passengerCountProperty.get() - debark);

        //Embark passengers within capacity of train
        int embark  = rand.nextInt(capacity - passengerCountProperty.get() + 1);
        passengerCountProperty.set(passengerCountProperty.get() + embark);

        //Recalculate train mass
        setMass((carsProperty.getValue() * 37103) * (passengerCountProperty.get() * 73)); //73 kg is estimated as the average weight of passengers

        //Relay throughput to CTC
        track.disembarkPassengers(debark);
    }

    public double calculateAcceleration()
    {
        //Calculate forces
        frictionForce = getMass() * g * coeffFriction * Math.cos(Math.toRadians(grade)); // We've got to convert this to degrees
        brakingForce = brakingAcceleration * getMass();
        gradeForce = -(getMass() * g * Math.sin(Math.toRadians(grade))); //Negative here as a positive grade will reduce forward force
        if(previousBlock > currentBlock) //Invert grade force if train is traveling in opposite direction on track
        {
            gradeForce = gradeForce * -1;
        }
        powerForce = Math.sqrt((getPower() * getMass() * 2) / (deltaTmillis / 1000)); // I'll explain whats going on here in lecture

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

        setAcceleration(netForce / getMass());
        return getAcceleration();
    }

    //Setters//////////////////////////////////////////////////

    public void setEmergencyBrake()
    {
        if(brakeFailure == false)
        {
            brakingAcceleration = 2.73;
            setBrake(true);
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

    public void setPower(double power){ powerProperty.setValue(power);}

    public void setMass(double mass){massProperty.setValue(mass);}

    public void setAuthority(double authority){ authorityProperty.setValue(authority);}


    public void setRIS(String RIS){RISProperty.setValue(RIS);}

    public void setSpeed(double speed){ speedProperty.setValue(speed);}

    public void setAcceleration(double acceleration){ accelerationProperty.setValue(acceleration);}

    public void setBrake(boolean brakesOn){brakesProperty.setValue(brakesOn);}

    public void setPassengerCount(int passengerCount){passengerCountProperty.set(passengerCount);}

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
        return Math.floor(powerProperty.doubleValue() * 100) / 100; //Truncates decimal places
    }

    public double getMass(){return massProperty.doubleValue();}

    public boolean getDelete(){return delete;}

    public double getCabinTemp(){return cabinTempProperty.doubleValue();}

    public SimpleDoubleProperty getPowerProperty() {
        return powerProperty;
    }

    public SimpleDoubleProperty getAuthorityProperty() {
        return authorityProperty;
    }

    public SimpleDoubleProperty getCabinTempProperty(){return cabinTempProperty;}

    public SimpleDoubleProperty getMassProperty(){return massProperty;}

    public SimpleDoubleProperty getLengthProperty(){return lengthProperty;}

    public SimpleDoubleProperty getWidthProperty(){return widthProperty;}

    public SimpleDoubleProperty getHeightProperty(){return heightProperty;}

    public SimpleIntegerProperty getCarsProperty(){return carsProperty;}

    public SimpleBooleanProperty getLightsProperty(){return lightsProperty;}

    public SimpleBooleanProperty getLeftDoorsProperty(){return leftDoorsProperty;}

    public SimpleBooleanProperty getRightDoorsProperty(){return rightDoorsProperty;}

    public SimpleBooleanProperty getBrakesProperty(){return brakesProperty;}

    public SimpleDoubleProperty getSpeedProperty() {
        return speedProperty;
    }

    public SimpleDoubleProperty getAccelerationProperty() {
        return accelerationProperty;
    }

    public SimpleIntegerProperty getPassengerCountProperty(){return passengerCountProperty;}

    public double getSpeed(){ return speedProperty.doubleValue(); }
}
