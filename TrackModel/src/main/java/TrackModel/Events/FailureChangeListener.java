//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackModel.Events;

public interface FailureChangeListener
{
    public void failureChangeReceived(FailureChangeEvent event);
}
