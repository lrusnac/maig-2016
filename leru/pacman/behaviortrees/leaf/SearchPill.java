package pacman.entries.leru.pacman.behaviortrees.leaf;

import pacman.entries.leru.pacman.behaviortrees.Context;
import pacman.entries.leru.pacman.behaviortrees.Status;

public class SearchPill extends Leaf {

    @Override
    public Status process(Context context) {

        context.setMovementToNodeIndex(context.getClosestPill());
        return Status.SUCCESS;
    }

}
