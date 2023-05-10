package de.visparu.piper.game.structures.pipes.standard;

import java.util.Set;

import de.visparu.piper.game.structures.pipes.Pipe;

public class CrossPipe extends Pipe {
    @Override
    public Set<Direction> getOpeningDirections() {
        return Set.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    }
}
