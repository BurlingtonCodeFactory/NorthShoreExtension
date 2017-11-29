package com.company;

import javax.swing.*;

import static com.sun.javafx.tools.resource.DeployResource.Type.icon;

public class TrainController {

    double prevAcceleration;
    double acceleration;

    Double current_velocity;

    Double commanded_velocity;

    Double authority;
    double previousTime;

    boolean auto;
    boolean emergency_brake;

    boolean brake;

    boolean doorsOpenLeft;
    boolean doorsOpenRight;//0 = all closed, 1 = left open, 2 = right open

    boolean lights;

    Double power_out;

    Double input_velocity;

    Double setpoint_velocity;

    int currentBlock;
    int previousBlock;
    double kp;
    PID pid;
    Double cabinTemp;
    String [] stations = new String[226];

    Double blockLength;
    Double distToStation;
    boolean stopping;
    Double speedLimit;
    Double stoppingDistance;
    double speed;
    int beacon;

    Node greenYard;
    Node redYard;
    String RIS;
    boolean arriving;
    int searchNode;
    String name;
    int ID;



    public TrainController( boolean pidInputBypass, int previous, int current, int ID){

        this.ID = ID;

        this.name = "Train Controller " + Integer.toString(ID);
        prevAcceleration = 0;
        acceleration=0;

        currentBlock = current;
        previousBlock = previous;
        stopping = false;

        doorsOpenLeft = false;
        doorsOpenRight = false;

        emergency_brake = false;

        brake = false;
        current_velocity =0.0;
        power_out=0.0;
        previousTime = System.currentTimeMillis();


        input_velocity  = 0.0;
        authority =0.0;

        Node yard = new Node("yard", -1, null);
        greenYard = yard;
        Node node = yard;
        this.speedLimit = Double.MAX_VALUE;
        this.cabinTemp = 67.0;

        stations[157] = "Shadyside";
        stations[166] = "Herron Ave";
        stations[171] = "Swissville";
        stations[175] = "Penn Station";
        stations[185] = "Steel Plaza";
        stations[195] ="First Ave";
        stations[198] = "Station Square";
        stations[210] = "South Hills Junction";
        stations[2] = "Pioneer";
        stations[9] = "Edgebrook";
        stations[16] = "Station";
        stations[22] = "Whited";
        stations[31]="South Bank";
        stations[39] = "Central";
        stations[48] = "Inglewood";
        stations[57] = "Overbrook";
        stations[65] = "Glenbury";
        stations[73] = "Dormont";
        stations[77] = "Mount Lebanon";
        stations[88] = "Poplar";
        stations[96] = "Castle Shannon";

        int j = 0;
        for (int i =0; i<stations.length;i++){
            if(stations[i] != null){
                node = addNode(node, stations[i], i);
                node.setStationNum(j);
                j++;
            }
            if(i == 150){
                node = addNode(node, "yard", -1);
                redYard = node;
            }
        }


        JFrame frame = new JFrame();

        if(pidInputBypass){
            pid = new PID();
        } else {

            int n = JOptionPane.showConfirmDialog(frame,
                    "Do you want to use the default values for Ki and Kp or insert your own?",
                    "PID options",
                    JOptionPane.YES_NO_OPTION);
            if(n == 1){
                String str = (String)JOptionPane.showInputDialog(frame,
                            "Please enter your desired values for Ki and Kp seperated by a semicolon",
                            "ki and kp input",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                        null);
                String []ks = str.split(";");
                Double kI = Double.parseDouble(ks[0]);
                Double kP = Double.parseDouble(ks[1]);
                pid = new PID(kI, kP);
            }
        }

        kp = pid.getKP();

    }



    public PID getPid(){
        return pid;
    }

    public void setPID(PID new_pid){
        pid = new_pid;

    }



    public void set_power_out(Double power){
        this.power_out = power;
    }

    public void setBeacon(int beacon){
        this.beacon = beacon;

        int type = beacon & 1073741824;

        if (type == 0)
        {
            int block = beacon &1072693248;
            block = block >> 20;
            currentBlock = block;

        }
        else
        {
            int stationOne = beacon & 1056964608;
            stationOne = stationOne >> 24;
            int doorsOne = stationOne & 1;
            stationOne  = stationOne >> 1;

            int stationTwo = beacon & 16515072;
            stationTwo = stationTwo >> 18;
            int doorsTwo = stationTwo & 1;
            stationTwo = stationTwo >> 1;

            int id = -1;
            Node node = greenYard;
            while (id != stationOne){
                node = node.nextStation;
                id = node.getStationNum();

            }

            if(node.getID() == previousBlock){
                node = node.getNext();
                RIS = node.getName();
                //oldID = node.getPrev().getID();
                //newID = currentBlock
                //searchNode = currentBlock
                //int id;
                //while(searchNode != node.getID())
                //  distToStation += getBlockLength(searchNode.getID())
                //  searchNoce = getNextBlock(oldID, newID);
                //  oldID = newID;
                //  newID = searchNode;
                //

            }
            else
            {
                arriving = true;
            }




        }
    }




    public Node addNode(Node node, String name, int block){
        Node newNode = new Node(name, block, node);

        node.nextStation = newNode;
        return newNode;

    }

