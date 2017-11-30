package CTCOffice;

import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.Train;
import TrackModel.Events.OccupancyChangeEvent;
import TrackModel.Events.OccupancyChangeListener;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import javafx.application.Platform;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class CTCEventHandler implements OccupancyChangeListener {
    private ITrainRepository trainRepository;
    private ITrackModelForCTCOffice trackModel;

    @Inject
    public CTCEventHandler(ITrainRepository trainRepository, ITrackModelForCTCOffice trackModel) {
        this.trainRepository = trainRepository;
        this.trackModel = trackModel;
    }

    @Override
    public void occupancyChangeReceived(OccupancyChangeEvent event) {
        // Get block whose occupancy changed
        Block changedBlock = (Block) event.getSource();
        if (changedBlock == null) {
            return;
        }

        if (!changedBlock.getIsOccupied()) {
            System.out.println("Occupancy false for id "+changedBlock.getId());
            // Train has left block - set speed and authority to 0
            changedBlock.setSuggestedSpeed(0);
            changedBlock.setSuggestedAuthority(new ArrayList<>());

            /*// If train is on block and has authority to go into yard delete it
            Train movedTrain = findTrainOnBlock(changedBlock);
            if (movedTrain != null && movedTrain.getSuggestedAuthority().get(0).getId() == 0) {
                trainRepository.removeTrain(movedTrain.getLine(), movedTrain.getId());
            }*/
        }
        else {
            System.out.println("Occupancy true for id "+changedBlock.getId());
            // Block is now occupied - determine if a train moved into it
            Train movedTrain = findTrainThatMoved(changedBlock);
            if (movedTrain != null) {
                System.out.println("Train "+movedTrain.getId() + "moved to "+changedBlock.getId());
                // The train moved into a new block, update the previous and current blocks
                Platform.runLater(
                        () -> {
                            movedTrain.setPreviousBlock(movedTrain.getCurrentBlock());
                            movedTrain.setCurrentBlock(changedBlock);
                        }
                );

                List<Block> newSuggestedAuthority = new ArrayList<>(movedTrain.getSuggestedAuthority());
                if (newSuggestedAuthority.size() > 1) {
                    // There is still authority left, so remove the previous block and assign the updated authority to the train
                    newSuggestedAuthority.remove(0);
                    System.out.println("Still authority left, setting "+changedBlock.getId()+" to "+newSuggestedAuthority);
                    Platform.runLater(
                            () -> {
                                movedTrain.setSuggestedAuthority(newSuggestedAuthority);
                                movedTrain.setSuggestedSpeed(movedTrain.getCurrentBlock().getSpeedLimit() < movedTrain.getSuggestedSpeed() ? movedTrain.getCurrentBlock().getSpeedLimit() : movedTrain.getSuggestedSpeed());
                            }
                    );
                }
                else {
                    // Should only occur if a train goes past its authority, so we want to set the authority to 0
                    Platform.runLater(
                            () -> movedTrain.setSuggestedAuthority(new ArrayList<>())
                    );
                    System.out.println("Authority is empty - did the train go past its authority?, setting "+changedBlock.getId()+" to empty authority");
                }
            }
        }
    }

    private Train findTrainThatMoved(Block changedBlock) {
        for (Train train : trainRepository.getTrains(changedBlock.getLine())) {
            for (int blockId : train.getCurrentBlock().getConnectedBlocks()) {
                if (blockId == changedBlock.getId()) {
                    return train;
                }
            }
        }

        return null;
    }

    private Train findTrainOnBlock(Block changedBlock) {
        for (Train train : trainRepository.getTrains(changedBlock.getLine())) {
            if (train.getCurrentBlock().getId() == changedBlock.getId()) {
                return train;
            }
        }

        return null;
    }
}
