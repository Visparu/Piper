package de.visparu.piper.structures.boards;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.Pipe.Direction;
import de.visparu.piper.structures.pipes.standard.CrossPipe;
import de.visparu.piper.structures.pipes.standard.CurvedPipe;
import de.visparu.piper.structures.pipes.standard.DeadEndPipe;
import de.visparu.piper.structures.pipes.standard.SplitPipe;
import de.visparu.piper.structures.pipes.standard.StraightPipe;
import de.visparu.piper.ui.Input;

public class Board {
    public static final Color COLOR_PAUSE = Color.WHITE;

    private final Map<Field, Point> coordinates = new HashMap<>();
    private final Field[][]         fields;

    private final List<Field> entryFields;
    private final List<Field> exitFields;
    private final List<Field> fixedFields;

    private final float progressIncrement;
    private       int   startTimer;

    private boolean paused = false;
    private boolean won    = false;
    private boolean lost   = false;

    public Board(int width,
                 int height,
                 int startTimer,
                 float progressIncrement,
                 int[] fixedPieces,
                 int entries,
                 int exits,
                 Random rand) {
        this.startTimer        = startTimer;
        this.progressIncrement = progressIncrement;

        this.fields = new Field[height][width];
        for (int y = 0; y < this.fields.length; y++) {
            for (int x = 0; x < this.fields[y].length; x++) {
                Field field = new Field();
                this.fields[y][x] = field;
                this.coordinates.put(field, new Point(x, y));
            }
        }

        // Start fields
        this.entryFields = new ArrayList<>();
        List<Field> possibleEntryFields = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            possibleEntryFields.add(this.fields[y][0]);
        }
        for (int i = 0; i < entries; i++) {
            int   entryIndex = rand.nextInt(possibleEntryFields.size());
            Field entryField = possibleEntryFields.get(entryIndex);
            Pipe  entryPipe  = new StraightPipe(Direction.EAST, Direction.WEST);
            entryField.setPipe(entryPipe);
            entryField.setEntry(true);
            possibleEntryFields.remove(entryIndex);
            this.entryFields.add(entryField);
        }

