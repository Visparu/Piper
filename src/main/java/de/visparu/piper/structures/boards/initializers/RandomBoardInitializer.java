package de.visparu.piper.structures.boards.initializers;

import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.boards.Board;
import de.visparu.piper.structures.fields.Field;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.standard.CrossPipe;
import de.visparu.piper.structures.pipes.standard.CurvedPipe;
import de.visparu.piper.structures.pipes.standard.DeadEndPipe;
import de.visparu.piper.structures.pipes.standard.SplitPipe;
import de.visparu.piper.structures.pipes.standard.StraightPipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBoardInitializer implements BoardInitializer {
    private final Random            rand;
    private final int               entries;
    private final int               exits;
    private final int[]             fixedPieces;
    private final List<Field>       entryFields;
    private final List<Field>       exitFields;
    private final List<Field>       fixedFields;

    public RandomBoardInitializer(Random rand,
                                  int entries,
                                  int exits,
                                  int[] fixedPieces) {
        this.rand        = rand;
        this.entries     = entries;
        this.exits       = exits;
        this.fixedPieces = fixedPieces;
        this.entryFields = new ArrayList<>();
        this.exitFields  = new ArrayList<>();
        this.fixedFields = new ArrayList<>();
    }

    @Override
    public void initialize(Board board) {
        // Start fields
        List<Field> possibleEntryFields = new ArrayList<>();
        for (int y = 0; y < board.getBoardHeight(); y++) {
            possibleEntryFields.add(board.getField(0, y));
        }
        for (int i = 0; i < this.entries; i++) {
            int   entryIndex = this.rand.nextInt(possibleEntryFields.size());
            Field entryField = possibleEntryFields.get(entryIndex);
            Pipe  entryPipe  = new StraightPipe(Pipe.Direction.EAST, Pipe.Direction.WEST);
            entryField.setPipe(entryPipe);
            entryField.setEntry(true);
            possibleEntryFields.remove(entryIndex);
            this.entryFields.add(entryField);
        }

        // End fields
        List<Field> possibleExitFields = new ArrayList<>();
        for (int y = 0; y < board.getBoardHeight(); y++) {
            possibleExitFields.add(board.getField(board.getBoardWidth() - 1, y));
        }
        for (int i = 0; i < exits; i++) {
            int   exitIndex = rand.nextInt(possibleExitFields.size());
            Field exitField = possibleExitFields.get(exitIndex);
            Pipe  exitPipe  = new StraightPipe(Pipe.Direction.EAST, Pipe.Direction.WEST);
            exitField.setPipe(exitPipe);
            exitField.setExit(true);
            possibleExitFields.remove(exitIndex);
            this.exitFields.add(exitField);
        }

        // Fixed Fields
        List<Field> proximityFields = new ArrayList<>();
        proximityFields.addAll(this.entryFields);
        proximityFields.addAll(this.exitFields);
        for (int type = 0; type < fixedPieces.length; type++) {
            for (int j = 0; j < fixedPieces[type]; j++) {
                Pipe fixedPipe = switch (type) {
                    case Settings.ID_PIPE_STRAIGHT -> new StraightPipe();
                    case Settings.ID_PIPE_CURVED -> new CurvedPipe();
                    case Settings.ID_PIPE_SPLIT -> new SplitPipe();
                    case Settings.ID_PIPE_CROSS -> new CrossPipe();
                    case Settings.ID_PIPE_DEADEND -> new DeadEndPipe();
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
                List<Field> possibleFields = new ArrayList<>();
                for (int y = 0; y < board.getBoardHeight(); y++) {
                    for (int x = 0; x < board.getBoardWidth(); x++) {
                        if (x == 0 || y == 0) {
                            continue;
                        }
                        if (x == board.getBoardWidth() - 1 || y == board.getBoardHeight() - 1) {
                            continue;
                        }
                        boolean proximityFlag = false;
                        for (Field proximityField : proximityFields) {
                            if (Math.abs(x - proximityField.getBoardX()) <= 1 && Math.abs(y - proximityField.getBoardY()) <= 1) {
                                proximityFlag = true;
                                break;
                            }
                        }
                        if (proximityFlag) {
                            continue;
                        }
                        possibleFields.add(board.getField(x, y));
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
