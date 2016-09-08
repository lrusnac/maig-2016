package pacman.entries.pacman.behaviortrees.leaf;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.game.Constants.GHOST;

public class GhostTooClose extends Leaf {

    @Override
    public Status process(Context context) {
        GHOST ghost = context.getClosestGhost();
        if (ghost != null) { // must be not null actually
            int ghostIndex = context.getGame().getGhostCurrentNodeIndex(ghost);
            int distanceToGhost = context.getGame().getShortestPathDistance(context.getPacmanPosition(), ghostIndex);

            if (distanceToGhost < context.getEscapeThreshold()) { // run away if it's close enough
                return Status.SUCCESS;
            }
        }

        return Status.FAILURE;
    }

}
