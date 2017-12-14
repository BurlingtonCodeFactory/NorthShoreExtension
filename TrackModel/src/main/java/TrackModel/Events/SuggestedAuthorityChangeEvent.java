//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker, Robert Taylor
//**************************************************
package TrackModel.Events;

import java.util.EventObject;

public class SuggestedAuthorityChangeEvent extends EventObject
{
    public SuggestedAuthorityChangeEvent(Object source)
    {
        super(source);
    }
}
