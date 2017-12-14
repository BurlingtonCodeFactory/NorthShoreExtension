//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackController.Events;

import java.util.EventObject;

public class RefreshUIEvent extends EventObject
{
    public RefreshUIEvent(Object source)
    {
        super(source);
    }
}
