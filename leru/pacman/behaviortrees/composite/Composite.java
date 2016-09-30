package pacman.entries.leru.pacman.behaviortrees.composite;

import java.util.ArrayList;
import java.util.List;

import pacman.entries.leru.pacman.behaviortrees.Node;

public abstract class Composite extends Node {
    private List<Node> children;

    public Composite() {
        super();
        this.children = new ArrayList<>();
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }
}
