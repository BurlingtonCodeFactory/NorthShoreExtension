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


        double integral = kI*(uK +integral_last);


        double power_out = integral + kP*error;

        if(power_out>power_max){
            integral = kI*(integral_last +integral_last);

            power_out = integral +kP*error;
        } else{
            integral_last =  uK;
        }

        error_last = error;


        return power_out;

    }


}
