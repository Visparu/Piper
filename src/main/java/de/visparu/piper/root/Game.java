package de.visparu.piper.root;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.settings.Settings;
import de.visparu.piper.structures.boards.Board;
import de.visparu.piper.structures.Toolbox;
import de.visparu.piper.structures.boards.BoardFlowController;
import de.visparu.piper.structures.boards.BoardRenderer;
import de.visparu.piper.structures.boards.initializers.BoardInitializer;
import de.visparu.piper.structures.boards.initializers.RandomBoardInitializer;
import de.visparu.piper.structures.fields.Field;
import de.visparu.piper.ui.io.InputObserver;

public final class Game implements InputObserver {
    private Board   board;
    private Toolbox toolbox;

    public void initialize() {
        GameContext.get()
                   .getInput()
                   .registerInputObserver(this);
    }

    @Override
    public void keyPressed(int keycode) {
        switch (keycode) {
            case KeyEvent.VK_F2 -> this.newGame();
            case KeyEvent.VK_ESCAPE -> this.getBoard()
                                           .setPaused(!this.board.isPaused());
        }
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

    public void newGame(Random seed) {
        this.unregisterAsObserver();

        BoardInitializer    boardInitializer    = new RandomBoardInitializer(seed, Settings.BOARD_ENTRIES, Settings.BOARD_EXITS, Settings.BOARD_FIXED_PIECES);
        BoardFlowController boardFlowController = new BoardFlowController(Settings.START_DELAY_SECONDS, Settings.PROGRESS_INCREMENT);
        BoardRenderer          boardRenderer       = new BoardRenderer();

        Board                  board               = new Board(Settings.BOARD_WIDTH, Settings.BOARD_HEIGHT, boardFlowController, boardRenderer, boardInitializer);
        Toolbox                toolbox             = new Toolbox(seed);

        this.board   = board;
        this.toolbox = toolbox;

        this.board.initialize();

        GameContext.get()
                   .getGameWindow()
                   .resize();

        this.registerAsObserver();
    }

    @Override
    public void mousePressed(int button,
                             float x,
                             float y) {
        int xf = (int) (x / Field.SIZE);
        int yf = (int) (y / Field.SIZE);
        if (button == MouseEvent.BUTTON1) {
            if (Game.this.board.hasWon() || Game.this.board.hasLost()) {
                return;
            }
            if (Game.this.board.getField(xf, yf)
                               .getPipe() == null) {
                Game.this.board.addPipe(xf, yf, Game.this.toolbox.pollNextPipe());
            } else {
                Game.this.board.rotatePipe(xf, yf);
            }
        }
    }

    private void registerAsObserver() {
        GameContext.get()
                   .getInput()
                   .registerInputObserver(this);
    }

    private void unregisterAsObserver() {
        GameContext.get()
                   .getInput()
                   .unregisterInputObserver(this);
    }
}
