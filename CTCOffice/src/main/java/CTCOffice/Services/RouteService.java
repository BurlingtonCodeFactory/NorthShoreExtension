package CTCOffice.Services;

import CTCOffice.Interfaces.IRouteService;
import CTCOffice.Models.Block;
import CTCOffice.Models.Repository;
import com.google.inject.Inject;

import java.util.*;

public class RouteService implements IRouteService {

    private Repository repository;

    @Inject
    public RouteService(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<Block> getShortestPath(Block previousBlock, Block currentBlock, Block destination, List<Block> blocks) {
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

            for (int neighborId : u.getTravelTo()) {
                if (firstTime && previousBlock != null && previousBlock.getBlockId() == neighborId) {
                    continue;
                }

                Block neighbor = repository.getBlock(neighborId);
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, distance.get(u) + 1);
                    previous.put(neighbor, u);
                    queue.offer(neighbor);
                }
            }
        }

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