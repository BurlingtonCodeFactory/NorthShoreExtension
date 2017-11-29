package com.company;

public class PID {

    Double kI;
    Double kP;
    Double currentVel;
    Double setpointVel;
    Double error;
    int power_max = 480000;
    Double integral_last;
    Double error_last;



    public PID(){
        kI = 9000.0;
        kP=11250.0;
        integral_last = 0.0;
        error_last=0.0;
    }

    public PID(Double kI, Double kP){
        this.kP = kP;
        this.kI = kI;
    }

    public double getKP(){
        return kP;
    }

    public void setkP(double kp){
        this.kP = kp;
    }

    public double getPower(Double vel_input, Double setpoint_vel, Double period ){

        error = (setpoint_vel - vel_input)*.447307;

        double errorSum = error + error_last;

        double uK = (period/2000) *errorSum;
        System.out.println(Double.toString(errorSum));

        double integral = kI*(uK +integral_last);
        System.out.println("integral gain is  " + Double.toString(integral));
        System.out.println("proportional gain is " + Double.toString(kP*error));

        double power_out = integral + kP*error;

        if(power_out>power_max){
            integral = kI*(integral_last +integral_last);

            power_out = integral +kP*error;
        } else{
            integral_last =  uK;
        }

        error_last = error;

        //System.out.println("Power out is " + Double.toString(power_out));

        return power_out;

    }


}
