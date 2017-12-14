package TrackModel.Events;

import java.util.EventObject;

public class ClockTickUpdateEvent extends EventObject
{
    public ClockTickUpdateEvent(Object source)
    {
        super(source);
    }
}
