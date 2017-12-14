//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackModel.Events;

import java.util.EventObject;

public class SwitchStateManualChangeEvent extends EventObject
{
    public SwitchStateManualChangeEvent(Object source)
    {
        super(source);
    }
}