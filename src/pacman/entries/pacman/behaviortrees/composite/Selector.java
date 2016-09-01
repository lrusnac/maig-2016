package pacman.entries.pacman.behaviortrees.composite;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Node;
import pacman.entries.pacman.behaviortrees.Status;

public class Selector extends Composite {
    // the selector is equivalent to an OR operation, at least one success child, does not evaluate the next ones

    @Override
    public Status process(Context context) {
        for (Node node : super.getChildren()) {
            Status childStatus = node.process(context);
            if (childStatus == Status.RUNNING || childStatus == Status.SUCCESS) {
                return childStatus;
            }
        }
        return Status.FAILURE;
    }

}
