package TrackController;

import TrackController.Models.TrackController;
import TrackModel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackControllerManager {
    public List<TrackController> redControllers;
    public List<TrackController> greenControllers;
    private HashMap<Integer, TrackController> redMapping;
    private HashMap<Integer, TrackController> greenMapping;
    private ITrackModelForTC track;
    private final int[] REDCONTROLLERBLOCKS = {15};
    private final int[] GREENCONTROLLERBLOCKS = {12};

    public TrackControllerManager() //TODO: Inject Track Model and create controllers
    {
        greenControllers = new ArrayList<TrackController>();
        track = new Track();
        int id = 1;
        int blockid = 1;
        for (int limit : GREENCONTROLLERBLOCKS) {
            TrackController controller = new TrackController(id, "Vital Section " + id, "file" + id + ".plc");
            for (int i = blockid; i <= limit; i++)
            {
                controller.addBlock(track.getBlock(LineType.GREEN, i));
                blockid++;
            }
            id++;
            greenControllers.add(controller);
        }
    }

    public boolean handleEvent()
    {
        for (TrackController controller : greenControllers) {
            controller.evaluateBlocks();
        }
        return true;
    }
}
