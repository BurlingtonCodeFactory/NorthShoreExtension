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
public class TrackControllerManager implements OccupancyChangeListener, SuggestedSpeedChangeListener,
        SuggestedAuthorityChangeListener, FailureChangeListener, SwitchStateChangeListener, MaintenanceRequestListener{
    public List<TrackController> controllers;
    private ITrackModelForTrackController track;
    private final ArrayList<Integer> GREENLOCKS = new ArrayList<>(Arrays.asList(29,76));
    private final ArrayList<Integer> REDLOCKS = new ArrayList<>();
    private static ArrayList<RefreshUIListener> listeners = new ArrayList<>();

    public TrackControllerManager() //TODO: Inject Track Model and create controllers
    {
        track = TrackControllerModule.injector.getInstance(ITrackModelForTrackController.class);
        int id = 1;
        if(track.getBlocks(Line.GREEN).size() > 0)
        {
            controllers = new ArrayList<>();
            TrackController controller = new TrackController(id, "Vital Section " + id, "green" + id + ".plc", track);
            for (Block block : track.getBlocks(Line.GREEN))
            {
                controller.addBlock(block);
                if(GREENLOCKS.contains(block.getId()))
                {
                    System.out.println("Creating lock on "+block.getId());
                    block.createLock();
                }
            }
            controllers.add(controller);
        }
        else {
            controllers = new ArrayList<>();
            id = 1;
            TrackController controller = new TrackController(id, "Vital Section " + id, "red" + id + ".plc", track);
            for (Block block : track.getBlocks(Line.RED)) {
                controller.addBlock(block);
                if (REDLOCKS.contains(block.getId())) {
                    System.out.println("Creating lock on " + block.getId());
                    block.createLock();
                }
            }
            controllers.add(controller);
        }
    }

    private void runRules()
    {
        for (TrackController controller : controllers) {
            controller.evaluateBlocks();
        }
    }

    public ITrackModelForTrackController getTrack() {
        return track;
    }

    public void handleEvent()
    {
       runRules();
    }

    public void occupancyChangeReceived(OccupancyChangeEvent event)
    {
        runRules();
        fireRefreshUIEvent(this);
    }

    public void suggestedSpeedChangeReceived(SuggestedSpeedChangeEvent event)
    {
        runRules();
        fireRefreshUIEvent(this);
    }

    public void suggestedAuthorityChangeReceived(SuggestedAuthorityChangeEvent event)
    {
        runRules();
        fireRefreshUIEvent(this);
    }

    public void failureChangeReceived(FailureChangeEvent event)
    {
        System.out.println("Fired");
        runRules();
        fireRefreshUIEvent(this);
    }

    public void switchStateChangeReceived(SwitchStateChangeEvent event)
    {
        runRules();
        fireRefreshUIEvent(this);
    }

    public void maintenanceRequestReceived(MaintenanceRequestEvent event)
    {
        System.out.println("Fired");
        runRules();
        fireRefreshUIEvent(this);
    }

    // Occupancy Change
    public static synchronized void addRefreshUIListener( RefreshUIListener l ) {
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
            Platform.runLater(()->listener.refreshUIReceived(event));
        }
    }
}
