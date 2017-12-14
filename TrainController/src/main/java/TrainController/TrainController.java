package TrainController;

import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import com.sun.org.apache.bcel.internal.generic.SIPUSH;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Skin;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

public class TrainController {

    SimpleBooleanProperty lightsProperty;
    SimpleBooleanProperty leftOpenDoorProperty;
    SimpleBooleanProperty rightOpenDoorProperty;
    SimpleBooleanProperty emergencyBrakeProperty;
    SimpleBooleanProperty serviceBrakeProperty;
    SimpleDoubleProperty currentVelocityProperty;
    SimpleDoubleProperty setpointVelocityProperty;
    SimpleDoubleProperty authorityProperty;
    SimpleDoubleProperty powerProperty;
    SimpleStringProperty cabinTempProperty;
    SimpleBooleanProperty autoModeProperty;
    double prevAcceleration;
    double acceleration;
    double desiredCabinTemp;
    List<SkinnyBlock> track;
    int previousStation;

    double commandedVelocity;

    double previousTime;

    SimpleStringProperty doorSide;

    double switchBeaconNext;
    double input_velocity;
    double distInBlock;

    double distToStop;
    int previousBlock;
    double kp;
    PID pid;
    double cabinTemp;
    String [] stations = new String[226];
    boolean departing;
    String nextStation;

    boolean stopped;
    double speedLimit;
    double stoppingDistance;
    double speed;
    int beacon;
    boolean knowStop = false;

    String RIS;
    boolean arriving;
    int door;
    String name;
    int ID;
    SkinnyBlock currentSkinnyBlock;
    boolean stopping;
    int stopWait = 0;



