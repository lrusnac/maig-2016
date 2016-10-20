package pacman.entries.pacman;

import java.security.SecureRandom;
import java.util.EnumMap;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.entries.pacman.mcts.Node;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MCTSPacMan extends Controller<MOVE> {

    private static final long DECISION_TIME = 5;
    private static final long DEPTH_LIMIT = 40;
    private static final Controller<EnumMap<GHOST, MOVE>> ghostsController = new StarterGhosts();

    @Override
    public MOVE getMove(Game game, long timeDue) {
        Node root = new Node(game.copy(), null, null);

        while (System.currentTimeMillis() < timeDue - DECISION_TIME) {
            Node mostPromisingNode = this.treePolicy(root);
            double score = this.defaultPolicy(mostPromisingNode);
            this.backPropagate(mostPromisingNode, score);
        }

        Node best = root.getBestChild();
        if (best != null) {
            lastMove = best.getMove();
        }
        return lastMove;
    }

    private Node treePolicy(Node node) {
        int remainingLives = node.getState().getPacmanNumberOfLivesRemaining();
        int currentLevel = node.getState().getCurrentLevel();

        while (!node.getState().gameOver() && remainingLives == node.getState().getPacmanNumberOfLivesRemaining()
                && currentLevel == node.getState().getCurrentLevel() && node.depth < DEPTH_LIMIT) {
            if (!isFullyExpanded(node)) {
                return expandNode(node);
            } else {
                node = node.getBestUCTChild();
            }
        }
        return node;
    }

    private double defaultPolicy(Node node) {
        Game state = node.getState().copy();
        SecureRandom random = new SecureRandom();
        int currentLevel = state.getCurrentLevel();

        while (!state.gameOver()) {
            int remainingLives = state.getPacmanNumberOfLivesRemaining();
            EnumMap<GHOST, MOVE> ghostMove = ghostsController.getMove(state, System.currentTimeMillis() + 1);
            MOVE[] moves = this.getPossibleMoves(state);
            MOVE pacManMove = moves[random.nextInt(moves.length)];

            state.advanceGame(pacManMove, ghostMove);

            if (state.getPacmanNumberOfLivesRemaining() < remainingLives) {
                break;
            }

            if (state.getCurrentLevel() != currentLevel) {
                return Double.MAX_VALUE;
            }
        }

        return state.getScore() - state.getNumberOfActivePills() * 100;
    }

    private void backPropagate(Node node, double score) {
        while (node != null) {
            node.setVisited(node.getVisited() + 1);
            node.setScore(node.getScore() + score);
            node = node.getParent();
        }
    }

    private Node expandNode(Node node) {
        Game state = node.getState().copy();

        MOVE[] possibleMoves = getPossibleMoves(state);

        SecureRandom random = new SecureRandom();
        int index = random.nextInt(possibleMoves.length);
        while (alreadyExpanded(node, possibleMoves[index])) {
            index = (index + 1) % possibleMoves.length;
        }

        do {
            state.advanceGame(possibleMoves[index], ghostsController.getMove(state, System.currentTimeMillis() + 1));
        } while (!state.isJunction(state.getPacmanCurrentNodeIndex()) && !state.gameOver());

        Node child = new Node(state, node, possibleMoves[index]);

        node.getChildren().add(child);

        return child;
    }

    private boolean alreadyExpanded(Node node, MOVE move) {
        return node.getChildren().stream().filter(child -> (child.getMove() == move)).count() != 0;
    }

    private boolean isFullyExpanded(Node node) {
        return getPossibleMoves(node.getState()).length == node.getChildren().size();
    }

    private MOVE[] getPossibleMoves(Game state) {
        return state.getPossibleMoves(state.getPacmanCurrentNodeIndex(), state.getPacmanLastMoveMade());
    }
}
