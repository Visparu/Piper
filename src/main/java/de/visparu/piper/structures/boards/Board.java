package de.visparu.piper.structures.boards;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.visparu.piper.structures.boards.initializers.BoardInitializer;
import de.visparu.piper.structures.fields.Field;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.Pipe.Direction;

public class Board {
    public static final Color COLOR_PAUSE = Color.WHITE;

    private final BoardFlowController boardFlowController;
    private final BoardRenderer       boardRenderer;
    private final BoardInitializer    boardInitializer;

    private final Map<Field, Point> coordinates;
    private final Field[][]         fields;

    private List<Field> entryFields;
    private List<Field> exitFields;
    private List<Field> fixedFields;

    private boolean paused = false;
    private boolean won    = false;
    private boolean lost   = false;

    public Board(int width,
                 int height,
                 BoardFlowController boardFlowController,
                 BoardRenderer boardRenderer,
                 BoardInitializer boardInitializer) {
        this.fields      = new Field[height][width];
        this.entryFields = new ArrayList<>();
        this.exitFields  = new ArrayList<>();
        this.fixedFields = new ArrayList<>();
        this.coordinates = new HashMap<>();

        this.boardFlowController = boardFlowController;
        this.boardRenderer       = boardRenderer;
        this.boardInitializer    = boardInitializer;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Field field = new Field(x, y);
                this.fields[y][x] = field;
                this.coordinates.put(field, new Point(x, y));
            }
        }
    }

    public void initialize() {
        boardFlowController.initialize(this);
        boardRenderer.initialize(this);
        boardInitializer.initialize(this);

        this.entryFields = boardInitializer.getEntryFields();
        this.exitFields  = boardInitializer.getExitFields();
        this.fixedFields = boardInitializer.getFixedFields();
    }

    public void render(Graphics2D g2d) {
        this.boardRenderer.render(g2d, this.paused);
    }

    public void tick(float delta) {
        if (this.paused) {
            return;
        }
        this.boardFlowController.tick(delta);
        this.checkForLoss();
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
            boolean fixedFieldsCovered = true;
            for (Field fixedField : this.fixedFields) {
                Pipe fixedPipe = fixedField.getPipe();
                if (fixedPipe.getProgress() != Pipe.MAX_PROGRESS) {
                    fixedField.setAsLossField();
                    fixedFieldsCovered = false;
                }
            }
            if (fixedFieldsCovered) {
                this.won = true;
            }
        }
    }

    public void checkForLoss() {
        for (Field[] fieldRow : this.fields) {
            for (Field field : fieldRow) {
                if (field.isLossField()) {
                    this.lost = true;
                    return;
                }
            }
        }
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

    public int getBoardWidth() {
        return this.fields[0].length;
    }

    public int getBoardHeight() {
        return this.fields.length;
    }

    public Field getField(int x,
                          int y) {
        return this.fields[y][x];
    }

    public List<Field> getEntryFields() {
        return this.entryFields;
    }

    public List<Field> getExitFields() {
        return this.exitFields;
    }

    public List<Field> getFixedFields() {
        return this.fixedFields;
    }

    public Map<Field, Point> getCoordinates() {
        return this.coordinates;
    }
}
