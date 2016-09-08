package pacman.entries.pacman.behaviortrees.leaf;

import java.awt.Color;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.game.Constants.GHOST;
import pacman.game.GameView;

public class FindSafePath extends Leaf {

    private static final int DEPTH = 40;

    private List<Integer> pillsIndexes;
    private List<Integer> powerPillsIndexes;
    private Map<Integer, GHOST> ghostsIndexes;

    private List<Integer> visitedNodes;

    private int initialNode;

    @Override
    public Status process(Context context) {
        initialNode = context.getPacmanPosition();
        this.visitedNodes = new ArrayList<>();

        this.ghostsIndexes = new HashMap<>();
        this.pillsIndexes = IntStream.of(context.getGame().getActivePillsIndices()).boxed().collect(Collectors.toList());
        this.powerPillsIndexes = IntStream.of(context.getGame().getActivePowerPillsIndices()).boxed().collect(Collectors.toList());

        for (GHOST g : GHOST.values()) {
            ghostsIndexes.put(context.getGame().getGhostCurrentNodeIndex(g), g);
        }

        Entry<Integer, Integer> bestNeighbor = expandPath(context, context.getPacmanPosition(), DEPTH);

        if (bestNeighbor.getValue() <= DEPTH / 4) {
            context.setMovementAwayNodeIndex(context.getGame().getGhostCurrentNodeIndex(context.getClosestGhost()));
        } else {
            context.setMovementToNodeIndex(bestNeighbor.getKey());
        }

        return Status.SUCCESS;
    }

    private Entry<Integer, Integer> expandPath(Context context, int nodeIndex, int steps) {

        if (visitedNodes.contains(nodeIndex) || steps == 0) {
            GameView.addLines(context.getGame(), Color.GREEN, nodeIndex, initialNode);
            return new AbstractMap.SimpleEntry<>(nodeIndex, 0);
        } else {
            visitedNodes.add(nodeIndex);
        }

        Map<Integer, Integer> neighborsScore = new HashMap<>();

        for (int n : context.getGame().getNeighbouringNodes(nodeIndex)) {
            if (this.ghostsIndexes.containsKey(n)) {
                if (context.getGame().isGhostEdible(this.ghostsIndexes.get(n))) {
                    neighborsScore.put(n, 10 + expandPath(context, n, steps - 1).getValue());
                } else {
                    GameView.addLines(context.getGame(), Color.RED, nodeIndex, initialNode);
                    return new AbstractMap.SimpleEntry<>(n, -50);
                }
            } else if (this.pillsIndexes.contains(n)) {
                neighborsScore.put(n, 5 + expandPath(context, n, steps - 1).getValue());
            } else if (this.powerPillsIndexes.contains(n)) {
                neighborsScore.put(n, 20 + expandPath(context, n, steps - 1).getValue());
            } else {
                neighborsScore.put(n, 1 + expandPath(context, n, steps - 1).getValue());
            }
        }

        // if (steps == DEPTH) {
        // System.out.println(neighborsScore);
        // }

        Integer bestScore = Integer.MIN_VALUE;
        Integer bestNeighbor = -1;

        for (Integer neighbor : neighborsScore.keySet()) {
            if (neighborsScore.get(neighbor) > bestScore) {
                bestScore = neighborsScore.get(neighbor);
                bestNeighbor = neighbor;
            }
        }

        return new AbstractMap.SimpleEntry<>(bestNeighbor, bestScore);
    }
}
