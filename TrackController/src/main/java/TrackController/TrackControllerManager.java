package TrackController;

import TrackController.Models.TrackController;
import TrackModel.Events.*;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Models.Line;
import TrackModel.TrackModel;
import javafx.application.Application;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class TrackControllerManager implements OccupancyChangeListener, SuggestedSpeedChangeListener, SuggestedAuthorityChangeListener {
    public List<TrackController> redControllers;
    public List<TrackController> greenControllers;
    private HashMap<Integer, TrackController> redMapping;
    private HashMap<Integer, TrackController> greenMapping;
    private ITrackModelForTrackController track;
    private final int[] REDCONTROLLERBLOCKS = {15};
    private final int[] GREENCONTROLLERBLOCKS = {150};

    public TrackControllerManager() //TODO: Inject Track Model and create controllers
    {
        track = TrackControllerModule.injector.getInstance(ITrackModelForTrackController.class);
        greenControllers = new ArrayList<TrackController>();
        int id = 1;
        int blockid = 0;
        for (int limit : GREENCONTROLLERBLOCKS) {
            TrackController controller = new TrackController(id, "Vital Section " + id, "file" + id + ".plc", track);
            for (int i = blockid; i <= limit; i++)
            {
                controller.addBlock(track.getBlock(Line.GREEN, i));
                blockid++;
            }
            id++;
            greenControllers.add(controller);
        }
    }

    private void runRules()
    {
        for (TrackController controller : greenControllers) {
            controller.evaluateBlocks();
        }
    }


    public void handleEvent()
    {
       runRules();
    }

    public void occupancyChangeReceived(OccupancyChangeEvent event)
    {
        System.out.println("Handling occupancy change in Track Controller");
        runRules();
    }

    public void suggestedSpeedChangeReceived(SuggestedSpeedChangeEvent event)
    {
        System.out.println("Handling speed change in Track Controller");
        runRules();
    }

    public void suggestedAuthorityChangeReceived(SuggestedAuthorityChangeEvent event)
    {
        System.out.println("Handling authority change in Track Controller");
        runRules();
    }
}
