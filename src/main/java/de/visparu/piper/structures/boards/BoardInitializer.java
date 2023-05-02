package de.visparu.piper.structures.boards;

import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.fields.Field;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.standard.CrossPipe;
import de.visparu.piper.structures.pipes.standard.CurvedPipe;
import de.visparu.piper.structures.pipes.standard.DeadEndPipe;
import de.visparu.piper.structures.pipes.standard.SplitPipe;
import de.visparu.piper.structures.pipes.standard.StraightPipe;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BoardInitializer {
    private final Board             board;
    private final Random            rand;
    private final int               entries;
    private final int               exits;
    private final int[]       fixedPieces;
    private final Field[][]   fields;
    private final List<Field> entryFields;
    private final List<Field>       exitFields;
    private final List<Field>       fixedFields;
    private final Map<Field, Point> coordinates;

    public BoardInitializer(Board board,
                            Random rand,
                            int entries,
                            int exits,
                            int[] fixedPieces,
                            Field[][] fields,
                            List<Field> entryFields,
                            List<Field> exitFields,
                            List<Field> fixedFields,
                            Map<Field, Point> coordinates) {
        this.board       = board;
        this.rand        = rand;
        this.entries     = entries;
        this.exits       = exits;
        this.fixedPieces = fixedPieces;
        this.fields      = fields;
        this.entryFields = entryFields;
        this.exitFields  = exitFields;
        this.fixedFields = fixedFields;
        this.coordinates = coordinates;
    }

    public void initialize() {
        for (int y = 0; y < this.fields.length; y++) {
            for (int x = 0; x < this.fields[y].length; x++) {
                Field field = new Field(this.board);
                this.fields[y][x] = field;
                this.coordinates.put(field, new Point(x, y));
            }
        }

        // Start fields
        List<Field> possibleEntryFields = new ArrayList<>();
        for (Field[] field : this.fields) {
            possibleEntryFields.add(field[0]);
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
        for (Field[] field : this.fields) {
            possibleExitFields.add(field[field.length - 1]);
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
                for (int y = 0; y < this.fields.length; y++) {
                    for (int x = 0; x < this.fields.length; x++) {
                        if (x == 0 || y == 0) {
                            continue;
                        }
                        if (x == this.fields[y].length - 1 || y == this.fields.length - 1) {
                            continue;
                        }
                        boolean proximityFlag = false;
                        for (Field proximityField : proximityFields) {
                            Point proximityPosition = this.coordinates.get(proximityField);
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
}
