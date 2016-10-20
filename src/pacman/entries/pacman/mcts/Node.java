package pacman.entries.pacman.mcts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Node {
    private static final double C = 100;

    private final Node parent;
    private final List<Node> children;
    private final MOVE move;
    private final Game state;

    private int visited;
    private double score;
    public int depth;

    public Node(Game state, Node parent, MOVE move) {
        this.state = state;
        this.parent = parent;
        this.move = move;
        this.depth = (parent == null) ? 0 : parent.getDepth() + 1;
        this.children = new ArrayList<>();
        this.visited = 1;
        this.score = 0;
    }

    public double getUCTScore() {
        if (state.gameOver()) {
            return 0;
        }

        return (this.score / this.visited) + C * Math.sqrt(Math.log(this.parent.getVisited()) / this.visited);
    }

    public Node getBestUCTChild() {
        if (this.children.isEmpty()) {
            return null;
        }
        return this.children.stream().max(Comparator.comparingDouble(Node::getUCTScore)).get();
    }

    public Node getBestChild() {
        if (this.children.isEmpty()) {
            return null;
        }
        return this.children.stream().max(Comparator.comparingDouble(Node::getScore)).get();
    }

    public int getVisited() {
        return visited;
    }

    public void setVisited(int visited) {
        this.visited = visited;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public MOVE getMove() {
        return move;
    }

    public Game getState() {
        return state;
    }
}
