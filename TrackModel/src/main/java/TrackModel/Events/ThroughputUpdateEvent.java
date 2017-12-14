package TrackModel.Events;

import java.util.EventObject;

public class ThroughputUpdateEvent extends EventObject {
    public ThroughputUpdateEvent(Object source)
    {
        super(source);
    }
}
