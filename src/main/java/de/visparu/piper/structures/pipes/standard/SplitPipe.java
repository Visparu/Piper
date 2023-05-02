package de.visparu.piper.structures.pipes.standard;

import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.structures.pipes.Pipe;

public class SplitPipe extends Pipe {
    @Override
    public Set<Direction> getOpeningDirections() {
        return switch (super.getDirection()) {
            case EAST -> Set.of(Direction.SOUTH, Direction.WEST, Direction.NORTH);
            case NORTH -> Set.of(Direction.EAST, Direction.SOUTH, Direction.WEST);
            case SOUTH -> Set.of(Direction.WEST, Direction.NORTH, Direction.EAST);
            case WEST -> Set.of(Direction.NORTH, Direction.EAST, Direction.SOUTH);
        };
    }
}
