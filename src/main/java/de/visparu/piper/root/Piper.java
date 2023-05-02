package de.visparu.piper.root;

import java.util.Random;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.boards.Board;
import de.visparu.piper.structures.Toolbox;
import de.visparu.piper.ui.GameWindow;

public final class Piper {
    private final GameContext context;

    private Board   board;
    private Toolbox toolbox;

    public Piper(GameContext context) {
        this.context = context;
    }

    public void init() {
        this.newGame();
    }

    public Board getBoard() {
        return this.board;
    }

    public Toolbox getToolbox() {
        return this.toolbox;
    }

    public void newGame() {
        this.newGame(new Random());
    }

    public void newGame(Random rand) {
        Board   board   = new Board(this.context, Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT, Settings.START_DELAY_SECONDS, Settings.PROGRESS_INCREMENT, Settings.BOARD_FIXED_PIECES, Settings.BOARD_ENTRIES, Settings.BOARD_EXITS, rand);
        Toolbox toolbox = new Toolbox(this.context, rand);
        this.board   = board;
        this.toolbox = toolbox;
        this.context.getGameWindow().resize();
    }
}
