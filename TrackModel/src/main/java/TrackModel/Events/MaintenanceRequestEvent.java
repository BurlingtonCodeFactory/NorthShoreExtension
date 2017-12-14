//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package TrackModel.Events;

import java.util.EventObject;

public class MaintenanceRequestEvent extends EventObject
{
    public MaintenanceRequestEvent(Object source)
    {
        super(source);
    }
}

