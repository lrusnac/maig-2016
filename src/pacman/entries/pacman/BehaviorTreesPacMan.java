package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.entries.pacman.behaviortrees.Context;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

// ms pacman controlled with behavior trees

// Behavior trees implemented with reference to https://goo.gl/SAVaVh

public class BehaviorTreesPacMan extends Controller<MOVE> {
    private MOVE nextMove = MOVE.NEUTRAL;
    private Context context;

    public BehaviorTreesPacMan() {
        super();
        this.context = new Context();
    }

    public MOVE getMove(Game game, long timeDue) {

        return nextMove;
    }
}
