package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Node;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.entries.pacman.behaviortrees.composite.Selector;
import pacman.entries.pacman.behaviortrees.composite.Sequence;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

// ms pacman controlled with behavior trees

// Behavior trees implemented with reference to https://goo.gl/SAVaVh

public class BehaviorTreesPacMan extends Controller<MOVE> {
    private Context context;
    private Node root;

    public BehaviorTreesPacMan() {
        super();
        this.context = new Context();
        this.buildTree();
    }

    public MOVE getMove(Game game, long timeDue) {
        context.setGame(game);
        Status status = root.process(context);

        if (status != Status.SUCCESS) {
            return MOVE.NEUTRAL;
        } else {
            return context.getNextMove();
        }
    }

    private void buildTree() {
        this.root = new Selector();
        Node ghosts = new Selector();
        Node decisions = new Sequence();

        ((Selector) root).addChild(ghosts);
        ((Selector) root).addChild(decisions);

        // Node runFromGhosts
        // Node huntGhosts
        // Node findDot
        // Node chooseDirection
    }
}
