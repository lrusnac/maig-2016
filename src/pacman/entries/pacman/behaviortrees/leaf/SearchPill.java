package pacman.entries.pacman.behaviortrees.leaf;

import pacman.entries.pacman.behaviortrees.Context;
import pacman.entries.pacman.behaviortrees.Status;

public class SearchPill extends Leaf {

    @Override
    public Status process(Context context) {

        context.setMovementToNodeIndex(context.getClosestPill());
        return Status.SUCCESS;
    }

}
