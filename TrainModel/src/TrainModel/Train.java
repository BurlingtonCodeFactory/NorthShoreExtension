package TrainModel;

import java.lang.Math;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class Train {

    //All values are are calculated within the program using SI units. Returned values will be converted to U.S customary units.

    private double g = 9.8;
    private double coeffFriction = 0.57;
    private double brakingDeceleration = 0;
    private double maximumAcceleration = 0.5;
    private double maximumVelocity = 19.4444;
    private double power, grade, mass;
    private double velocity;
    private double acceleration;
    private double deltaT;



    public Train (int cars, double pwr, double grd)
    {
        //Set power and grade
        power = pwr;
        grade = grd;

        //Calculate mass of train based on type and cars number
        mass = 37104 * cars;

        //Train begins with velocity zero and maximum acceleration
        velocity = 0;
        acceleration = maximumAcceleration;

        //Initialize clock;
        Clock clock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };

        System.out.println(clock);
    }

    public void update()
    {
        //Calculate new velocity, limit to max
        velocity = velocity  + (acceleration * deltaT);

        if(velocity > maximumVelocity)
        {
            velocity = maximumVelocity;
        }

        //Calculate new acceleration, limit to max. If velocity is zero, preset to maximum acceleration
        if(velocity == 0 )
        {
            acceleration = maximumAcceleration;
        }
        else
        {
            acceleration = power/(velocity * mass) - (g * (coeffFriction * Math.sin(grade))) + brakingDeceleration;

            if(acceleration > maximumAcceleration)
            {
                acceleration = maximumAcceleration;
            }
        }

    }

    public void activateEBrake()
    {
        brakingDeceleration = -2.73;
    }

    public String getAccelerationString()
    {
        return String.valueOf(acceleration);
    }

    public String getVelocityString()
    {
        return String.valueOf(velocity);
    }

    public String getPowerString()
    {
        return String.valueOf(power);
    }
}
