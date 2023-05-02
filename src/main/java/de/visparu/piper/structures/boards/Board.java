package de.visparu.piper.structures.boards;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.Pipe.Direction;

public class Board {
    public static final Color COLOR_PAUSE = Color.WHITE;

    private final GameContext context;

    private final BoardRenderer boardRenderer;

    private final Map<Field, Point> coordinates;
    private final Field[][]         fields;

    private final List<Field> entryFields;
    private final List<Field> exitFields;
    private final List<Field> fixedFields;

    private final float progressIncrement;
    private       float startDelaySeconds;

    private boolean paused = false;
    private boolean won    = false;
    private boolean lost   = false;

    public Board(GameContext context,
                 int width,
                 int height,
                 float startDelaySeconds,
                 float progressIncrement,
                 int[] fixedPieces,
                 int entries,
                 int exits,
                 Random rand) {
        this.context           = context;
        this.startDelaySeconds = startDelaySeconds;
        this.progressIncrement = progressIncrement;
        this.fields            = new Field[height][width];
        this.entryFields       = new ArrayList<>();
        this.exitFields        = new ArrayList<>();
        this.fixedFields       = new ArrayList<>();
        this.coordinates       = new HashMap<>();

        BoardInitializer boardInitializer = new BoardInitializer(this, rand, entries, exits, fixedPieces, this.fields, this.entryFields, this.exitFields, this.fixedFields, this.coordinates);
        this.boardRenderer = new BoardRenderer(this.fields);

        boardInitializer.initialize();
    }

    public void render(Graphics2D g2d) {
        this.boardRenderer.render(g2d, this.paused);
    }

    public void tick(float delta) {
        if (this.paused) {
            return;
        }
        if (this.startDelaySeconds > 0) {
            this.startDelaySeconds -= delta;
            return;
        }
        if (this.lost || this.won) {
            return;
        }
        Set<Pipe> usedPipes = new HashSet<>();
        for (Field startingField : this.entryFields) {
            this.incrementPipeProgress(delta, startingField, usedPipes);
        }
    }

    private void incrementPipeProgress(float delta,
                                       Field field,
                                       Set<Pipe> usedPipes) {
        Pipe pipe = field.getPipe();
        if (pipe == null) {
            this.lose(field, null);
            return;
        }
        usedPipes.add(pipe);
        float nextIncrement = pipe.increaseProgress(this.context.getInput()
                                                                .isKeyDown(KeyEvent.VK_SPACE) ? Settings.ACCELERATED_PROGRESS_INCREMENT * delta : this.progressIncrement * delta);
        if (nextIncrement > 0.0F) {
            if (this.exitFields.contains(field)) {
                this.checkForWin();
            } else {
                this.spread(delta, field, usedPipes);
            }
        }
    }