    public TrainController(boolean pidInputBypass, int previous, int current, int ID, ITrackModelForTrainController trackInterface, Line line){

        lightsProperty = new SimpleBooleanProperty();
        leftOpenDoorProperty =new SimpleBooleanProperty();
        rightOpenDoorProperty =new SimpleBooleanProperty();
        emergencyBrakeProperty=new SimpleBooleanProperty();
        serviceBrakeProperty=new SimpleBooleanProperty();
        currentVelocityProperty = new SimpleDoubleProperty();
        setpointVelocityProperty = new SimpleDoubleProperty();
        authorityProperty = new SimpleDoubleProperty();
        powerProperty= new SimpleDoubleProperty();
        cabinTempProperty = new SimpleStringProperty();
        autoModeProperty = new SimpleBooleanProperty(true);
        doorSide = new SimpleStringProperty("");



        switchBeaconNext =0;
        this.ID = ID;

        List<Block> trackInput = trackInterface.getBlocks(line);
        stopped = false;

        this.name = "Train Controller " + Integer.toString(ID);
        prevAcceleration = 0;
        acceleration=0;


        previousBlock = previous;
        stopping = false;


        leftOpenDoorProperty.setValue(false);
        rightOpenDoorProperty.setValue(false);


        emergencyBrakeProperty.setValue(false);


        serviceBrakeProperty.setValue(false);


        currentVelocityProperty.set(0.0);

        powerProperty.set(0.0);
        previousTime = System.currentTimeMillis();


        input_velocity  = 0.0;

        authorityProperty.set(0.0*1.09);
        distInBlock=0.0;



        this.cabinTemp = 67.0;
        this.desiredCabinTemp= 67.0;
        cabinTempProperty.set("Cabin temp is " + cabinTemp);

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
        for (int i =0; i<trackInput.size();i++){
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
        speedLimit=Double.MAX_VALUE;

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

        RIS = "Next station will be announced shortly";

    }


    public void updateVelocity(double velocity){
        //System.out.println("update stopped="+stopped + "stopwait is " + stopWait + " velocity " + velocity);
        //System.out.println("authority is " + authorityProperty.getValue() + " brake is " + serviceBrakeProperty.getValue() + " dist in block is " + distInBlock);
        if(stopped && velocity==0){
            stopAtStation();
            stopWait++;
            serviceBrakeProperty.setValue(true);


        }
        setStoppingDistance();
        checkStopping();

        currentVelocityProperty.setValue(velocity);
    }

    public void setUnderground(boolean underground){

        lightsProperty.setValue(underground);
    }

    public boolean isStoppedAtStation(){
        if (stopped && stopWait == 120) {
            stopped = false;
            stopWait = 0;
            close_doors();
            return true;
        }

        return false;
    }

    public void stopAtStation()
    {
       
        serviceBrakeProperty.setValue(true);
        if(autoModeProperty.getValue()){
            if(door==1){
                open_left_doors();
            }else{
                open_right_doors();
            }
        }

        arriving=false;
    }

    public void nextBlock()
    {
        System.out.println("Next Block");

        if(arriving){
            arriving =false;
            stopped = true;
            RIS = "Now arriving at " + nextStation;
        } else{
            stopped=false;
        }
        distInBlock=0.0;
        if(departing) {

            departing = false;
        }
        if (!knowStop) {
          RIS = "Next station will be announced shortly";
        }

        int prev = currentSkinnyBlock.getID();
        int next = currentSkinnyBlock.getNext();
        System.out.println("Moving to block " + next);
        if(next >=0)
        {
            currentSkinnyBlock = track.get(next);
            currentSkinnyBlock.setPrev(prev);
        }
        speedLimit = currentSkinnyBlock.getSpeedLimit();
        //System.out.println("Current block is " + currentSkinnyBlock.getID() + " The previous block was " +prev);
        if(currentSkinnyBlock.station && !stopped){
            RIS = "Now Passing " + stations[currentSkinnyBlock.getID()];
        }

    }



    public void set_power_out(double power){
        powerProperty.set(power);
    }

    public void setBeacon(int beacon) {
        if(beacon != 0){

            this.beacon = beacon;
            int type = beacon & 1073741824;
            type = type >> 30;
            if(type == 1){

                int station = beacon & 536870912;
                station = station >> 29;
                if(station == 1){

                    int block = beacon & 534773760;
                    block = block >>21;
                    currentSkinnyBlock = track.get(block);

                    arriving = true;
                    if(authorityProperty.getValue() <= currentSkinnyBlock.getLength()){
                        arriving = false;
                        stopped = true;
                        knowStop=true;
                        nextStation = stations[currentSkinnyBlock.getID()];
                        RIS = "Now Arriving at " + nextStation;
                    }
                } else {
                    knowStop=false;
                    int block = beacon & 534773760;
                    block = block >>21;
                    currentSkinnyBlock = track.get(block);
                    double searchAuthority = authorityProperty.getValue();
                    int searchBlock =currentSkinnyBlock.getID();
                    searchAuthority -= distInBlock;


                }

            }else {
                int switchBeacon = beacon & 1;
                int stationOne= beacon&1069547520;
                stationOne = stationOne >> 22;
                int stationTwo=beacon &2088960;
                stationTwo = stationTwo >>13;
                int doors = beacon&2097152;

                if(doors==0){
                    door = 0;
                    doorSide.setValue("Right Side");
                }else{
                    door=1;
                    doorSide.setValue("Left Side");
                }
                if(switchBeacon==1){
                    int block = beacon&8160;
                    block = block >> 5;
                    currentSkinnyBlock = track.get(block);
                }
                //System.out.println("Station two " + stationTwo + " connected blocks " + currentSkinnyBlock.getConnected());
                if(currentSkinnyBlock.getConnected().contains(stationTwo)){
                   /* System.out.println("Station beacon authority is " + authorityProperty.getValue() + " distance to end of station " +
                            ( currentSkinnyBlock.getLength()+track.get(stationTwo).getLength()) );*/
                    if(authorityProperty.getValue() ==( currentSkinnyBlock.getLength()+track.get(stationTwo).getLength())){

                        arriving = true;
                        nextStation = stations[stationTwo];
                        RIS = "Approaching Stop at " + nextStation;
                        knowStop = true;
                    }else{
                        arriving = false;
                        knowStop=false;
                        RIS = "Approaching " + stations[stationTwo];
                        /*double searchAuthority = authorityProperty.getValue();
                        int searchBlock =currentSkinnyBlock.getID();
                        searchAuthority -= distInBlock;
                        knowStop=false;
                        while(searchAuthority>0 && searchBlock != -1 && searchBlock != -2 ){
                            searchBlock=track.get(searchBlock).getNext();
                            if(searchBlock != -1 && searchBlock != -2){
                                searchAuthority -= track.get(searchBlock).getLength();
                                if(searchAuthority <= 0){
                                    nextStation = stations[searchBlock];
                                    knowStop = true;
                                    System.out.println("station for next stop is " + searchBlock);
                                }
                            }
                        }*/
                    }
                }else{
                    departing = true;
                    arriving=false;
                    knowStop=false;
                    RIS = "Now departing " +  stations[stationOne];
                    /*double searchAuthority = authorityProperty.getValue();
                    int searchBlock =currentSkinnyBlock.getID();
                    searchAuthority -= distInBlock;
                    knowStop=false;
                    while(searchAuthority>0 && searchBlock != -1 && searchBlock != -2 ){
                        searchBlock=track.get(searchBlock).getNext();
                        if(searchBlock != -1 && searchBlock != -2){
                            searchAuthority -= track.get(searchBlock).getLength();
                            if(searchAuthority <= 0){
                                nextStation = stations[searchBlock];
                                knowStop = true;
                                System.out.println("station for next stop is " + searchBlock);
                            }
                        }
                    }*/
                }

            }
        }
    }



    public void setAuthority(double a)
    {

        authorityProperty.set(a);
        if(knowStop)
        {

        }

    }

    public boolean getLights(){
        return lightsProperty.getValue();
    }

    public double getPower()
    {

        return powerProperty.getValue();
    }

    public double getPower(double period){
        if (serviceBrakeProperty.getValue() || emergencyBrakeProperty.getValue())
        {
            powerProperty.set(0.0);

        }
        else
        {
            double p = pid.getPower(currentVelocityProperty.getValue(), setpointVelocityProperty.getValue(), period);
            powerProperty.set(p);
        }
        distInBlock += currentVelocityProperty.getValue()*(period/1000);
        //System.out.println("Controller Velocity="+currentVelocity +" Power="+getPower() + " Setpoint="+setpointVelocity +" Distance="+distInBlock+" Brake="+brake);
       // System.out.println("Controller x="+(authority-distInBlock) + " Stop Dist="+stoppingDistance + " Authority="+authority);
        //System.out.println("Controller e brake" + emergencyBrakeProperty.getValue() + " Controller left door =" + leftOpenDoorProperty.getValue() + " Controller right door " + rightOpenDoorProperty.getValue() + " Cabin Temp " + cabinTemp);
        //System.out.println(RIS + " Current block is " + currentSkinnyBlock.getID() + " previous block is " + currentSkinnyBlock.previous + " The connected blocks are " );
       /* for(int i = 0; i< currentSkinnyBlock.getConnected().size(); i++){
            System.out.print(" "+currentSkinnyBlock.getConnected().get(i) + " ");
        }*/
        //System.out.println("Controller x="+(authority-distInBlock) + " Stop Dist="+stoppingDistance + " Authority="+authority);

        return powerProperty.getValue();
    }

    public void setDesiredCabinTemp(double temp){
        this.desiredCabinTemp = temp;
    }


    public void calcSetpointVelocity(double v){

        this.commandedVelocity = v;
        if(v < currentVelocityProperty.getValue()){

            serviceBrakeProperty.setValue(true);
        }

        if(v > speedLimit){
            this.commandedVelocity = speedLimit;

        } else;{
            this.commandedVelocity = v;
        }

        setpointVelocityProperty.set(commandedVelocity);
    }

    public SimpleBooleanProperty getLightsProperty(){
        return lightsProperty;
    }

    public SimpleBooleanProperty getAutoModeProperty() {
        return autoModeProperty;
    }

    public SimpleBooleanProperty getEmergencyBrakeProperty() {
        return emergencyBrakeProperty;
    }

    public SimpleBooleanProperty getLeleftOpenDoorProperty() {
        return leftOpenDoorProperty;
    }

    public SimpleBooleanProperty getRightOpenDoorProperty() {
        return rightOpenDoorProperty;
    }

    public SimpleBooleanProperty getServiceBrakeProperty() {
        return serviceBrakeProperty;
    }

    public SimpleDoubleProperty getAuthorityProperty() {
        return authorityProperty;
    }

    public SimpleStringProperty getCabinTempProperty() {
        return cabinTempProperty;
    }

    public SimpleDoubleProperty getCurrentVelocityProperty() {
        return currentVelocityProperty;
    }

    public SimpleDoubleProperty getPowerProperty() {
        return powerProperty;
    }

    public SimpleDoubleProperty getSetpointVelocityProperty() {
        return setpointVelocityProperty;
    }
    public SimpleStringProperty getDoorSide(){
        return doorSide;
    }

    public void open_left_doors()
    {

        leftOpenDoorProperty.setValue(true);
    }



    public void open_right_doors()
    {

        rightOpenDoorProperty.setValue(true);
    }



    public void close_doors(){

        leftOpenDoorProperty.setValue(false);
        rightOpenDoorProperty.setValue(false);
    }

    public boolean getServiceBrake(){
        return serviceBrakeProperty.getValue();
    }





    public void setEmergencyBrake(){

        emergencyBrakeProperty.setValue(true);
    }



    public boolean getEmergencyBrake(){
        return emergencyBrakeProperty.getValue();
    }

    public void setCabinTemp(double temp){
        this.cabinTemp = temp;
        cabinTempProperty.set("Cabin temp is " + cabinTemp);
    }

    public double getCabinTemp(){
        return this.desiredCabinTemp;
    }

    public String getRIS(){
        return RIS;
    }

    public boolean getLeftDoors()
    {
        return  leftOpenDoorProperty.getValue();
    }

    public boolean getRightDoors()
    {
        return  rightOpenDoorProperty.getValue();
    }



    public void setUnderground(Boolean underground){
        lightsProperty.setValue(underground);
    }

    public void setStoppingDistance(){
        if(!serviceBrakeProperty.getValue()) {
            stoppingDistance = Math.pow(currentVelocityProperty.getValue(), 2) / 2.4;
            stoppingDistance +=15;
            stoppingDistance *=1.2;
        }
    }

    public void checkStopping(){
        if(authorityProperty.getValue() ==0 && distInBlock ==0 && currentVelocityProperty.getValue()==0 ){
        }else {
            double travelDistance = currentVelocityProperty.getValue()*1.5;
            if(stopped){
                serviceBrakeProperty.setValue(true);
            } else {
                serviceBrakeProperty.setValue(((authorityProperty.getValue() - (distInBlock + travelDistance)) <= stoppingDistance));
            }

        }
    }


    public void brakeTrain(double period){
        currentVelocityProperty.set(currentVelocityProperty.getValue() + (-1.2 *period));
    }



}