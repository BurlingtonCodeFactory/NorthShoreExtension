package CTCOffice.Services;

import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Models.Train;
import TrackModel.Interfaces.ITrackModelForCTCOffice;
import TrackModel.Models.Block;
import com.google.inject.Inject;

import java.util.*;

public class RouteService implements IRouteService {

    private ITrackModelForCTCOffice repository;

    @Inject
    public RouteService(ITrackModelForCTCOffice repository) {
        this.repository = repository;
    }

    @Override
    public List<Block> getShortestPath(Block previousBlock, Block currentBlock, Block destination) {
        System.out.println(previousBlock.getId());
        System.out.println(currentBlock.getId());
        System.out.println(destination.getId());


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

                Block neighbor = repository.getBlock(currentBlock.getLine(), neighborId);
                if (!distance.containsKey(neighbor)) {
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
