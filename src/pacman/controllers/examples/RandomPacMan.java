package pacman.controllers.examples;

import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class RandomPacMan.
 */
public final class RandomPacMan extends Controller<MOVE> {
    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();

    /*
     * (non-Javadoc)
     * 
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    public MOVE getMove(Game game, long timeDue) {
        return allMoves[rnd.nextInt(allMoves.length)];
    }
}