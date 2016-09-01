package pacman.entries.pacman.behaviortrees.leaf;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class HuntGhosts extends Leaf {

    @Override
    public Status process(Context context) {
        Status status = Status.FAILURE;

        GHOST edibleGhost = null;
        int distanceToNearestEdibleGhost = 1111; // TODO figure out the max distance I want to use for eating a ghost

        for (GHOST ghost : GHOST.values()) {
            if (context.getGame().isGhostEdible(ghost)) {
                int distanceToGhost = context.getGame().getShortestPathDistance(context.getGame().getPacmanCurrentNodeIndex(),
                        context.getGame().getGhostCurrentNodeIndex(ghost));
                if (distanceToGhost < distanceToNearestEdibleGhost) {
                    distanceToNearestEdibleGhost = distanceToGhost;
                    edibleGhost = ghost;
                }
            }
        }

        if (edibleGhost != null) {
            context.setNextMove(context.getGame().getNextMoveTowardsTarget(context.getGame().getPacmanCurrentNodeIndex(),
                    context.getGame().getGhostCurrentNodeIndex(edibleGhost), DM.PATH));
            status = Status.SUCCESS;
        }

        return status;
    }

}
