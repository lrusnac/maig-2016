package pacman.entries.pacman.behaviortrees.leaf;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.game.Constants.GHOST;

public class HuntGhosts extends Leaf {

    @Override
    public Status process(Context context) {

        GHOST edibleGhost = context.getClosestEdibleGhost();
        if (edibleGhost != null) {
            int ghostIndex = context.getGame().getGhostCurrentNodeIndex(edibleGhost);
            int distanceToGhost = context.getGame().getShortestPathDistance(context.getPacmanPosition(), ghostIndex);

            if (distanceToGhost < context.getChaseThreshold()) { // go to ghost only if it's close enough
                context.setMovementToNodeIndex(ghostIndex);
                return Status.SUCCESS;
            }
        }

        return Status.FAILURE;
    }

}
