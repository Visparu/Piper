package de.visparu.piper.game.structures.pipes;

import java.awt.Graphics2D;
import java.util.Set;

public class PipeRenderer {
    private final Pipe pipe;

    public PipeRenderer(Pipe pipe) {
        this.pipe = pipe;
    }

    public void render(Graphics2D g2d) {
        this.render_background(g2d);
        this.render_fill(g2d);
        this.render_edges(g2d);
    }

    private void render_background(Graphics2D g2d) {
        Set<Pipe.Direction> directions = this.pipe.getOpeningDirections();
        for (Pipe.Direction d : Pipe.Direction.values()) {
            if (directions.contains(d)) {
                int x, y, w, h;
                switch (d) {
                    case EAST -> {
                        x = Pipe.SIZE_LARGE;
                        y = Pipe.SIZE_MEDIUM;
                        w = Pipe.SIZE_MEDIUM;
                        h = Pipe.SIZE_SMALL;
                    }
                    case NORTH -> {
                        x = Pipe.SIZE_MEDIUM;
                        y = 0;
                        w = Pipe.SIZE_SMALL;
                        h = Pipe.SIZE_MEDIUM;
                    }
                    case SOUTH -> {
                        x = Pipe.SIZE_MEDIUM;
                        y = Pipe.SIZE_LARGE;
                        w = Pipe.SIZE_SMALL;
                        h = Pipe.SIZE_MEDIUM;
                    }
                    case WEST -> {
                        x = 0;
                        y = Pipe.SIZE_MEDIUM;
                        w = Pipe.SIZE_MEDIUM;
                        h = Pipe.SIZE_SMALL;
                    }
                    default -> throw new IllegalStateException();
                }

                g2d.setColor(Pipe.COLOR_PIPE);
                g2d.fillRect(x, y, w, h);
            }
        }
        int x = Pipe.SIZE_MEDIUM;
        int y = Pipe.SIZE_MEDIUM;
        int w = Pipe.SIZE_SMALL;
        int h = Pipe.SIZE_SMALL;
        g2d.setColor(Pipe.COLOR_PIPE);
        g2d.fillRect(x, y, w, h);
    }

    private void render_fill(Graphics2D g2d) {
        this.render_fill_segment1(g2d);
        this.render_fill_segment2(g2d);
        this.render_fill_segment3(g2d);
    }

    private void render_fill_segment1(Graphics2D g2d) {
        float segmentProgress = Math.max(Math.min(this.pipe.getProgress() / Pipe.MAX_PROGRESS_SEGMENT, 1.0F), 0.0F);
        for (Pipe.Direction d : this.pipe.getEntryPoints()) {
            int x, y, w, h;
            switch (d) {
                case EAST -> {
                    int xs = Pipe.SIZE_LARGE;
                    int ys = Pipe.SIZE_MEDIUM;
                    int ws = Pipe.SIZE_MEDIUM;
                    int hs = Pipe.SIZE_SMALL;

                    w = (int) (ws * segmentProgress);
                    h = hs;
                    x = xs + ws - w;
                    y = ys;
                }
                case NORTH -> {
                    int xs = Pipe.SIZE_MEDIUM;
                    int ys = 0;
                    int ws = Pipe.SIZE_SMALL;
                    int hs = Pipe.SIZE_MEDIUM;

                    w = ws;
                    h = (int) (hs * segmentProgress);
                    x = xs;
                    y = ys;
                }
                case SOUTH -> {
                    int xs = Pipe.SIZE_MEDIUM;
                    int ys = Pipe.SIZE_LARGE;
                    int ws = Pipe.SIZE_SMALL;
                    int hs = Pipe.SIZE_MEDIUM;

                    w = ws;
                    h = (int) (hs * segmentProgress);
                    x = xs;
                    y = ys + hs - h;
                }
                case WEST -> {
                    int xs = 0;
                    int ys = Pipe.SIZE_MEDIUM;
                    int ws = Pipe.SIZE_MEDIUM;
                    int hs = Pipe.SIZE_SMALL;

                    w = (int) (ws * segmentProgress);
                    h = hs;
                    x = xs;
                    y = ys;
                }
                default -> throw new IllegalStateException();
            }

            g2d.setColor(Pipe.COLOR_FILL);
            g2d.fillRect(x, y, w, h);
        }
    }

