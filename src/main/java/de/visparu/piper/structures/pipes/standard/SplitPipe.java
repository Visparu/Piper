package de.visparu.piper.structures.pipes.standard;

import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.structures.pipes.Pipe;

public class SplitPipe extends Pipe {
    @Override
    public Set<Direction> getOpeningDirections() {
        Direction d1, d2, d3;
        switch (super.getDirection()) {
            case EAST: {
                d1 = Direction.SOUTH;
                d2 = Direction.WEST;
                d3 = Direction.NORTH;
                break;
            }
            case NORTH: {
                d1 = Direction.EAST;
                d2 = Direction.SOUTH;
                d3 = Direction.WEST;
                break;
            }
            case SOUTH: {
                d1 = Direction.WEST;
                d2 = Direction.NORTH;
                d3 = Direction.EAST;
                break;
            }
            case WEST: {
                d1 = Direction.NORTH;
                d2 = Direction.EAST;
                d3 = Direction.SOUTH;
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }

        Set<Direction> result = new HashSet<>();
        result.add(d1);
        result.add(d2);
        result.add(d3);
        return result;
    }
}
