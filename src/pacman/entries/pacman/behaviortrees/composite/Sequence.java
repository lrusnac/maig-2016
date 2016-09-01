package pacman.entries.pacman.behaviortrees.composite;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Node;
import pacman.entries.pacman.behaviortrees.Status;

public class Sequence extends Composite {
    // the selector is equivalent to an AND operation, at least one failure child in order to fail, does not evaluate the next ones

    @Override
    public Status process(Context context) {
        for (Node node : super.getChildren()) {
            Status childStatus = node.process(context);
            if (childStatus == Status.RUNNING || childStatus == Status.FAILURE) {
                return childStatus;
            }
        }
        return Status.SUCCESS;
    }
}
