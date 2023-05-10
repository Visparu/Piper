package de.visparu.piper.game.structures.pipes.standard;

import java.util.Set;

import de.visparu.piper.game.structures.pipes.Pipe;

public class DeadEndPipe extends Pipe {
    @Override
    public Set<Direction> getOpeningDirections() {
        return Set.of(super.getDirection());
    }
}
