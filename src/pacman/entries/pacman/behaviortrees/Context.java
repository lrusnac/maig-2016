package pacman.entries.pacman.behaviortrees;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Context {
    // This is the context class that all the nodes will be able to read and write data
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
}
