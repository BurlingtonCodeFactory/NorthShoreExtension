package TrainModel;

import java.lang.Math;

public class Train {

    //All values are are calculated within the program using SI units. Returned values will be converted to U.S customary units.

    private double g = 9.8;
    private double coeffFrictionStatic = 0.74, coeffFrictionKinetic = 0.57;
    private double gradeDeceleration = 0, staticFrictionAcceleration = 0, kineticFrictionAcceleration = 0, brakingAcceleration = 0;
    private double maximumAcceleration = 1;
    private double maximumVelocity = 19.4444;
    private double power, grade, mass;
    private double velocity;
    private double acceleration;
    private double previousTimestamp, deltaTmillis;
    private boolean brakeFailure = false, signalPickupFailure = false, engineFailure = false;



    public Train (int cars, double pwr, double grd)
    {
        //Set power and grade
        power = pwr;
        grade = grd;

        //Establish existing acceleration components
        gradeDeceleration = g * Math.sin(grade);
        staticFrictionAcceleration = g * coeffFrictionStatic;
        kineticFrictionAcceleration = g * coeffFrictionKinetic;

        //Calculate mass of train based on type and cars number
        mass = 37096 * cars;

        //Train begins with velocity zero
        velocity = 0;

        //Calculate initial acceleration
        acceleration = maximumAcceleration;

        //Get Initial timestamp (wall-clock time)
        previousTimestamp = System.currentTimeMillis();
    }

    public void update()
    {
        //Calculate elapsed time since last update
        deltaTmillis = System.currentTimeMillis() - previousTimestamp;
        previousTimestamp = System.currentTimeMillis();

        //Calculate new velocity
        velocity = velocity  + (acceleration * (deltaTmillis / 1000));

        if(velocity < 0 && gradeDeceleration <= 0)
        {
            velocity = 0;
        }

        //Calculate new acceleration
        if(velocity != 0)
        {
            if(((power / (mass * velocity)) - gradeDeceleration) > (kineticFrictionAcceleration + brakingAcceleration))
            {
                acceleration = (power / (mass * velocity))/* - gradeDeceleration - kineticFrictionAcceleration */- brakingAcceleration
                ;
            }
            else
            {
                acceleration = (power / (mass * velocity))/* - gradeDeceleration - kineticFrictionAcceleration */- brakingAcceleration
                ;
            }
        }
        else
        {

        }

    }

    public void engineFailure()
    {
        power = 0;
    }

    public void signalPickupFailure()
    {

    }

    public void brakeFailure()
    {
        brakeFailure = true;
    }

    public void activateEBrake()
    {

        if(brakeFailure == false)
        {
            brakingAcceleration = 2.73;
        }
    }

    public double getVelocity()
    {
        return velocity;
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
