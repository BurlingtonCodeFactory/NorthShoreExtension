//**************************************************
//  COE 1186 - Software Engineering
//
//  Burlington Code Factory
//
//  Ryan Becker
//**************************************************
package TrackController;

import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.TrackModel;
import com.google.inject.AbstractModule;

public class TrackControllerDIModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(ITrackModelForTrackController.class).to(TrackModel.class);
    }
}

