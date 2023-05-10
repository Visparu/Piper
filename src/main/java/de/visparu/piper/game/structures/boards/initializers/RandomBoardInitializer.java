package de.visparu.piper.game.structures.boards.initializers;

import de.visparu.piper.game.structures.boards.Board;
import de.visparu.piper.game.structures.fields.Field;
import de.visparu.piper.game.structures.pipes.Pipe;
import de.visparu.piper.game.structures.pipes.standard.StraightPipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomBoardInitializer implements BoardInitializer {
    private final Random                              rand;
    private final int                                 entries;
    private final int                                 exits;
    private final Map<Class<? extends Pipe>, Integer> fixedPieces;
    private final List<Field>                         entryFields;
    private final List<Field>                         exitFields;
    private final List<Field>                         fixedFields;

    public RandomBoardInitializer(Random rand,
                                  int entries,
                                  int exits,
                                  Map<Class<? extends Pipe>, Integer> fixedPieces) {
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
        this.generateEntryFields(board);
        this.generateExitFields(board);
        this.generateFixedFields(board);
    }

    private void generateEntryFields(Board board) {
        List<Field> possibleEntryFields = new ArrayList<>(IntStream.range(0, board.getRowCount())
                                                                   .mapToObj(y -> board.getField(0, y))
                                                                   .toList());
        IntStream.range(0, this.entries)
                 .forEach(unused -> {
                     int   entryIndex = this.rand.nextInt(possibleEntryFields.size());
                     Field entryField = possibleEntryFields.get(entryIndex);
                     Pipe  entryPipe  = new StraightPipe(Pipe.Direction.EAST, Pipe.Direction.WEST);
                     entryField.setPipe(entryPipe);
                     entryField.setEntry(true);
                     possibleEntryFields.remove(entryIndex);
                     this.entryFields.add(entryField);
                 });
    }

    private void generateExitFields(Board board) {
        List<Field> possibleExitFields = new ArrayList<>(IntStream.range(0, board.getColumnCount())
                                                                  .mapToObj(y -> board.getField(board.getColumnCount() - 1, y))
                                                                  .toList());
        IntStream.range(0, this.exits)
                 .forEach(unused -> {
                     int   exitIndex = rand.nextInt(possibleExitFields.size());
                     Field exitField = possibleExitFields.get(exitIndex);
                     Pipe  exitPipe  = new StraightPipe(Pipe.Direction.EAST, Pipe.Direction.WEST);
                     exitField.setPipe(exitPipe);
                     exitField.setExit(true);
                     possibleExitFields.remove(exitIndex);
                     this.exitFields.add(exitField);
                 });
    }

    private void generateFixedFields(Board board) {
        List<Field> proximityFields = new ArrayList<>();
        proximityFields.addAll(this.entryFields);
        proximityFields.addAll(this.exitFields);
        this.fixedPieces.forEach((pipeClass, amount) -> IntStream.range(0, amount)
                                                                 .forEach(unused -> {
                                                                     Pipe fixedPipe;
                                                                     try {
                                                                         fixedPipe = pipeClass.getConstructor()
                                                                                              .newInstance();
                                                                     } catch (Exception e) {
                                                                         throw new RuntimeException(e);
                                                                     }
                                                                     fixedPipe.setDirection(Pipe.Direction.values()[rand.nextInt(4)]);
                                                                     List<Field> possibleFields = this.findPotentialFixedFields(board,
                                                                                                                                proximityFields);
                                                                     int   fixedFieldIndex = rand.nextInt(possibleFields.size());
                                                                     Field newFixedField   = possibleFields.get(fixedFieldIndex);
                                                                     newFixedField.setFixed(true);
                                                                     newFixedField.setPipe(fixedPipe);
                                                                     proximityFields.add(newFixedField);
                                                                     this.fixedFields.add(newFixedField);
                                                                 }));
    }

    private List<Field> findPotentialFixedFields(Board board,
                                                 List<Field> proximityFields) {
        return IntStream.range(0, board.getRowCount())
                        .filter(y -> y != 0)
                        .filter(y -> y != board.getRowCount() - 1)
                        .boxed()
                        .flatMap(y -> this.findPotentialFixedFieldsForRow(board, proximityFields, y))
                        .toList();
    }

    private Stream<Field> findPotentialFixedFieldsForRow(Board board,
                                                         List<Field> proximityFields,
                                                         int y) {
        return IntStream.range(0, board.getColumnCount())
                        .filter(x -> x != 0)
                        .filter(x -> x != board.getColumnCount() - 1)
                        .filter(x -> proximityFields.stream()
                                                    .noneMatch(proximityField -> this.isNextToProximityField(x, y, proximityField)))
                        .mapToObj(x -> board.getField(x, y));
    }

    private boolean isNextToProximityField(int x,
                                           int y,
                                           Field proximityField) {
        return Math.abs(x - proximityField.getBoardX()) <= 1 && Math.abs(y - proximityField.getBoardY()) <= 1;
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
