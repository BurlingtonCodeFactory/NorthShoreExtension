package CTCOffice;

import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.Train;
import TrackModel.Events.MaintenanceChangeEvent;
import TrackModel.Events.MaintenanceChangeListener;
import TrackModel.Events.OccupancyChangeEvent;
import TrackModel.Events.OccupancyChangeListener;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.application.Platform;

import java.util.ArrayList;

@Singleton
public class CTCEventHandler implements OccupancyChangeListener, MaintenanceChangeListener {
    private ITrainRepository trainRepository;
    private ITrackModelForCTCOffice trackModel;
    private IRouteService routeService;

    @Inject
    public CTCEventHandler(ITrainRepository trainRepository, ITrackModelForCTCOffice trackModel, IRouteService routeService) {
        this.trainRepository = trainRepository;
        this.trackModel = trackModel;
        this.routeService = routeService;
    }

    @Override
    public void occupancyChangeReceived(OccupancyChangeEvent event) {
        // Get block whose occupancy changed
        Block changedBlock = (Block) event.getSource();
        if (changedBlock == null) {
            throw new NullPointerException("This shouldn't happen");
        }

        if (!changedBlock.getIsOccupied()) {
            System.out.println("Occupancy false for line "+ changedBlock.getLine() + " block "+changedBlock.getId());
            // Train has left block - set speed and authority to 0
            changedBlock.setSuggestedSpeed(0);
            changedBlock.setSuggestedAuthority(new ArrayList<>());

            Train train = findTrainOnBlock(changedBlock);
            if (train == null) {
                System.out.println("CTC: Track failure must have been resolved on line - " + changedBlock.getLine() + " block - " + changedBlock.getId() + " rerouting trains.");
                Platform.runLater(
                        () -> routeService.RouteTrains(changedBlock.getLine())
                );
            }
            else if (changedBlock.getId() == 0 && train.getPreviousBlock().getId() != 0) {
                // TODO: test to make sure this works, need to wait for Evan to implement deleting trains in Train Model
                System.out.println("Train " + train.getId() + " was in yard after having been elsewhere previously. Removing train from CTC.");
                trainRepository.removeTrain(train.getLine(), train.getId());
            }
        }
        else {
            System.out.println("Occupancy true for line "+ changedBlock.getLine() + " block "+changedBlock.getId());
            // Block is now occupied - determine if a train moved into it
            Train movedTrain = findTrainThatMoved(changedBlock);
            if (movedTrain != null) {
                System.out.println("Train " + movedTrain.getId() + " moved to " + changedBlock.getId());
                // The train moved into a new block, update the previous and current blocks
                Platform.runLater(
                        () -> {
                            movedTrain.setPreviousBlock(movedTrain.getCurrentBlock());
                            movedTrain.setCurrentBlock(changedBlock);
                            if (movedTrain.getSchedule().size() > 0 && movedTrain.getSchedule().get(0).getBlock().getId() == movedTrain.getCurrentBlock().getId()) {
                                System.out.println("Stop " + movedTrain.getSchedule().get(0).getBlock() + " reached for train "+ movedTrain.getLine() + "-" + movedTrain.getId());
                                movedTrain.removeStop(movedTrain.getSchedule().get(0));
                            }
                            routeService.RouteTrains(changedBlock.getLine());
                        }
                );

                if (movedTrain.getSuggestedAuthority().size() <= 1) {
                    // Should only occur if a train goes past its authority, so we want to set the authority to 0
                    Platform.runLater(
                            () -> movedTrain.setSuggestedAuthority(new ArrayList<>())
                    );
                    System.out.println("Authority is empty - did the train go past its authority?, setting "+changedBlock.getId()+" to empty authority");
                }
            }
            else {
                if (changedBlock.getId() != 0) {
                    System.out.println("CTC: Track failure must have been occurred on line - " + changedBlock.getLine() + " block - " + changedBlock.getId() + " rerouting trains.");
                }
                Platform.runLater(
                        () -> routeService.RouteTrains(changedBlock.getLine())
                );
            }
        }
    }

    @Override
    public void maintenanceChangeReceived(MaintenanceChangeEvent event) {
        // Get block whose maintenance state changed
        Block changedBlock = (Block) event.getSource();

        System.out.println("CTC: Maintenance changed received - line " + changedBlock.getLine() + " block " + changedBlock.getId() + " is now " + changedBlock.getUnderMaintenance() + " - rerouting trains.");
        Platform.runLater(
                () -> routeService.RouteTrains(changedBlock.getLine())
        );
    }

    private Train findTrainThatMoved(Block changedBlock) {
        for (Train train : trainRepository.getTrains(changedBlock.getLine())) {
            if (train.getPreviousBlock() != null) {
                for (int blockId : train.getCurrentBlock().getConnectedBlocks()) {
                    if (blockId == changedBlock.getId()) {
                        return train;
                    }
                }
            }
        }

        return null;
    }

    private Train findTrainOnBlock(Block changedBlock) {
        for (Train train : trainRepository.getTrains(changedBlock.getLine())) {
            if (train.getCurrentBlock().getId() == changedBlock.getId() && train.getPreviousBlock() != null) {
                return train;
            }
        }

        return null;
    }
}
