package de.visparu.piper.game.structures.pipes.standard;

import java.util.Set;

import de.visparu.piper.game.structures.pipes.Pipe;

public class StraightPipe extends Pipe {
    public StraightPipe() {
        super();
    }

    public StraightPipe(Direction direction) {
        super(direction);
    }

    public StraightPipe(Direction direction,
                        Direction entryPoint) {
        super(direction);
        super.addEntryPoint(entryPoint);
    }

    @Override
    public Set<Direction> getOpeningDirections() {
        Direction d1 = super.getDirection();
        Direction d2 = switch (d1) {
            case EAST -> Direction.WEST;
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.EAST;
        };

        return Set.of(d1, d2);
    }
}
