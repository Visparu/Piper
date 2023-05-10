package de.visparu.piper.game.structures.pipes;

import de.visparu.piper.game.structures.fields.Field;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

public abstract class Pipe {
    public static final Color COLOR_PIPE_EDGE = Color.DARK_GRAY;
    public static final Color COLOR_PIPE      = Color.GRAY;
    public static final Color COLOR_FILL      = Color.GREEN;

    public static final int   PIPE_WIDTH           = 10;
    public static final int   SIZE_SMALL           = Pipe.PIPE_WIDTH;
    public static final int   SIZE_MEDIUM          = (int) (Field.SIZE / 2.0F - Pipe.PIPE_WIDTH / 2.0F);
    public static final int   SIZE_LARGE           = (int) (Field.SIZE / 2.0F + Pipe.PIPE_WIDTH / 2.0F);
    public static final float MAX_PROGRESS_SEGMENT = Pipe.MAX_PROGRESS / 3;

    public static final float MAX_PROGRESS = 100.0F;

    private final PipeRenderer pipeRenderer;

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private       Direction      direction;
    private final Set<Direction> entryPoints = new HashSet<>();
    private       float          progress;

    public Pipe() {
        this(Direction.NORTH);
    }

    public Pipe(Direction direction) {
        this(direction, 0.0F);
    }

    public Pipe(Direction direction,
                float progress) {
        this.direction    = direction;
        this.progress     = progress;
        this.pipeRenderer = new PipeRenderer(this);
    }

    @Override
    public String toString() {
        StringBuilder sb1 = new StringBuilder();
        for (Direction d : this.getOpeningDirections()) {
            sb1.append(", ");
            sb1.append(d);
        }
        String openings = "";
        if (sb1.length() >= 2) {
            openings = sb1.substring(2);
        }

        StringBuilder sb2 = new StringBuilder();
        for (Direction d : this.entryPoints) {
            sb2.append(", ");
            sb2.append(d);
        }
        String entryPoints = "";
        if (sb2.length() >= 2) {
            entryPoints = sb2.substring(2);
        }

        return String.format("%s: {Direction: %s, Progress: %s, Openings: {%s}, Entry points: {%s}", this.getClass()
                                                                                                        .getSimpleName(), this.direction, this.progress, openings, entryPoints);
    }

    public void render(Graphics2D g2d) {
        this.pipeRenderer.render(g2d);
    }

    public float increaseProgress(float progress) {
        this.progress += progress;
        if (this.progress > Pipe.MAX_PROGRESS) {
            float overflow = this.progress - Pipe.MAX_PROGRESS;
            this.progress = Pipe.MAX_PROGRESS;
            return overflow;
        } else {
            return 0.0F;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Set<Direction> getEntryPoints() {
        return this.entryPoints;
    }

    public void addEntryPoint(Direction entryPoint) {
        if (!this.getOpeningDirections()
                 .contains(entryPoint)) {
            throw new IllegalArgumentException();
        }
        this.entryPoints.add(entryPoint);
    }

    public float getProgress() {
        return progress;
    }

    public abstract Set<Direction> getOpeningDirections();
}
