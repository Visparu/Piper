package de.visparu.piper.structures.boards;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.fields.Field;
import de.visparu.piper.structures.pipes.Pipe;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class BoardFlowController {
    private final Board board;
    private       float startDelaySeconds;
    private final float progressIncrement;

    public BoardFlowController(final Board board,
                               final float startDelaySeconds,
                               final float progressIncrement) {
        this.board             = board;
        this.startDelaySeconds = startDelaySeconds;
        this.progressIncrement = progressIncrement;
    }

    public void tick(float delta) {
        if (this.startDelaySeconds > 0) {
            this.startDelaySeconds -= delta;
            return;
        }
        if (this.board.hasLost() || this.board.hasWon()) {
            return;
        }
        Set<Pipe> usedPipes = new HashSet<>();
        for (Field startingField : this.board.getEntryFields()) {
            this.incrementPipeProgress(delta, startingField, usedPipes);
        }
    }

    private void incrementPipeProgress(float delta,
                                       Field field,
                                       Set<Pipe> usedPipes) {
        Pipe pipe = field.getPipe();
        if (pipe == null) {
            field.setLossField(true);
            return;
        }
        usedPipes.add(pipe);
        float nextIncrement = pipe.increaseProgress(GameContext.get()
                                                               .getInput()
                                                               .isKeyDown(KeyEvent.VK_SPACE) ? Settings.ACCELERATED_PROGRESS_INCREMENT * delta : this.progressIncrement * delta);
        if (nextIncrement > 0.0F) {
            if (this.board.getExitFields()
                          .contains(field)) {
                this.board.checkForWin();
            } else {
                this.spread(delta, field, usedPipes);
            }
        }
    }

    private void spread(float delta,
                        Field field,
                        Set<Pipe> usedPipes) {
        if (this.board.getExitFields()
                      .contains(field)) {
            return;
        }
        Pipe    pipe           = field.getPipe();
        boolean spreadComplete = false;
        for (Pipe.Direction d : pipe.getOpeningDirections()) {
            if (pipe.getEntryPoints()
                    .contains(d)) {
                continue;
            }
            Point p = this.board.getCoordinates()
                                .get(field);
            Field nextField;
            switch (d) {
                case EAST -> {
                    if (p.x + 1 >= this.board.getBoardWidth()) {
                        field.setLossField(true);
                        return;
                    }
                    nextField = this.board.getField(p.x + 1, p.y);
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Pipe.Direction.WEST)) {
                        nextField.setLossField(true);
                        return;
                    }
                    nextPipe.addEntryPoint(Pipe.Direction.WEST);
                }
                case NORTH -> {
                    if (p.y - 1 < 0) {
                        field.setLossField(true);
                        return;
                    }
                    nextField = this.board.getField(p.x, p.y - 1);
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Pipe.Direction.SOUTH)) {
                        nextField.setLossField(true);
                        return;
                    }
                    nextPipe.addEntryPoint(Pipe.Direction.SOUTH);
                }
                case SOUTH -> {
                    if (p.y + 1 >= this.board.getBoardHeight()) {
                        field.setLossField(true);
                        return;
                    }
                    nextField = this.board.getField(p.x, p.y + 1);
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Pipe.Direction.NORTH)) {
                        nextField.setLossField(true);
                        return;
                    }
                    nextPipe.addEntryPoint(Pipe.Direction.NORTH);
                }
                case WEST -> {
                    if (p.x - 1 < 0) {
                        field.setLossField(true);
                        return;
                    }
                    nextField = this.board.getField(p.x - 1, p.y);
                    Pipe nextPipe = nextField.getPipe();
                    if (nextPipe == null || !nextPipe.getOpeningDirections()
                                                     .contains(Pipe.Direction.EAST)) {
                        nextField.setLossField(true);
                        return;
                    }
                    nextPipe.addEntryPoint(Pipe.Direction.EAST);
                }
                default -> throw new IllegalStateException();
            }
            if (!usedPipes.contains(nextField.getPipe())) {
                spreadComplete = true;
                this.incrementPipeProgress(delta, nextField, usedPipes);
            }
        }
        boolean exitFieldNotReached = false;
        for (Field exitField : this.board.getExitFields()) {
            if (exitField.getPipe()
                         .getProgress() == 0.0F) {
                exitFieldNotReached = true;
                break;
            }
        }
        if (exitFieldNotReached && !spreadComplete) {
            boolean leak = false;
            for (Field entryField : this.board.getEntryFields()) {
                if (hasLeftoverLeak(entryField, Pipe.Direction.WEST)) {
                    leak = true;
                    break;
                }
            }
            if (!leak) {
                this.board.getExitFields()
                          .forEach(exitField -> exitField.setLossField(true));
                this.board.getFixedFields()
                          .stream()
                          .filter(fixedField -> fixedField.getPipe()
                                                          .getProgress() == 0.0F)
                          .forEach(fixedField -> fixedField.setLossField(true));
            }
        }
    }

    public boolean hasLeftoverLeak(Field field,
                                   Pipe.Direction inputDirection) {
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
        if (this.board.getExitFields()
                      .contains(field)) {
            return false;
        }
        for (Pipe.Direction d : pipe.getOpeningDirections()) {
            if (pipe.getEntryPoints()
                    .contains(d)) {
                continue;
            }
            Point pf = this.board.getFieldPosition(field);
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
            if (xt >= this.board.getBoardWidth() || yt >= this.board.getBoardHeight()) {
                return true;
            }
            Field nextField = this.board.getField(xt, yt);
            Pipe  nextPipe  = nextField.getPipe();
            if (nextPipe == null) {
                return true;
            }
            Pipe.Direction nextEntryPoint = switch (d) {
                case EAST -> Pipe.Direction.WEST;
                case NORTH -> Pipe.Direction.SOUTH;
                case SOUTH -> Pipe.Direction.NORTH;
                case WEST -> Pipe.Direction.EAST;
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
}
