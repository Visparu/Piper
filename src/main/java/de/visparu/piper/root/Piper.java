package de.visparu.piper.root;

import java.util.Random;

import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.boards.Board;
import de.visparu.piper.structures.Toolbox;
import de.visparu.piper.ui.GameWindow;

public final class Piper {
    private static boolean init = false;

    private static Board   board;
    private static Toolbox toolbox;

    private Piper() {
    }

    public static void init() {
        if (Piper.init) {
            throw new IllegalStateException();
        }
        Piper.init = true;

        Piper.newGame();
    }

    public static Board getBoard() {
        return Piper.board;
    }

    public static Toolbox getToolbox() {
        return Piper.toolbox;
    }

    public static void newGame() {
        Piper.newGame(new Random());
    }

    public static void newGame(Random rand) {
        Board   board   = new Board(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT, Settings.START_DELAY_TICKS, Settings.PROGRESS_INCREMENT, Settings.BOARD_FIXED_PIECES, Settings.BOARD_ENTRIES, Settings.BOARD_EXITS, rand);
        Toolbox toolbox = new Toolbox(rand);
        Piper.board   = board;
        Piper.toolbox = toolbox;
        GameWindow.resize();
    }
}