    private void spread(float delta,
                        Field field,
                        Set<Pipe> usedPipes) {
        if (this.exitFields.contains(field)) {
            return;
        }
        Pipe    pipe           = field.getPipe();
        boolean spreadComplete = false;
        for (Direction d : pipe.getOpeningDirections()) {
            if (pipe.getEntryPoints()
                    .contains(d)) {
                continue;
            }
            Point p = this.coordinates.get(field);
            Field nextField;
            switch (d) {
                case EAST -> {
                    if (p.x + 1 >= this.fields[0].length) {
                        this.lose(field, null);
                        return;
                    }
                    nextField = this.fields[p.y][p.x + 1];
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Direction.WEST)) {
                        this.lose(field, nextField);
                        return;
                    }
                    nextPipe.addEntryPoint(Direction.WEST);
                }
                case NORTH -> {
                    if (p.y - 1 < 0) {
                        this.lose(field, null);
                        return;
                    }
                    nextField = this.fields[p.y - 1][p.x];
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Direction.SOUTH)) {
                        this.lose(field, nextField);
                        return;
                    }
                    nextPipe.addEntryPoint(Direction.SOUTH);
                }
                case SOUTH -> {
                    if (p.y + 1 >= this.fields.length) {
                        this.lose(field, null);
                        return;
                    }
                    nextField = this.fields[p.y + 1][p.x];
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Direction.NORTH)) {
                        this.lose(field, nextField);
                        return;
                    }
                    nextPipe.addEntryPoint(Direction.NORTH);
                }
                case WEST -> {
                    if (p.x - 1 < 0) {
                        this.lose(field, null);
                        return;
                    }
                    nextField = this.fields[p.y][p.x - 1];
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Direction.EAST)) {
                        this.lose(field, nextField);
                        return;
                    }
                    nextPipe.addEntryPoint(Direction.EAST);
                }
                default -> throw new IllegalStateException();
            }
            if (!usedPipes.contains(nextField.getPipe())) {
                spreadComplete = true;
                this.incrementPipeProgress(delta, nextField, usedPipes);
            }
        }
        boolean exitFieldNotReached = false;
        for (Field exitField : this.exitFields) {
            if (exitField.getPipe()
                         .getProgress() == 0.0F) {
                exitFieldNotReached = true;
                break;
            }
        }
        if (exitFieldNotReached && !spreadComplete) {
            boolean leak = false;
            for (Field entryField : this.entryFields) {
                if (hasLeftoverLeak(entryField, Direction.WEST)) {
                    leak = true;
                    break;
                }
            }
            if (!leak) {
                this.lose(field, null);
            }
        }
    }

    public boolean hasLeftoverLeak(Field field,
                                   Direction inputDirection) {
        Pipe pipe = field.getPipe();
        if (pipe == null) {
            return true;
        }
        if (!pipe.getOpeningDirections()
                 .contains(inputDirection)) {
            return true;
        }
        if (pipe.getProgress() < 100.0F) {
            return true;
        }
        for (Direction d : pipe.getOpeningDirections()) {
            if (this.exitFields.contains(field)) {
                break;
            }
            if (pipe.getEntryPoints()
                    .contains(d)) {
                continue;
            }
            Point pf = this.getFieldPosition(field);
            int   xf = pf.x;
            int   yf = pf.y;
            int   xt, yt;
            switch (d) {
                case EAST -> {
                    xt = xf + 1;
                    yt = yf;
                }
                case NORTH -> {
                    xt = xf;
                    yt = yf - 1;
                }
                case SOUTH -> {
                    xt = xf;
                    yt = yf + 1;
                }
                case WEST -> {
                    xt = xf - 1;
                    yt = yf;
                }
                default -> throw new IllegalStateException();
            }
            if (xt < 0 || yt < 0) {
                return true;
            }
            if (xt >= this.fields[0].length || yt >= this.fields.length) {
                return true;
            }
            Field nextField = this.fields[yt][xt];
            Pipe  nextPipe  = nextField.getPipe();
            if (nextPipe == null) {
                return true;
            }
            Direction nextEntryPoint = switch (d) {
                case EAST -> Direction.WEST;
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                case WEST -> Direction.EAST;
            };
            if (nextPipe.getEntryPoints()
                        .contains(nextEntryPoint)) {
                if (this.hasLeftoverLeak(nextField, nextEntryPoint)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public void checkForWin() {
        boolean noLeakLeft = true;
        for (Field entryField : this.entryFields) {
            if (this.hasLeftoverLeak(entryField, Direction.WEST)) {
                noLeakLeft = false;
                break;
            }
        }

        if (noLeakLeft) {
            for (Field fixedField : this.fixedFields) {
                Pipe fixedPipe = fixedField.getPipe();
                if (fixedPipe.getProgress() != Pipe.MAX_PROGRESS) {
                    this.lose(fixedField, null);
                    return;
                }
            }
            this.won = true;
        }
    }

    public void lose(Field from,
                     Field to) {
        if (to == null) {
            from.setLossField(true);
        } else {
            to.setLossField(true);
        }
        this.lost = true;
    }

    public void addPipe(int x,
                        int y,
                        Pipe pipe) {
        if (isModificationValid(x, y)) {
            return;
        }
        Field field = this.fields[y][x];
        if (field.getPipe() != null) {
            return;
        }
        field.setPipe(pipe);
    }

    public void rotatePipe(int x,
                           int y) {
        if (isModificationValid(x, y)) {
            return;
        }
        this.fields[y][x].rotatePipe();
    }

    private boolean isModificationValid(int x,
                                        int y) {
        if (this.won || this.lost) {
            return true;
        }
        if (x < 0 || y < 0) {
            return true;
        }
        return x >= this.fields[0].length || y >= this.fields.length;
    }

    public Field getField(int x,
                          int y) {
        return this.fields[y][x];
    }

    public int getCanvasWidth() {
        return this.fields[0].length * Field.SIZE;
    }

    public int getCanvasHeight() {
        return this.fields.length * Field.SIZE;
    }

    public Point getFieldPosition(Field field) {
        return this.coordinates.get(field);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean hasWon() {
        return this.won;
    }

    public boolean hasLost() {
        return this.lost;
    }
}
