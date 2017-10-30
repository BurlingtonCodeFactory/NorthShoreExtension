package TrainController;

public class Train {

    String name;

    Double current_velocity;

    Double commanded_velocity;

    Double authority;

    Double acceleration;

    boolean emergency_brake;

    boolean brake;

    int doors; //0 = all closed, 1 = left open, 2 = right open

    boolean lights;

    Double power_out;

    Double input_velocity;




    public Train(String name){



        doors = 0;

        emergency_brake = false;

        brake = false;
        current_velocity =0.0;
        power_out=0.0;

        this.name = name;

        input_velocity  = 0.0;



    }



    public Train(){



        doors = 0;

        emergency_brake = false;

        brake = false;

        this.name = " ";

        current_velocity =0.0;
        power_out=0.0;
        input_velocity  = 0.0;





    }



    public void set_authority(Double a){

        this.authority = a;

    }

    public void set_power_out(Double p){
        this.power_out = p;
    }

    public Double get_power_out(){
        return this.power_out;
    }

    public void set_cmd_vel(Double v){

        this.commanded_velocity = v;

    }



    public void update_velocity(Double v){

        this.current_velocity = v;

    }



    public void update_accel(Double a){

        this.acceleration = a;

    }



    public void changeLights(){

        if(lights){

            this.lights = false;



        }else {

            this.lights = true;

        }

    }



    public void open_left_doors(){

        this.doors = 1;

    }



    public void open_right_doors(){

        this.doors = 2;

    }



    public void close_doors(){

        this.doors = 0;

    }



    public void change_brake(){

        if(brake){

            this.brake = false;

        } else {

            this.brake = true;

        }

    }



    public void change_e_brake(){

        if (emergency_brake){

            this.emergency_brake = false;

        } else{

            this.emergency_brake = true;

        }

    }



}
