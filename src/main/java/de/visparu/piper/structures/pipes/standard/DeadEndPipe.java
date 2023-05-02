package de.visparu.piper.structures.pipes.standard;

import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.structures.pipes.Pipe;

public class DeadEndPipe extends Pipe {
    @Override
    public Set<Direction> getOpeningDirections() {
        return Set.of(super.getDirection());
    }
}
