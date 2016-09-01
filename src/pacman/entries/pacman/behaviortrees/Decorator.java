package pacman.entries.pacman.behaviortrees;

public abstract class Decorator extends Node {
    private Node child;

    public Decorator() {
        super();
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }
}
