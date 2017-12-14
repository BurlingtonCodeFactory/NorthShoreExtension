//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackModel.Events;

import java.util.EventObject;

public class MaintenanceChangeEvent extends EventObject
{
    public MaintenanceChangeEvent(Object source)
    {
        super(source);
    }
}
