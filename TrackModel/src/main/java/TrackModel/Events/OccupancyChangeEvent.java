package TrackModel.Events;

import java.util.EventObject;

public class OccupancyChangeEvent extends EventObject{

    public OccupancyChangeEvent(Object source)
    {
        super(source);
    }
}
