package de.visparu.piper.game.structures.boards.initializers;

import de.visparu.piper.game.structures.boards.Board;
import de.visparu.piper.game.structures.fields.Field;

import java.util.List;

public interface BoardInitializer {
    void initialize(Board board);

    List<Field> getEntryFields();

    List<Field> getExitFields();

    List<Field> getFixedFields();
}