    private void render_fill_segment2(Graphics2D g2d) {
        float segmentProgress = Math.max(Math.min((this.pipe.getProgress() - Pipe.MAX_PROGRESS_SEGMENT) / Pipe.MAX_PROGRESS_SEGMENT, 1.0F), 0.0F);
        int   xs              = Pipe.SIZE_MEDIUM;
        int   ys              = Pipe.SIZE_MEDIUM;
        int   ws              = Pipe.SIZE_SMALL;
        int   hs              = Pipe.SIZE_SMALL;
        for (Pipe.Direction d : this.pipe.getEntryPoints()) {
            int x, y, w, h;
            switch (d) {
                case EAST -> {
                    w = (int) (ws * segmentProgress);
                    h = hs;
                    x = xs + ws - w;
                    y = ys;
                }
                case NORTH -> {
                    w = ws;
                    h = (int) (hs * segmentProgress);
                    x = xs;
                    y = ys;
                }
                case SOUTH -> {
                    w = ws;
                    h = (int) (hs * segmentProgress);
                    x = xs;
                    y = ys + hs - h;
                }
                case WEST -> {
                    w = (int) (ws * segmentProgress);
                    h = hs;
                    x = xs;
                    y = ys;
                }
                default -> throw new IllegalStateException();
            }

            g2d.setColor(Pipe.COLOR_FILL);
            g2d.fillRect(x, y, w, h);
        }
    }

    private void render_fill_segment3(Graphics2D g2d) {
        int   x, y, w, h;
        float segmentProgress = Math.max(Math.min((this.pipe.getProgress() - 2 * Pipe.MAX_PROGRESS_SEGMENT) / Pipe.MAX_PROGRESS_SEGMENT, 1.0F), 0.0F);
        for (Pipe.Direction d : this.pipe.getOpeningDirections()) {
            if (this.pipe.getEntryPoints().contains(d)) {
                continue;
            }
            switch (d) {
                case EAST -> {
                    int xs = Pipe.SIZE_LARGE;
                    int ys = Pipe.SIZE_MEDIUM;
                    int ws = Pipe.SIZE_MEDIUM;
                    int hs = Pipe.SIZE_SMALL;

                    w = (int) (ws * segmentProgress);
                    h = hs;
                    x = xs;
                    y = ys;
                }
                case NORTH -> {
                    int xs = Pipe.SIZE_MEDIUM;
                    int ys = 0;
                    int ws = Pipe.SIZE_SMALL;
                    int hs = Pipe.SIZE_MEDIUM;

                    w = ws;
                    h = (int) (hs * segmentProgress);
                    x = xs;
                    y = ys + hs - h;
                }
                case SOUTH -> {
                    int xs = Pipe.SIZE_MEDIUM;
                    int ys = Pipe.SIZE_LARGE;
                    int ws = Pipe.SIZE_SMALL;
                    int hs = Pipe.SIZE_MEDIUM;

                    w = ws;
                    h = (int) (hs * segmentProgress);
                    x = xs;
                    y = ys;
                }
                case WEST -> {
                    int xs = 0;
                    int ys = Pipe.SIZE_MEDIUM;
                    int ws = Pipe.SIZE_MEDIUM;
                    int hs = Pipe.SIZE_SMALL;

                    w = (int) (ws * segmentProgress);
                    h = hs;
                    x = xs + ws - w;
                    y = ys;
                }
                default -> throw new IllegalStateException();
            }

            g2d.setColor(Pipe.COLOR_FILL);
            g2d.fillRect(x, y, w, h);
        }
    }

    private void render_edges(Graphics2D g2d) {
        Set<Pipe.Direction> directions = this.pipe.getOpeningDirections();
        for (Pipe.Direction d : Pipe.Direction.values()) {
            if (directions.contains(d)) {
                int x, y, w, h;
                switch (d) {
                    case EAST -> {
                        x = Pipe.SIZE_LARGE;
                        y = Pipe.SIZE_MEDIUM;
                        w = Pipe.SIZE_MEDIUM;
                        h = Pipe.SIZE_SMALL;
                    }
                    case NORTH -> {
                        x = Pipe.SIZE_MEDIUM;
                        y = 0;
                        w = Pipe.SIZE_SMALL;
                        h = Pipe.SIZE_MEDIUM;
                    }
                    case SOUTH -> {
                        x = Pipe.SIZE_MEDIUM;
                        y = Pipe.SIZE_LARGE;
                        w = Pipe.SIZE_SMALL;
                        h = Pipe.SIZE_MEDIUM;
                    }
                    case WEST -> {
                        x = 0;
                        y = Pipe.SIZE_MEDIUM;
                        w = Pipe.SIZE_MEDIUM;
                        h = Pipe.SIZE_SMALL;
                    }
                    default -> throw new IllegalStateException();
                }

                g2d.setColor(Pipe.COLOR_PIPE_EDGE);
                g2d.drawRect(x, y, w, h);
            }
        }
        int x = Pipe.SIZE_MEDIUM;
        int y = Pipe.SIZE_MEDIUM;
        int w = Pipe.SIZE_SMALL;
        int h = Pipe.SIZE_SMALL;
        g2d.setColor(Pipe.COLOR_PIPE_EDGE);
        g2d.drawRect(x, y, w, h);
    }
}
