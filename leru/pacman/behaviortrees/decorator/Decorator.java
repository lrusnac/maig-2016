package pacman.entries.leru.pacman.behaviortrees.decorator;

import pacman.entries.leru.pacman.behaviortrees.Node;

public abstract class Decorator extends Node {
    private Node child;

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }
}
