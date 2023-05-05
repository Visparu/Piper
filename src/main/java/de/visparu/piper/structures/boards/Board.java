package de.visparu.piper.structures.boards;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.visparu.piper.structures.fields.Field;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.Pipe.Direction;

public class Board {
    public static final Color COLOR_PAUSE = Color.WHITE;

    private final BoardRenderer boardRenderer;
    private final BoardFlowController boardFlowController;

    private final Map<Field, Point> coordinates;
    private final Field[][]         fields;

    private final List<Field> entryFields;
    private final List<Field> exitFields;
    private final List<Field> fixedFields;

    private       float startDelaySeconds;

    private boolean paused = false;
    private boolean won    = false;
    private boolean lost   = false;

    public Board(int width,
                 int height,
                 float startDelaySeconds,
                 float progressIncrement,
                 int[] fixedPieces,
                 int entries,
                 int exits,
                 Random rand) {
        this.startDelaySeconds = startDelaySeconds;
        this.fields            = new Field[height][width];
        this.entryFields       = new ArrayList<>();
        this.exitFields        = new ArrayList<>();
        this.fixedFields       = new ArrayList<>();
        this.coordinates       = new HashMap<>();

        BoardInitializer boardInitializer = new BoardInitializer(this, rand, entries, exits, fixedPieces, this.fields, this.entryFields, this.exitFields, this.fixedFields, this.coordinates);
        this.boardRenderer = new BoardRenderer(this.fields);
        this.boardFlowController = new BoardFlowController(this, progressIncrement);

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
            this.boardFlowController.incrementPipeProgress(delta, startingField, usedPipes);
        }
    }

    public void checkForWin() {
        boolean noLeakLeft = true;
        for (Field entryField : this.entryFields) {
            if (this.boardFlowController.hasLeftoverLeak(entryField, Direction.WEST)) {
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
        if (isModificationInvalid(x, y)) {
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
        if (isModificationInvalid(x, y)) {
            return;
        }
        this.fields[y][x].rotatePipe();
    }

    private boolean isModificationInvalid(int x,
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

    public Field[][] getFields() {
        return this.fields;
    }

    public List<Field> getEntryFields() {
        return this.entryFields;
    }

    public List<Field> getExitFields() {
        return this.exitFields;
    }

    public Map<Field, Point> getCoordinates() {
        return this.coordinates;
    }
}
