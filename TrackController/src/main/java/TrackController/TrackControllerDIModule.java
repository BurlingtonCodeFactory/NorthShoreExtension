package TrackController;

import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.TrackModel;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TrackControllerDIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ITrackModelForTrackController.class).to(TrackModel.class);
    }
}

