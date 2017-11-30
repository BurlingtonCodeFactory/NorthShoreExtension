package TrackController;

import TrackController.Events.RefreshUIEvent;
import TrackController.Events.RefreshUIListener;
import TrackController.Models.TrackController;
import TrackModel.Events.*;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import TrackModel.TrackModel;
import javafx.application.Application;
import javafx.application.Platform;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Singleton
public class TrackControllerManager implements OccupancyChangeListener, SuggestedSpeedChangeListener, SuggestedAuthorityChangeListener, FailureChangeListener {
    public List<TrackController> redControllers;
    public List<TrackController> greenControllers;
    private HashMap<Integer, TrackController> redMapping;
    private HashMap<Integer, TrackController> greenMapping;
    private ITrackModelForTrackController track;
    private final int[] REDCONTROLLERBLOCKS = {15};
    private final int[] GREENCONTROLLERBLOCKS = {150};
    private final ArrayList<Integer> LOCKS = new ArrayList<>(Arrays.asList(29,76));
    private static ArrayList<RefreshUIListener> listeners = new ArrayList<>();

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
                if(LOCKS.contains(i))
                {
                    System.out.println("Creating lock on "+i);
                    track.getBlock(Line.GREEN, i).createLock();
                }
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
        //System.out.println("Handling occupancy change in Track Controller ");
        runRules();
        fireRefreshUIEvent(this);
    }

    public void suggestedSpeedChangeReceived(SuggestedSpeedChangeEvent event)
    {
        //System.out.println("Handling speed change in Track Controller");
        runRules();
        fireRefreshUIEvent(this);
    }

    public void suggestedAuthorityChangeReceived(SuggestedAuthorityChangeEvent event)
    {
        //System.out.println("Handling authority change in Track Controller");
        runRules();
        fireRefreshUIEvent(this);
    }

    public void failureChangeReceived(FailureChangeEvent event)
    {
        //System.out.println("Handling failure change in Track Controller");
        runRules();
        fireRefreshUIEvent(this);
    }

    // Occupancy Change
    public static synchronized void addRefreshUIListener( RefreshUIListener l ) {
        //System.out.println("Adding refresh change listener " + l.getClass());
        listeners.add( l );
    }

    public static synchronized void removeRefreshUIListener( RefreshUIListener l ) {
        listeners.remove( l );
    }

    private static synchronized void fireRefreshUIEvent(Object source)
    {
        RefreshUIEvent event = new RefreshUIEvent(source);
        for(RefreshUIListener listener : listeners)
        {
            //System.out.println("Sending refresh event to "+listener.getClass());
            Platform.runLater(()->listener.refreshUIReceived(event));
        }
    }
}
