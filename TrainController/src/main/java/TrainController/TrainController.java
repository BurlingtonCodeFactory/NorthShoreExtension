package TrainController;

import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Models.Block;
import TrackModel.Models.Line;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class TrainController {

    double prevAcceleration;
    double acceleration;
    List<SkinnyBlock> track;
    double current_velocity;
    double commanded_velocity;
    double authority;
    double previousTime;
    boolean auto;
    boolean emergency_brake;
    boolean brake;
    boolean doorsOpenLeft;
    boolean doorsOpenRight;
    boolean lights;
    double power_out;
    double input_velocity;
    double distInBlock;
    double setpoint_velocity;
    int previousBlock;
    double kp;
    PID pid;
    double cabinTemp;
    String [] stations = new String[226];
    boolean departing;
    String nextStation;
    double blockLength;
    double distToStation;
    boolean stopping;
    double speedLimit;
    double stoppingDistance;
    double speed;
    int beacon;
    SkinnyBlock greenYard;
    SkinnyBlock redYard;
    String RIS;
    boolean arriving;
    int searchBlock;
    String name;
    int ID;
    SkinnyBlock currentSkinnyBlock;



    public TrainController(boolean pidInputBypass, int previous, int current, int ID, ITrackModelForTrainController trackInterface, Line line){

        System.out.println("Creating train controller "+ID);

        this.ID = ID;

        List<Block> trackInput = trackInterface.getBlocks(line);


        this.name = "Train Controller " + Integer.toString(ID);
        prevAcceleration = 0;
        acceleration=0;


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
        distInBlock=0.0;


        this.speedLimit = Double.MAX_VALUE;
        this.cabinTemp = 67.0;

        if(line.equals(Line.GREEN)){
            stations[0] = "green yard";
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
            stations[105] = "Dormont";
            stations[114]="Glenbury";
            stations[123]="Overbrook";
            stations[132]="Inglewood";
            stations[141]="Central";
        }else{
            stations[0] = "red yard";
            stations[150] = "Shadyside";
            stations[16] = "Herron Ave";
            stations[21] = "Swissville";
            stations[25] = "Penn Station";
            stations[35] = "Steel Plaza";
            stations[15] ="First Ave";
            stations[48] = "Station Square";
            stations[60] = "South Hills Junction";
        }




        track = new ArrayList<SkinnyBlock>();
        boolean station = false;
        for (int i =0; i<stations.length;i++){
            Block block = trackInput.get(i);

            if(stations[i] != null){
                station = true;
            }

            SkinnyBlock skinny = new SkinnyBlock( block.getLength(), block.getSpeedLimit(), station, block.getId(), block.getConnectedBlocks());

            if(i == current){
                 currentSkinnyBlock = skinny;
            }
            track.add(i, skinny);
            station=false;
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
                double kI = Double.parseDouble(ks[0]);
                double kP = Double.parseDouble(ks[1]);
                pid = new PID(kI, kP);
            }
        }

        kp = pid.getKP();

    }


    public void updateVelocity(double velocity){
        current_velocity = velocity;
        setStoppingDistance();
        checkStopping();
    }

    public void setUnderground(boolean underground){
        lights = underground;
    }

    public void nextBlock()
    {
        distInBlock=0.0;
        if(departing){
            RIS = "Next station is " + nextStation;
        }
        /*SkinnyBlock skinnyBlock;
        if(track.get(current).getPrev().ID == previous) {
             skinnyBlock = track.get(current).getNext();
        } else {
            skinnyBlock = track.get(current).getPrev();
        }
        return skinnyBlock.ID;*/
    }



    public void set_power_out(double power){
        this.power_out = power;
    }

    public void setBeacon(int beacon){
        this.beacon = beacon;
        int type = beacon & 1073741824;
        if (type == 0)
        {
            int block = beacon &1072693248;
            block = block >> 20;
            currentSkinnyBlock = track.get(block);

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
            SkinnyBlock skinnyBlock = greenYard;

            while (id != stationOne){
                skinnyBlock = skinnyBlock.next;
                id = skinnyBlock.getID();

            }

            if(skinnyBlock.getID() == previousBlock){
                skinnyBlock = skinnyBlock.getNext();
                double dist = skinnyBlock.getDistToStation(stationTwo, skinnyBlock);
                setAuthority(dist);
                departing = true;
                nextStation = stations[stationTwo];
                RIS = "Now departing " + stations[stationOne];
                //oldID = skinnyBlock.getPrev().getID();
                //newID = currentSkinnyBlock
                //searchNode = currentSkinnyBlock
                //int id;
                //while(searchNode != skinnyBlock.getID())
                //  distToStation += getBlockLength(searchNode.getID())
                //  searchNoce = getNextBlock(oldID, newID);
                //  oldID = newID;
                //  newID = searchNode;
                //
            }
            else
            {
                arriving = true;
                RIS = "Arriving at " +stations[stationOne];
            }
        }
    }




    public SkinnyBlock addBlock(SkinnyBlock skinnyBlock, boolean station, int blockID, double speedLimit, double blockLength){
        SkinnyBlock newSkinnyBlock = new SkinnyBlock(blockLength, speedLimit, skinnyBlock, station, blockID);
        skinnyBlock.next= newSkinnyBlock;
        return newSkinnyBlock;
    }

    public void setAuthority(double a){this.authority = a;}

    public boolean getLights(){
        return lights;
    }

    public double getPower(){
        return power_out;
    }

    public double getPower(double period){
        if (brake || emergency_brake)
        {
            power_out =0.0;
        }
        else
        {
            power_out = pid.getPower(current_velocity, setpoint_velocity, period);
        }
        distInBlock += current_velocity*(period/1000);
       // System.out.println("Controller Velocity="+current_velocity +" Power="+getPower() + " Setpoint="+setpoint_velocity +" Distance="+distInBlock+" Brake="+brake);
       // System.out.println("Controller x="+(authority-distInBlock) + " Stop Dist="+stoppingDistance + " Authority="+authority);
        //System.out.println("Controller e brake" + emergency_brake + " Controller left door =" + doorsOpenLeft + " Controller right door " + doorsOpenRight + " Cabin Temp " + cabinTemp);
        return power_out;
    }

    public void calcSetpointVelocity(double v){

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









    public void open_left_doors(){doorsOpenLeft = true;}



    public void open_right_doors(){doorsOpenRight = true;}



    public void close_doors(){
        doorsOpenLeft = false;
        doorsOpenRight = false;
    }

    public boolean getServiceBrake(){
        return brake;
    }





    public void setEmergencyBrake(){
        emergency_brake = true;
    }

    public void setEBrake( boolean brake){
        this.emergency_brake = brake;
    }

    public boolean getEmergencyBrake(){
        return this.emergency_brake;
    }

    public void setCabinTemp(double temp){
        this.cabinTemp = temp;
    }

    public double getCabinTemp(){
        return this.cabinTemp;
    }

    public String getRIS(){
        return RIS;
    }

    public boolean getLeftDoors()
    {
        return  doorsOpenLeft;
    }

    public boolean getRightDoors()
    {
        return  doorsOpenRight;
    }

    public void setSetpointVelocity(double vel){
        this.setpoint_velocity = vel;
    }

    public void setUnderground(Boolean underground){
        this.lights = underground;
    }

    public void setStoppingDistance(){
        if(!brake) {
            stoppingDistance = Math.pow(current_velocity, 2) / 2.4;
            stoppingDistance +=15;
            stoppingDistance *=1.2;
        }
    }

    public void checkStopping(){
        if(authority ==0 && distInBlock ==0 && current_velocity==0 ){
        }else {
            double travelDistance = current_velocity*1.5;
            brake = ((authority - (distInBlock + travelDistance)) <= stoppingDistance);
        }
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
        }
        else
        {
            power_out = getPower(deltaTmillis);

            //Calculate new velocity and speed
            current_velocity = current_velocity + (((prevAcceleration + acceleration) / 2) * (deltaTmillis / 1000)); // Average previous two accelerations, multiply by deltaT and add to existing velocity
            speed = Math.abs(current_velocity);

            //Calculate forces
            double mass = 37096;
            double frictionForce = mass * 9.8 * .001 * Math.cos(Math.toRadians(0)); // We've got to convert this to degrees
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
                    }
                    else                                    //Standstill
                    {
                        netForce = 0;
                    }
                }
            }

            //Calculate new acceleration
            prevAcceleration = acceleration;
            acceleration = netForce / mass;
        }
    }

}


