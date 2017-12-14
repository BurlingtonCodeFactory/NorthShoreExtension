package TrainModel;

import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.Models.Line;
import TrainController.TrainController;
import TrainController.TrainControllerManager;
import TrainModel.Interfaces.ITrainModelForCTCOffice;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.stage.Stage;

import java.util.*;

@Singleton
public class TrainModel implements ITrainModelForCTCOffice{

    //Initialize Track Model Interface
    ITrackModelForTrainModel track; //TODO: Fix this

    ITrackModelForTrainController trackModelForTrainController;

    //Initialize array of trains
    private ArrayList<Train> trains;

    //Initialize associated train controller manager
    private TrainControllerManager controllerManager;

    //Initialize ID
    private int ID;

    @Inject
    public TrainModel(ITrackModelForTrainModel track, ITrackModelForTrainController trackModelForTrainController)
    {
        //Assign track
        this.track = track;

        //Assign train controller manager
        this.controllerManager = new TrainControllerManager();

        //Create trains ArrayList
        trains = new ArrayList<Train>(30);

        //ID of trains will begin at 1
        ID = 1;

        this.trackModelForTrainController = trackModelForTrainController;

        controllerManager.launch();
    }

    public int createTrain(int previousBlock, int currentBlock, int cars, boolean PIDSetupBypass, Line line) //This constructor should probably take a track
    {
        //Create train controller
        TrainController trainController = new TrainController(PIDSetupBypass, previousBlock, currentBlock, ID, trackModelForTrainController, line);
        controllerManager.addTrainController(trainController);
        //Create train
        Train train = new Train(previousBlock, currentBlock, cars, trainController, PIDSetupBypass, ID, track, line);

        System.out.println("Here Train Model Create");
        TrainGUI gui = new TrainGUI(train);
        gui.start(new Stage());

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

    public void updateTrains(double elapsedTime)
    {
        //Get iterator for trains
        Iterator<Train> itTrains = trains.iterator();

        //Update each train iteratively
        while(itTrains.hasNext())
        {
            itTrains.next().update(elapsedTime);
        }
    }

}

