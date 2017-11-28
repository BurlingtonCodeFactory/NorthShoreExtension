package TrackController;

import TrackController.Models.TrackController;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Models.Line;
import TrackModel.TrackModel;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackControllerManager {
    public List<TrackController> redControllers;
    public List<TrackController> greenControllers;
    private HashMap<Integer, TrackController> redMapping;
    private HashMap<Integer, TrackController> greenMapping;
    private ITrackModelForTrackController track;
    private final int[] REDCONTROLLERBLOCKS = {15};
    private final int[] GREENCONTROLLERBLOCKS = {150};

    public TrackControllerManager() //TODO: Inject Track Model and create controllers
    {
        greenControllers = new ArrayList<TrackController>();
        track = new TrackModel();
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
        //TrackControllerGUI gui = new TrackControllerGUI();
    }

    public boolean handleEvent()
    {
        for (TrackController controller : greenControllers) {
            controller.evaluateBlocks();
        }
        return true;
    }
}
