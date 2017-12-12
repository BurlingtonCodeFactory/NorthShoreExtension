package CTCOffice.Services;

import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Interfaces.ITrainRepository;
import CTCOffice.Models.Train;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import TrackModel.Models.Line;
import com.google.inject.Inject;

import java.util.*;

public class RouteService implements IRouteService {

    private ITrackModelForCTCOffice trackModel;
    private ITrainRepository trainRepository;

    @Inject
    public RouteService(ITrackModelForCTCOffice trackModel, ITrainRepository trainRepository) {
        this.trackModel = trackModel;
        this.trainRepository = trainRepository;
    }

    @Override
    public List<Block> getShortestPath(Block previousBlock, Block currentBlock, Block destination) {
        if (destination == null) {
            return null;
        }
        System.out.print("Previous "+previousBlock.getId());
        System.out.print(" Current "+currentBlock.getId());
        System.out.println(" Destination "+destination.getId());


        Map<Block, Integer> distance = new HashMap<>();
        Map<Block, Block> previous = new HashMap<>();
        Queue<Block> queue = new LinkedList<>();

        distance.put(currentBlock, 0);
        previous.put(currentBlock, null);
        queue.offer(currentBlock);

        boolean firstTime = true;
        while (!queue.isEmpty()) {
            Block u = queue.poll();
            if (u.equals(destination)) {
                List<Block> path = tracebackPath(u, previous);
                path.add(0, currentBlock);
                return path;
            }

            queue.remove(u);

            for (int neighborId : u.getConnectedBlocks()) {
                if (firstTime && previousBlock != null && previousBlock.getId() == neighborId) {
                    continue;
                }

                Block neighbor = trackModel.getBlock(currentBlock.getLine(), neighborId);
                if (!distance.containsKey(neighbor) && !neighbor.getIsOccupied() && !neighbor.getUnderMaintenance()) {
                    distance.put(neighbor, distance.get(u) + 1);
                    previous.put(neighbor, u);
                    queue.offer(neighbor);
                }
            }
        }

        System.out.println("Route returned as null, couldn't find path");

        return null;
    }

    @Override
    public List<Block> getShortestPathWithMidpoint(Block previousBlock, Block currentBlock, Block midpoint, Block destination) {
        return null;
    }

    @Override
    public void RouteTrains(Line line) {
        for (Train train : trainRepository.getTrains(line)) {
            if (train.getPreviousBlock() == null) {
                continue;
            }

            if (trainRepository.getMode()) {
                Block destination = train.getDestinationBlock();
                if (destination == null && train.getSchedule().size() > 0) {
                    System.out.println("No destination for train " + line + "-" + train.getId() + " setting destination to " + train.getSchedule().get(0).getBlock());
                    train.setDestinationBlock(train.getSchedule().get(0).getBlock());
                }
                else if (destination != null && destination.getId() == train.getCurrentBlock().getId() && train.getSchedule().size() > 0) {
                    System.out.println("Destination reached for train " + line + "-" + train.getId() + " setting destination to " + train.getSchedule().get(0).getBlock());
                    train.setDestinationBlock(train.getSchedule().get(0).getBlock());
                }
            }

            List<Block> newAuthority = getShortestPath(train.getPreviousBlock(), train.getCurrentBlock(), train.getDestinationBlock());
            train.setSuggestedAuthority(newAuthority == null ? new ArrayList<>() : newAuthority);
            if (trainRepository.getMode()) {
                train.setSuggestedSpeed(train.getCurrentBlock().getSpeedLimit());
            }
            else {
                train.setSuggestedSpeed(train.getCurrentBlock().getSpeedLimit() < train.getSuggestedSpeed() ? train.getCurrentBlock().getSpeedLimit() : train.getSuggestedSpeed());
            }
            System.out.println("Setting " + line + "-" + train.getId() + " authority of " + newAuthority);
            /*else if (trainRepository.getMode() && train.getSchedule().size() > 0) {
                train.setSuggestedSpeed(train.getCurrentBlock().getSpeedLimit());
                List<Block> newAuthority = getShortestPath(train.getPreviousBlock(), train.getCurrentBlock(), train.getSchedule().get(0).getBlock());
            }*/
        }
    }

    private List<Block> tracebackPath(Block target, Map<Block, Block> previous) {
        List<Block> s = new LinkedList<>();

        Block u = target;
        while (previous.get(u) != null) {
            s.add(u);
            u = previous.get(u);
        }

        Collections.reverse(s);

        return s;
    }
}
