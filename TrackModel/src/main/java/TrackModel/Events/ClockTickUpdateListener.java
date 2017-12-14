//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package TrackModel.Events;

public interface ClockTickUpdateListener
{
    void clockTickUpdateReceived(ClockTickUpdateEvent event);
}
