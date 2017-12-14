package System;

import CTCOffice.Interfaces.IFileService;
import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.TrainRepository;
import CTCOffice.Services.FileService;
import CTCOffice.Services.RouteService;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Interfaces.ITrackModelForTrackController;
import TrackModel.Interfaces.ITrackModelForTrainController;
import TrackModel.Interfaces.ITrackModelForTrainModel;
import TrackModel.TrackModel;
import TrainModel.Interfaces.ITrainModelForCTCOffice;
import TrainModel.TrainModel;
import com.google.inject.AbstractModule;

public class DIModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(ITrackModelForTrackController.class).to(TrackModel.class);
        bind(ITrackModelForTrainController.class).to(TrackModel.class);
        bind(ITrackModelForTrainModel.class).to(TrackModel.class);
        bind(ITrackModelForCTCOffice.class).to(TrackModel.class);
        bind(ITrainModelForCTCOffice.class).to(TrainModel.class);
        bind(ITrainRepository.class).to(TrainRepository.class);
        bind(IRouteService.class).to(RouteService.class);
        bind(IFileService.class).to(FileService.class); // NOTE: this is the CTCOffice.FileService, if you need the TrackModel version you will have to be more specific
    }
}
