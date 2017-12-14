//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Robert Taylor
//**************************************************
package TrackModel.Events;

public interface MaintenanceRequestListener
{
    public void maintenanceRequestReceived(MaintenanceRequestEvent event);
}