    public void set_authority(Double a){

        this.authority = a;

    }

    public double getPower(){
        return power_out;
    }



    public Double get_power_out(double period){
        if (brake || emergency_brake)
        {
            power_out =0.0;
        }
        else
        {
            power_out = pid.getPower(current_velocity, setpoint_velocity, period);
        }
        return power_out;
    }

    public void calcSetpointVelocity(Double v){

        this.commanded_velocity = v;

        if(v < current_velocity){
            brake = true;

        }

        if(v > speedLimit){

            this.commanded_velocity = speedLimit;

        } else;{
            this.commanded_velocity = v;
        }

        this.setpoint_velocity = this.commanded_velocity;

    }



    public void update_velocity(Double v){

        this.current_velocity = v;

    }






    public void open_left_doors(){

       doorsOpenLeft = true;

    }



    public void open_right_doors(){

        doorsOpenRight = true;

    }



    public void close_doors(){

        doorsOpenLeft = false;
        doorsOpenRight = false;

    }

    public boolean getServiceBrake(){
        return brake;
    }







    public void setEBrake( boolean brake){

        this.emergency_brake = brake;

    }

    public boolean getEBrake(){
        return this.emergency_brake;
    }

    public void setCabinTemp(Double temp){
        this.cabinTemp = temp;
    }

    public Double getCabinTemp(){
        return this.cabinTemp;
    }



    public void nextBlock(){

    }

    public void setUnderground(Boolean underground){
        this.lights = underground;
    }

    public void setStoppingDistance(){

        Double stopTime  = this.current_velocity/9663.5648;

        stoppingDistance = current_velocity * stopTime - 9663.5648 *.5 * Math.pow(stopTime,2);



    }

    public void checkStopping(){
        brake = (authority<= stoppingDistance);
    }

    public void brakeTrain(double period){
        current_velocity = current_velocity + (-1.2 *period);

    }

    public void update() {


        //Calculate elapsed time since last update

        double deltaTmillis = System.currentTimeMillis() - previousTime;

        previousTime = System.currentTimeMillis();

        if(brake) {

            double period = deltaTmillis/1000;

            brakeTrain(period);

        }else {


        power_out = get_power_out(deltaTmillis);

        //Calculate new velocity and speed

        current_velocity = current_velocity + (((prevAcceleration + acceleration) / 2) * (deltaTmillis / 1000)); // Average previous two accelerations, multiply by deltaT and add to existing velocity

        speed = Math.abs(current_velocity);


        //Calculate forces
        double mass = 37096;

        double frictionForce = mass * 9.8 * .57 * Math.cos(Math.toRadians(0)); // We've got to convert this to degrees

        double brakingAcceleration = 0;

        if (emergency_brake) {
            brakingAcceleration = -2.37;
        } else if (brake) {
            brakingAcceleration = -1.2;
        }

        double brakingForce = brakingAcceleration * mass;

        double gradeForce = -(mass * 9.8 * Math.sin(Math.toRadians(0))); //Negative here as a positive grade will reduce forward force

        double powerForce = Math.sqrt((power_out * mass * 2) / (deltaTmillis / 1000)); // I'll explain whats going on here in lecture


        double staticForce = frictionForce + brakingForce;

        double dynamicForce = powerForce + gradeForce;



        //One last velocity check

        if (current_velocity < 0 && staticForce > dynamicForce)

        {

            current_velocity = 0.0;

        }


        double netForce = 0.0;
        //Calculate net force

        if (current_velocity > 0)                                //Forward movement

        {
            System.out.println("Here");
            netForce = dynamicForce - staticForce;

        } else if (current_velocity < 0 && gradeForce < 0)        //Backwards movement

        {

            netForce = dynamicForce + staticForce;

        } else if (current_velocity == 0)

        {


            if (dynamicForce > staticForce)              //Acceleration

            {

                netForce = dynamicForce - staticForce;


            } else if (dynamicForce <= staticForce)

            {


                if (Math.abs(gradeForce) > staticForce)  //Downhill roll from standstill

                {


                    netForce = dynamicForce + staticForce;

                } else                                    //Standstill

                {

                    netForce = 0;

                }

            }

        }


        //Calculate new acceleration

        prevAcceleration = acceleration;
        System.out.println("power is " + Double.toString(power_out));
        System.out.println("power force is " + Double.toString(powerForce));
        System.out.println("friction force is " + Double.toString(frictionForce));


        acceleration = netForce / mass;





    }


    }





}

class Node{

    String station;
    int block;
    Node previousStation;
    Node nextStation;
    int stationNum;

    public Node(String station, int block, Node prev){

        this.station = station;
        this.block = block;
        this.previousStation = prev;

    }

    public void addNext(Node next){
        this.nextStation = next;
    }

    public void setStationNum(int num){
        this.stationNum =num;
    }

    public int getStationNum(){
        return this.stationNum;
    }

    public Node getNext(){
        return  this.nextStation;
    }

    public int getID(){
        return this.block;
    }

    public String getName(){
        return this.station;
    }

    public Node getPrev(){
        return this.previousStation;
    }
}
