package TrainModel;

import java.util.*;

public class TrainModel{

    //Initialize Track Model Interface
    ITrackModelForTrainModel track; //TODO: Fix this

    //Initialize array of trains
    private ArrayList<Train> trains;

    //Initialize ID
    private int ID;

    public TrainModel(ITrackModelForTrainModel track)
    {
        //Assign track
        this.track = track; //TODO: NEED TO WRITE INTERFACE!!!

        //Create trains ArrayList
        trains = new ArrayList<Train>(25);

        //ID of trains will begin at 1
        ID = 1;


    }

    public int createTrain(int previousBlock, int currentBlock, int cars, boolean setupPID) //This constructor should probably take a track
    {
        //Create Train
        Train train = new Train(previousBlock, currentBlock, cars, setupPID, ID, track);

        //Add train to trains
        trains.add(train);

        //Increment ID
        ID++;

        //Return Train ID
        return ID - 1;
    }

    public Train getTrain(int ID)
    {
        //Get iterator for trains
        Iterator<Train> itTrains = trains.iterator();

        //Check trains for train by ID
        Train temp = null;
        while(itTrains.hasNext())
        {
            temp = itTrains.next();

            if(temp.getID() == ID)
            {
                break;
            }
        }

        return temp;
    }

    public boolean deleteTrain(int ID)
    {
        //Get iterator for trains
        Iterator<Train> itTrains = trains.iterator();

        //Check trains for train by ID
        Train temp = null;
        while(itTrains.hasNext())
        {
            temp = itTrains.next();

            if(temp.getID() == ID)
            {
               trains.remove(temp);
               return true;
            }
        }

        return false;
    }

    public void updateTrains()
    {
        //Get iterator for trains
        Iterator<Train> itTrains = trains.iterator();

        //Update each train iteratively
        while(itTrains.hasNext())
        {
            itTrains.next().update();
        }
    }

}

