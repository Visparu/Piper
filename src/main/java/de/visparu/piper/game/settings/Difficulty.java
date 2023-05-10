package de.visparu.piper.game.settings;

import de.visparu.piper.game.structures.pipes.Pipe;
import de.visparu.piper.game.structures.pipes.standard.CrossPipe;
import de.visparu.piper.game.structures.pipes.standard.CurvedPipe;
import de.visparu.piper.game.structures.pipes.standard.DeadEndPipe;
import de.visparu.piper.game.structures.pipes.standard.SplitPipe;
import de.visparu.piper.game.structures.pipes.standard.StraightPipe;

import java.util.Map;

public final class Difficulty {
    private static final Map<Class<? extends Pipe>, Integer> FIXED_PIPE_MAP_EASY   = Map.ofEntries(Map.entry(StraightPipe.class, 1),
                                                                                                   Map.entry(CurvedPipe.class, 1),
                                                                                                   Map.entry(SplitPipe.class, 1),
                                                                                                   Map.entry(CrossPipe.class, 0),
                                                                                                   Map.entry(DeadEndPipe.class, 0));
    private static final Map<Class<? extends Pipe>, Integer> FIXED_PIPE_MAP_MEDIUM = Map.ofEntries(Map.entry(StraightPipe.class, 1),
                                                                                                   Map.entry(CurvedPipe.class, 1),
                                                                                                   Map.entry(SplitPipe.class, 1),
                                                                                                   Map.entry(CrossPipe.class, 1),
                                                                                                   Map.entry(DeadEndPipe.class, 1));
    private static final Map<Class<? extends Pipe>, Integer> FIXED_PIPE_MAP_HARD   = Map.ofEntries(Map.entry(StraightPipe.class, 2),
                                                                                                   Map.entry(CurvedPipe.class, 2),
                                                                                                   Map.entry(SplitPipe.class, 2),
                                                                                                   Map.entry(CrossPipe.class, 1),
                                                                                                   Map.entry(DeadEndPipe.class, 1));
    private static final Map<Class<? extends Pipe>, Integer> FIXED_PIPE_MAP_WTF    = Map.ofEntries(Map.entry(StraightPipe.class, 3),
                                                                                                   Map.entry(CurvedPipe.class, 3),
                                                                                                   Map.entry(SplitPipe.class, 3),
                                                                                                   Map.entry(CrossPipe.class, 4),
                                                                                                   Map.entry(DeadEndPipe.class, 3));

    public static final Difficulty EASY   = new Difficulty(10, 10, 10.0F, 15.0F, Difficulty.FIXED_PIPE_MAP_EASY, 1, 1);
    public static final Difficulty MEDIUM = new Difficulty(10, 10, 10.0F, 12.5F, Difficulty.FIXED_PIPE_MAP_MEDIUM, 1, 2);
    public static final Difficulty HARD   = new Difficulty(15, 15, 15.0F, 10.0F, Difficulty.FIXED_PIPE_MAP_HARD, 2, 3);
    public static final Difficulty WTF    = new Difficulty(20, 20, 15.0F, 7.5F, Difficulty.FIXED_PIPE_MAP_WTF, 2, 5);

    private final int                                 width;
    private final int                                 height;
    private final float                               delaySeconds;
    private final float                               progressIncrement;
    private final Map<Class<? extends Pipe>, Integer> fixedPieces;
    private final int                                 entries;
    private final int                                 exits;

    Difficulty(int width,
               int height,
               float delaySeconds,
               float pipeSeconds,
               Map<Class<? extends Pipe>, Integer> fixedPieces,
               int entries,
               int exits) {
        this.width             = width;
        this.height            = height;
        this.delaySeconds      = delaySeconds;
        this.progressIncrement = Pipe.MAX_PROGRESS / pipeSeconds;
        this.fixedPieces       = fixedPieces;
        this.entries           = entries;
        this.exits             = exits;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getDelaySeconds() {
        return delaySeconds;
    }

    public float getProgressIncrement() {
        return progressIncrement;
    }

    public Map<Class<? extends Pipe>, Integer> getFixedPieces() {
        return fixedPieces;
    }

    public int getEntries() {
        return entries;
    }

    public int getExits() {
        return exits;
    }
}
