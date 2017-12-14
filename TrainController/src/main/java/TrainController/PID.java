package TrainController;

public class PID
{

    Double kI;
    Double kP;
    Double currentVel;
    Double setpointVel;
    Double error;
    int power_max = 480000;
    Double integral_last;
    Double error_last;


    public PID()
    {
        kI = 297.0;
        kP = 990.0;
        integral_last = 0.0;
        error_last = 0.0;
    }

    public PID(Double kI, Double kP)
    {
        this.kP = kP;
        this.kI = kI;
    }

    public double getKP()
    {
        return kP;
    }

    public void setkP(double kp)
    {
        this.kP = kp;
    }

    public double getPower(double vel_input, double setpoint_vel, double period)
    {

        error = (setpoint_vel - vel_input);
        double errorSum = error + error_last;
        double uK = (period / 2000) * errorSum;
        double integral = kI * (uK + integral_last);
        double power_out = integral + kP * error;

        if (power_out > power_max)
        {
            integral = kI * (integral_last + integral_last);
            power_out = integral + kP * error;
        }
        else
        {
            integral_last = uK;
        }

        error_last = error;

        if (power_out < 0)
        {
            power_out = 0;
        }


        return power_out;

    }


}
