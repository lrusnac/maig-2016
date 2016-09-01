package pacman.entries.pacman.behaviortrees;

import java.util.ArrayList;
import java.util.List;

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
