package TrainController;

public class SkinnyBlock {

    boolean station;
    int block;
    SkinnyBlock previous;
    SkinnyBlock next;
    int ID;
    double speedLimit;
    double length;




    public SkinnyBlock(double length, double speed, SkinnyBlock prev, boolean station, int id){

        this.length = length;
        this.speedLimit = speed;

        this.previous = prev;
        this.station=true;
        this.ID = id;

    }





    public int getID(){
        return this.ID;
    }

    public SkinnyBlock getNext(){
        return  this.next;
    }

    public double getDistToStation(int stationBlock, SkinnyBlock skinnyBlock){
        double dist = skinnyBlock.length;
        while(skinnyBlock.getID() != stationBlock){
            skinnyBlock = skinnyBlock.getNext();
            if(skinnyBlock.getID() != stationBlock) {
                dist = dist + skinnyBlock.length;
            }
        }
        return dist;

    }



    public SkinnyBlock getPrev(){
        return this.previous;
    }
}
