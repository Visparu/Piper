package de.visparu.piper.game.structures.pipes.standard;

import java.util.Set;

import de.visparu.piper.game.structures.pipes.Pipe;

public class CurvedPipe extends Pipe {
    @Override
    public Set<Direction> getOpeningDirections() {
        Direction d1 = switch (super.getDirection()) {
            case EAST -> Direction.WEST;
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.EAST;
        };
        Direction d2 = switch (d1) {
            case EAST -> Direction.SOUTH;
            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
        return Set.of(d1, d2);
    }
}
