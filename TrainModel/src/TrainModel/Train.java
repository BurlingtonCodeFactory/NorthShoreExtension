package TrainModel;

import java.lang.Math;

public class Train {

    //All values are are calculated within the program using SI units. Returned values will be converted to U.S customary units.

    //Initialize properties
    int cars, ID;
    int previousBlock, currentBlock;
    boolean setupPID;

    //
    private double g = 9.8;
    private double coeffFriction = 0.57;
    private double gradeForce = 0, frictionForce = 0, brakingForce = 0, powerForce = 0, staticForce = 0, dynamicForce = 0, netForce = 0;
    private double power, grade, mass;
    private double velocity = 0, speed = 0;
    private double acceleration = 0, previousAcceleration = 0, brakingAcceleration = 0;
    private double previousTimestamp, deltaTmillis;
    private boolean brakeFailure = false, signalPickupFailure = false, engineFailure = false;

    public Train(int previousBlock, int currentBlock, int cars, boolean setupPID, int ID)
    {
        //Assign values to fields
        this.previousBlock = previousBlock;
        this.currentBlock = currentBlock;
        this.cars = cars;
        this.setupPID = setupPID;
        this.ID = ID;

        //Initialize associated Train Controller
//        TrainController trainController = new TrainController(setupPID);
    }

    public Train (int cars, double pwr, double grd)
    {
        //Set power and grade
        power = pwr;
        grade = grd;

        //Calculate mass of train based on type and cars number
        mass = 37096 * cars;

        //Get Initial timestamp (wall-clock time)
        previousTimestamp = System.currentTimeMillis();
    }

    public void update()
    {
        //Calculate elapsed time since last update
        deltaTmillis = System.currentTimeMillis() - previousTimestamp;
        previousTimestamp = System.currentTimeMillis();

        //Calculate new velocity and speed
        velocity = velocity  + (((previousAcceleration + acceleration) / 2) * (deltaTmillis / 1000)); // Average previous two accelerations, multiply by deltaT and add to existing velocity
        speed = Math.abs(velocity);



        //Calculate forces
        frictionForce = mass * g * coeffFriction * Math.cos(Math.toRadians(grade)); // We've got to convert this to degrees
        brakingForce = brakingAcceleration * mass;
        gradeForce = -(mass * g * Math.sin(Math.toRadians(grade))); //Negative here as a positive grade will reduce forward force
        powerForce = Math.sqrt((power * mass * 2) / (deltaTmillis / 1000)); // I'll explain whats going on here in lecture

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
            if(dynamicForce > staticForce)              //Acceleration
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

        //Calculate new acceleration
        previousAcceleration = acceleration;
        acceleration = netForce / mass;
    }

    public boolean delete()
    {
        return false;
    }

    //Setters//////////////////////////////////////////////////

    public void setSuggestedSpeed(double suggestedSpeed1)
    {

    }

    public void setAuthority(double authority)
    {

    }

    public void setBeacon(int beacon)
    {

    }

    public void setEmergencyBrake()
    {

    }

    public void activateEBrake()
    {

        if(brakeFailure == false)
        {
            brakingAcceleration = 2.73;
        }
    }

    public void setCurrentSpeed(double currentSpeed)
    {

    }

    public void setUnderground(boolean underground)
    {

    }

    public void setEngineFailure()
    {
        power = 0;
    }

    public void setSignalPickupFailure(){

    }

    public void setBrakeFailure()
    {
        brakeFailure = true;
    }


    //Getters//////////////////////////////////////////////////

    public int getID()
    {
        return ID;
    }

    public double getVelocity()
    {
        return speed;
    }

    public double getAcceleration()
    {
        return acceleration;
    }

    public double getPower()
    {
        return power;
    }

}