        // End fields
        this.exitFields = new ArrayList<>();
        List<Field> possibleExitFields = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            possibleExitFields.add(this.fields[y][width - 1]);
        }
        for (int i = 0; i < exits; i++) {
            int   exitIndex = rand.nextInt(possibleExitFields.size());
            Field exitField = possibleExitFields.get(exitIndex);
            Pipe  exitPipe  = new StraightPipe(Direction.EAST, Direction.WEST);
            exitField.setPipe(exitPipe);
            exitField.setExit(true);
            possibleExitFields.remove(exitIndex);
            this.exitFields.add(exitField);
        }

        // Fixed Fields
        this.fixedFields = new ArrayList<>();
        List<Field> proximityFields = new ArrayList<>();
        proximityFields.addAll(this.entryFields);
        proximityFields.addAll(this.exitFields);
        for (int type = 0; type < fixedPieces.length; type++) {
            for (int j = 0; j < fixedPieces[type]; j++) {
                Pipe fixedPipe;
                switch (type) {
                    case Settings.ID_PIPE_STRAIGHT: {
                        fixedPipe = new StraightPipe();
                        break;
                    }
                    case Settings.ID_PIPE_CURVED: {
                        fixedPipe = new CurvedPipe();
                        break;
                    }
                    case Settings.ID_PIPE_SPLIT: {
                        fixedPipe = new SplitPipe();
                        break;
                    }
                    case Settings.ID_PIPE_CROSS: {
                        fixedPipe = new CrossPipe();
                        break;
                    }
                    case Settings.ID_PIPE_DEADEND: {
                        fixedPipe = new DeadEndPipe();
                        break;
                    }
                    default: {
                        throw new IllegalStateException();
                    }
                }
                int direction = rand.nextInt(4);
                switch (direction) {
                    case 0: {
                        fixedPipe.setDirection(Direction.NORTH);
                        break;
                    }
                    case 1: {
                        fixedPipe.setDirection(Direction.EAST);
                        break;
                    }
                    case 2: {
                        fixedPipe.setDirection(Direction.SOUTH);
                        break;
                    }
                    case 3: {
                        fixedPipe.setDirection(Direction.WEST);
                        break;
                    }
                    default: {
                        throw new IllegalStateException();
                    }
                }
                List<Field> possibleFields = new ArrayList<>();
                for (int y = 0; y < this.fields.length; y++) {
                    for (int x = 0; x < this.fields.length; x++) {
                        if (x == 0 || y == 0) {
                            continue;
                        }
                        if (x == this.getWidth() - 1 || y == this.getHeight() - 1) {
                            continue;
                        }
                        boolean proximityFlag = false;
                        for (int i = 0; i < proximityFields.size(); i++) {
                            Field proximityField    = proximityFields.get(i);
                            Point proximityPosition = this.getFieldPosition(proximityField);
                            if (Math.abs(x - proximityPosition.x) <= 1 && Math.abs(y - proximityPosition.y) <= 1) {
                                proximityFlag = true;
                                break;
                            }
                        }
                        if (proximityFlag) {
                            continue;
                        }
                        possibleFields.add(this.fields[y][x]);
                    }
                }

                int   fixedFieldIndex = rand.nextInt(possibleFields.size());
                Field newFixedField   = possibleFields.get(fixedFieldIndex);
                newFixedField.setFixed(true);
                newFixedField.setPipe(fixedPipe);
                proximityFields.add(newFixedField);
                this.fixedFields.add(newFixedField);
            }
        }
    }

    public void render(Graphics2D g2d) {
        if (this.paused) {
            g2d.setColor(Board.COLOR_PAUSE);
            g2d.setFont(new Font("Times New Roman", Font.BOLD, 11));
            g2d.drawString("Paused... (Press escape to resume)", 20, 20);
            return;
        }
        for (int y = 0; y < this.fields.length; y++) {
            for (int x = 0; x < this.fields[y].length; x++) {
                Field         field   = this.fields[y][x];
                BufferedImage img     = new BufferedImage(Field.SIZE, Field.SIZE, BufferedImage.TYPE_INT_RGB);
                Graphics2D    g2d_img = img.createGraphics();
                field.render(g2d_img);
                g2d_img.dispose();
                g2d.drawImage(img, x * Field.SIZE, y * Field.SIZE, null);
            }
        }
    }

    public void tick() {
        if (this.paused) {
            return;
        }
        if (this.startTimer > 0) {
            this.startTimer--;
            return;
        }
        if (this.lost || this.won) {
            return;
        }
        Set<Pipe> usedPipes = new HashSet<>();
        for (Field startingField : this.entryFields) {
            this.incrementPipeProgress(startingField, usedPipes);
        }
    }

    private void incrementPipeProgress(Field field,
                                       Set<Pipe> usedPipes) {
        Pipe pipe = field.getPipe();
        if (pipe == null) {
            this.lose(field, null);
            return;
        }
        usedPipes.add(pipe);
        float nextIncrement = pipe.increaseProgress(Input.isKeyDown(KeyEvent.VK_SPACE) ? Settings.ACCELERATED_PROGRESS_INCREMENT : this.progressIncrement);
        if (nextIncrement > 0.0F) {
            if (this.exitFields.contains(field)) {
                this.checkForWin();
            } else {
                this.spread(field, usedPipes);
            }
        }
    }

    private void spread(Field field,
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
                case EAST: {
                    if (p.x + 1 >= this.getWidth()) {
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
                    break;
                }
                case NORTH: {
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
                    break;
                }
                case SOUTH: {
                    if (p.y + 1 >= this.getHeight()) {
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
                    break;
                }
                case WEST: {
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
                    break;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
            if (!usedPipes.contains(nextField.getPipe())) {
                spreadComplete = true;
                this.incrementPipeProgress(nextField, usedPipes);
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
                case EAST: {
                    xt = xf + 1;
                    yt = yf;
                    break;
                }
                case NORTH: {
                    xt = xf;
                    yt = yf - 1;
                    break;
                }
                case SOUTH: {
                    xt = xf;
                    yt = yf + 1;
                    break;
                }
                case WEST: {
                    xt = xf - 1;
                    yt = yf;
                    break;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
            if (xt < 0 || yt < 0) {
                return true;
            }
            if (xt >= this.getWidth() || yt >= this.getHeight()) {
                return true;
            }
            Field nextField = this.fields[yt][xt];
            Pipe  nextPipe  = nextField.getPipe();
            if (nextPipe == null) {
                return true;
            }
            Direction nextEntryPoint;
            switch (d) {
                case EAST: {
                    nextEntryPoint = Direction.WEST;
                    break;
                }
                case NORTH: {
                    nextEntryPoint = Direction.SOUTH;
                    break;
                }
                case SOUTH: {
                    nextEntryPoint = Direction.NORTH;
                    break;
                }
                case WEST: {
                    nextEntryPoint = Direction.EAST;
                    break;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
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
        if (this.won || this.lost) {
            return;
        }
        if (x < 0 || y < 0) {
            return;
        }
        if (x >= this.getWidth() || y >= this.getHeight()) {
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
        if (this.won || this.lost) {
            return;
        }
        if (x < 0 || y < 0) {
            return;
        }
        if (x >= this.getWidth() || y >= this.getHeight()) {
            return;
        }
        this.fields[y][x].rotatePipe();
    }

    public Field getField(int x,
                          int y) {
        return this.fields[y][x];
    }

    public int getWidth() {
        if (this.fields.length == 0) {
            return 0;
        }
        return this.fields[0].length;
    }

    public int getHeight() {
        return this.fields.length;
    }

    public int getCanvasWidth() {
        return this.getWidth() * Field.SIZE;
    }

    public int getCanvasHeight() {
        return this.getHeight() * Field.SIZE;
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
