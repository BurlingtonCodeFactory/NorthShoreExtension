//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package TrackModel.Events;

import java.util.EventObject;

public class SwitchStateChangeEvent extends EventObject
{
    public SwitchStateChangeEvent(Object source)
    {
        super(source);
    }
}
