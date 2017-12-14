//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackModel.Events;

import java.util.EventObject;

public class FailureChangeEvent extends EventObject
{

    public FailureChangeEvent(Object source)
    {
        super(source);
    }
}
