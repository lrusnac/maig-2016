package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Node;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.entries.pacman.behaviortrees.composite.Selector;
import pacman.entries.pacman.behaviortrees.leaf.HuntGhosts;
import pacman.entries.pacman.behaviortrees.leaf.RunFromGhosts;
import pacman.entries.pacman.behaviortrees.leaf.SearchPill;
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
        Node runFromGhosts = new RunFromGhosts();
        Node huntGhosts = new HuntGhosts();
        Node searchPill = new SearchPill();

        ((Selector) root).addChild(ghosts);
        ((Selector) root).addChild(searchPill);

        ((Selector) ghosts).addChild(runFromGhosts);
        ((Selector) ghosts).addChild(huntGhosts);
    }
}
