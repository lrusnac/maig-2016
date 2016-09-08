package pacman.entries.pacman.behaviortrees.leaf;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.game.Constants.GHOST;

public class RunFromGhosts extends Leaf {

    @Override
    public Status process(Context context) {
        GHOST ghost = context.getClosestGhost();
        if (ghost != null) { // must be actually
            int ghostIndex = context.getGame().getGhostCurrentNodeIndex(ghost);
            int distanceToGhost = context.getGame().getShortestPathDistance(context.getPacmanPosition(), ghostIndex);

            if (distanceToGhost < 20) { // run away if it's close enough
                // context.setMovementAwayNodeIndex(ghostIndex);
                return Status.SUCCESS;
            }
            // } else if (distanceToGhost < 15) {
            // context.setContinuousMovementAwayNodeIndex(ghostIndex);
            // return Status.SUCCESS;
            // }
        }

        return Status.FAILURE;
    }

}
