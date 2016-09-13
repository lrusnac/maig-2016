package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Node;
import pacman.entries.pacman.behaviortrees.Status;
import pacman.entries.pacman.behaviortrees.composite.Composite;
import pacman.entries.pacman.behaviortrees.composite.Selector;
import pacman.entries.pacman.behaviortrees.composite.Sequence;
import pacman.entries.pacman.behaviortrees.leaf.FindSafePath;
import pacman.entries.pacman.behaviortrees.leaf.GhostTooClose;
import pacman.entries.pacman.behaviortrees.leaf.HuntGhosts;
import pacman.entries.pacman.behaviortrees.leaf.SearchPill;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

// ms pacman controlled with behaviour trees

// Behavior trees implemented with reference to https://goo.gl/SAVaVh

public class BehaviorTreesPacMan extends Controller<MOVE> {
    private Context context;
    private Node root;

    public BehaviorTreesPacMan() {
        this(25, 6, 41);
    }

    public BehaviorTreesPacMan(int escapeThreshold, int chaseThreshold, int maxDepth) {
        super();
        this.context = new Context();

        this.context.setChaseThreshold(chaseThreshold);
        this.context.setEscapeThreshold(escapeThreshold);
        this.context.setMaxDepth(maxDepth);

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
        Node defence = new Sequence();

        Node huntGhosts = new HuntGhosts();
        Node searchPill = new SearchPill();

        ((Composite) root).addChild(defence);
        ((Composite) root).addChild(huntGhosts);
        ((Composite) root).addChild(searchPill);

        Node runFromGhosts = new GhostTooClose();
        Node findSafePath = new FindSafePath();
        ((Composite) defence).addChild(runFromGhosts);
        ((Composite) defence).addChild(findSafePath);
    }
}
