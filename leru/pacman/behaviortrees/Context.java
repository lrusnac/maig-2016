package pacman.entries.leru.pacman.behaviortrees;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Context {

    // constants that are later used for genetic algorithm
    private int escapeThreshold;
    private int chaseThreshold;
    private int maxDepth;

    // This is the context class that all the nodes will be able to read and write
    // BE AWARE this is a shared context, attention on parallel stuff

    private MOVE nextMove;
    private Game game;

    public Context() {
        this.nextMove = MOVE.NEUTRAL;
    }

    public MOVE getNextMove() {
        return nextMove;
    }

    public void setNextMove(MOVE nextMove) {
        this.nextMove = nextMove;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getClosestPill() {
        int[] availablePills = new int[game.getNumberOfActivePills() + game.getNumberOfActivePowerPills()];
        System.arraycopy(game.getActivePillsIndices(), 0, availablePills, 0, game.getNumberOfActivePills());
        System.arraycopy(game.getActivePowerPillsIndices(), 0, availablePills, game.getNumberOfActivePills(),
                game.getNumberOfActivePowerPills());

        return game.getClosestNodeIndexFromNodeIndex(getPacmanPosition(), availablePills, DM.PATH);
    }

    public GHOST getClosestGhost() {
        GHOST closestGhost = null;
        int distanceToNearestGhost = Integer.MAX_VALUE;

        for (GHOST ghost : GHOST.values()) {
            if (game.getGhostLairTime(ghost) < 1 && !game.isGhostEdible(ghost)) {
                int distanceToGhost = game.getShortestPathDistance(getPacmanPosition(), game.getGhostCurrentNodeIndex(ghost));
                if (distanceToGhost < distanceToNearestGhost) {
                    distanceToNearestGhost = distanceToGhost;
                    closestGhost = ghost;
                }
            }
        }
        return closestGhost;
    }

    public GHOST getClosestEdibleGhost() {
        GHOST edibleGhost = null;
        int distanceToNearestEdibleGhost = Integer.MAX_VALUE;

        for (GHOST ghost : GHOST.values()) {
            if (game.isGhostEdible(ghost)) {
                int distanceToGhost = game.getShortestPathDistance(getPacmanPosition(), game.getGhostCurrentNodeIndex(ghost));
                if (distanceToGhost < distanceToNearestEdibleGhost) {
                    distanceToNearestEdibleGhost = distanceToGhost;
                    edibleGhost = ghost;
                }
            }
        }
        return edibleGhost;
    }

    public void isPacmanDoped() {
    }

    public int getPacmanPosition() {
        return game.getPacmanCurrentNodeIndex();
    }

    public void setMovementToNodeIndex(int nodeIndex) {
        nextMove = game.getNextMoveTowardsTarget(getPacmanPosition(), nodeIndex, DM.PATH);
    }

    public void setContinuousMovementAwayNodeIndex(int nodeIndex) {
        nextMove = game.getApproximateNextMoveAwayFromTarget(getPacmanPosition(), nodeIndex, nextMove, DM.PATH);
    }

    public void setMovementAwayNodeIndex(int nodeIndex) {
        nextMove = game.getNextMoveAwayFromTarget(getPacmanPosition(), nodeIndex, DM.PATH);
    }

    public int getEscapeThreshold() {
        return escapeThreshold;
    }

    public void setEscapeThreshold(int escapeThreshold) {
        this.escapeThreshold = escapeThreshold;
    }

    public int getChaseThreshold() {
        return chaseThreshold;
    }

    public void setChaseThreshold(int chaseThreshold) {
        this.chaseThreshold = chaseThreshold;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
