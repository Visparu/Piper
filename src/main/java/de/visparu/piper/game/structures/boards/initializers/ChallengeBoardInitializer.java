package de.visparu.piper.game.structures.boards.initializers;

import de.visparu.piper.game.structures.boards.Board;
import de.visparu.piper.game.structures.fields.Field;
import de.visparu.piper.game.structures.pipes.Pipe;
import de.visparu.piper.game.structures.pipes.standard.CrossPipe;
import de.visparu.piper.game.structures.pipes.standard.CurvedPipe;
import de.visparu.piper.game.structures.pipes.standard.DeadEndPipe;
import de.visparu.piper.game.structures.pipes.standard.SplitPipe;
import de.visparu.piper.game.structures.pipes.standard.StraightPipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChallengeBoardInitializer implements BoardInitializer {
    private final Random      rand;
    private final List<Field> entryFields;
    private final List<Field> exitFields;
    private final List<Field> fixedFields;

    public ChallengeBoardInitializer(Random rand) {
        this.rand        = rand;
        this.entryFields = new ArrayList<>();
        this.exitFields  = new ArrayList<>();
        this.fixedFields = new ArrayList<>();
    }

    @Override
    public void initialize(Board board) {
        // Start fields
        Field entryField = board.getField(0, 7);
        Pipe  entryPipe  = new StraightPipe(Pipe.Direction.EAST, Pipe.Direction.WEST);
        entryField.setPipe(entryPipe);
        entryField.setEntry(true);
        this.entryFields.add(entryField);

        // End fields
        Field exitField = board.getField(14, 7);
        Pipe  exitPipe  = new StraightPipe(Pipe.Direction.EAST, Pipe.Direction.WEST);
        exitField.setPipe(exitPipe);
        exitField.setExit(true);
        this.exitFields.add(exitField);

        // Fixed Fields
        List<Field> fixedFields = List.of(board.getField(1, 1),
                                          board.getField(5, 1),
                                          board.getField(9, 1),
                                          board.getField(13, 1),
                                          board.getField(3, 2),
                                          board.getField(7, 2),
                                          board.getField(11, 2),
                                          board.getField(2, 3),
                                          board.getField(5, 3),
                                          board.getField(9, 3),
                                          board.getField(12, 3),
                                          board.getField(1, 5),
                                          board.getField(3, 5),
                                          board.getField(5, 5),
                                          board.getField(9, 5),
                                          board.getField(11, 5),
                                          board.getField(13, 5),
                                          board.getField(7, 6),
                                          board.getField(2, 7),
                                          board.getField(6, 7),
                                          board.getField(8, 7),
                                          board.getField(12, 7),
                                          board.getField(7, 8),
                                          board.getField(1, 13),
                                          board.getField(5, 13),
                                          board.getField(9, 13),
                                          board.getField(13, 13),
                                          board.getField(3, 12),
                                          board.getField(7, 12),
                                          board.getField(11, 12),
                                          board.getField(2, 11),
                                          board.getField(5, 11),
                                          board.getField(9, 11),
                                          board.getField(12, 11),
                                          board.getField(1, 9),
                                          board.getField(3, 9),
                                          board.getField(5, 9),
                                          board.getField(9, 9),
                                          board.getField(11, 9),
                                          board.getField(13, 9),
                                          board.getField(7, 8));
        for (Field fixedField : fixedFields) {
            int nextPipeId = this.rand.nextInt(5);
            Pipe fixedPipe = switch (nextPipeId) {
                case 0 -> new StraightPipe();
                case 1 -> new CurvedPipe();
                case 2 -> new SplitPipe();
                case 3 -> new CrossPipe();
                case 4 -> new DeadEndPipe();
                default -> throw new IllegalStateException();
            };
            int direction = rand.nextInt(4);
            fixedPipe.setDirection(switch (direction) {
                case 0 -> Pipe.Direction.NORTH;
                case 1 -> Pipe.Direction.EAST;
                case 2 -> Pipe.Direction.SOUTH;
                case 3 -> Pipe.Direction.WEST;
                default -> throw new IllegalStateException();
            });

            fixedField.setFixed(true);
            fixedField.setPipe(fixedPipe);
            this.fixedFields.add(fixedField);
        }
    }

    @Override
    public List<Field> getEntryFields() {
        return this.entryFields;
    }

    @Override
    public List<Field> getExitFields() {
        return this.exitFields;
    }

    @Override
    public List<Field> getFixedFields() {
        return this.fixedFields;
    }
}
