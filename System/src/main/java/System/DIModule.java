package System;

import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.TrackModel;
import TrainModel.Interfaces.ITrainModelForCTCOffice;
import TrainModel.TrainModel;
import com.google.inject.AbstractModule;

public class DIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ITrackModelForTrackController.class).to(TrackModel.class);
        bind(ITrackModelForTrainController.class).to(TrackModel.class);
        bind(ITrackModelForTrainModel.class).to(TrackModel.class);
        bind(ITrackModelForCTCOffice.class).to(TrackModel.class);
        bind(ITrainModelForCTCOffice.class).to(TrainModel.class);

    }
}
