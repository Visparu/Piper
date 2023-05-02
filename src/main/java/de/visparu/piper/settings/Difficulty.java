package de.visparu.piper.settings;

import de.visparu.piper.root.Framework;
import de.visparu.piper.structures.pipes.Pipe;

public final class Difficulty {
    private static final int[] FIXED_PIPES_EASY = new int[]{1, 1, 1, 0, 0};

    private static final int[] FIXED_PIPES_MEDIUM = new int[]{1, 1, 1, 1, 1};

    private static final int[] FIXED_PIPES_HARD = new int[]{2, 2, 2, 1, 1};

    private static final int[] FIXED_PIPES_WTF = new int[]{3, 3, 3, 4, 3};

    public static final Difficulty EASY   = new Difficulty(10, 10, 10.0F, 15.0F, Difficulty.FIXED_PIPES_EASY, 1, 1);
    public static final Difficulty MEDIUM = new Difficulty(10, 10, 10.0F, 15.0F, Difficulty.FIXED_PIPES_MEDIUM, 1, 2);
    public static final Difficulty HARD   = new Difficulty(15, 15, 15.0F, 12.5F, Difficulty.FIXED_PIPES_HARD, 2, 3);
    public static final Difficulty WTF    = new Difficulty(20, 20, 15.0F, 12.0F, Difficulty.FIXED_PIPES_WTF, 2, 5);

    private final int   width;
    private final int   height;
    private final int   delayTicks;
    private final float progressIncrement;
    private final int[] fixedPieces;
    private final int   entries;
    private final int   exits;

    private Difficulty(int width,
                       int height,
                       float delaySeconds,
                       float pipeSeconds,
                       int[] fixedPieces,
                       int entries,
                       int exits) {
        this.width             = width;
        this.height            = height;
        this.delayTicks        = (int) (delaySeconds * Framework.TPS);
        this.progressIncrement = (float) (Pipe.MAX_PROGRESS / Framework.TPS / pipeSeconds);
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

    public int getDelayTicks() {
        return delayTicks;
    }

    public float getProgressIncrement() {
        return progressIncrement;
    }

    public int[] getFixedPieces() {
        return fixedPieces;
    }

    public int getEntries() {
        return entries;
    }

    public int getExits() {
        return exits;
    }
}
