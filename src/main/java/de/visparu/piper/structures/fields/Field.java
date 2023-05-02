package de.visparu.piper.structures.fields;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import de.visparu.piper.structures.boards.Board;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.Pipe.Direction;

public class Field {
    public static final Color COLOR_BACKGROUND = Color.LIGHT_GRAY;
    public static final Color COLOR_ENTRY_EXIT = Color.YELLOW;
    public static final Color COLOR_EDGE       = Color.BLACK;
    public static final Color COLOR_WON        = new Color(200, 255, 200);
    public static final Color COLOR_LOSS       = Color.RED;

    public static final int SIZE = 50;

    private final Board parentBoard;
    private       Pipe  pipe;

    private boolean entry     = false;
    private boolean exit      = false;
    private boolean fixed     = false;
    private boolean lossField = false;

    public Field() {
        this(null);
    }

    public Field(Board parentBoard) {
        this.parentBoard = parentBoard;
    }

    @Override
    public String toString() {
        Point p = this.parentBoard != null ? this.parentBoard.getFieldPosition(this) : null;
        int x = (p == null) ? -1 : p.x;
        int y = (p == null) ? -1 : p.y;
        return String.format("Field: {x: %d, y: %d, Loss field: %b, %s}", x, y, this.lossField, this.pipe);
    }

    public void render(Graphics2D g2d) {
        if (this.parentBoard != null && this.parentBoard.hasWon()) {
            g2d.setColor(Field.COLOR_WON);
        } else if (this.lossField) {
            g2d.setColor(Field.COLOR_LOSS);
        } else if (this.entry || this.exit) {
            g2d.setColor(Field.COLOR_ENTRY_EXIT);
        } else {
            g2d.setColor(Field.COLOR_BACKGROUND);
        }
        if (this.fixed) {
            g2d.setColor(g2d.getColor()
                            .darker());
        }
        g2d.fillRect(0, 0, Field.SIZE, Field.SIZE);
        if (this.pipe != null) {
            this.pipe.render(g2d);
        }
        g2d.setColor(Field.COLOR_EDGE);
        g2d.drawRect(0, 0, Field.SIZE - 1, Field.SIZE - 1);
    }

    public void rotatePipe() {
        if (this.fixed || this.pipe == null || !this.pipe.getEntryPoints()
                                                         .isEmpty()) {
            return;
        }
        this.pipe.setDirection(switch (this.pipe.getDirection()) {
            case EAST -> Direction.SOUTH;
            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        });
    }

    public Pipe getPipe() {
        return this.pipe;
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }

    public void setEntry(boolean entry) {
        this.entry = entry;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public void setLossField(boolean lossField) {
        this.lossField = lossField;
    }
}
