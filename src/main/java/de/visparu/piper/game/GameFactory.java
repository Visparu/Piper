package de.visparu.piper.game;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.game.settings.Difficulty;
import de.visparu.piper.game.settings.Settings;
import de.visparu.piper.game.structures.Toolbox;
import de.visparu.piper.game.structures.boards.Board;
import de.visparu.piper.game.structures.boards.BoardFlowController;
import de.visparu.piper.game.structures.boards.BoardRenderer;
import de.visparu.piper.game.structures.boards.initializers.BoardInitializer;
import de.visparu.piper.game.structures.boards.initializers.RandomBoardInitializer;
import de.visparu.piper.ui.io.InputObserver;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameFactory {
    public Game create() {
        return this.create(Settings.forDifficulty(Difficulty.EASY));
    }

    public Game create(Settings settings) {
        return this.create(settings, new Random());
    }

    public Game create(Settings settings,
                       Random seed) {
        BoardInitializer boardInitializer = new RandomBoardInitializer(seed,
                                                                       settings.getBoardEntries(),
                                                                       settings.getBoardExits(),
                                                                       settings.getBoardFixedPieces());
        BoardFlowController boardFlowController = new BoardFlowController(settings.getStartDelaySeconds(), settings.getProgressIncrement());
        BoardRenderer       boardRenderer       = new BoardRenderer();

        Board   board   = new Board(settings.getBoardWidth(), settings.getBoardHeight(), boardFlowController, boardRenderer, boardInitializer);
        Toolbox toolbox = new Toolbox(seed);

        return new SimpleGame(board, toolbox);
    }

    public static class SimpleGame implements Game, InputObserver {
        private final Board   board;
        private final Toolbox toolbox;

        private final Rectangle toolboxBounds;
        private final Rectangle boardBounds;

        private SimpleGame(Board board,
                           Toolbox toolbox) {
            this.board   = board;
            this.toolbox = toolbox;

            this.boardBounds   = new Rectangle(this.toolbox.getWidth(), 0, this.board.getDimension().width, this.board.getDimension().height);
            this.toolboxBounds = new Rectangle(0, 0, this.toolbox.getWidth(), this.board.getDimension().height);
        }

        @Override
        public void initialize() {
            this.board.initialize();
            this.toolbox.initialize();
            GameContext.get()
                       .getInputController()
                       .registerInputObserver(this);
        }

        @Override
        public void render(Graphics2D g2d) {
            BufferedImage imageToolbox = new BufferedImage(this.toolboxBounds.width, this.toolboxBounds.height, BufferedImage.TYPE_INT_ARGB);
            BufferedImage imageBoard   = new BufferedImage(this.boardBounds.width, this.boardBounds.height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2dToolbox = imageToolbox.createGraphics();
            Graphics2D g2dBoard   = imageBoard.createGraphics();

            this.toolbox.render(g2dToolbox);
            this.board.render(g2dBoard);

            g2dToolbox.dispose();
            g2dBoard.dispose();

            g2d.drawImage(imageToolbox, this.toolboxBounds.x, this.toolboxBounds.y, null);
            g2d.drawImage(imageBoard, this.boardBounds.x, this.boardBounds.y, null);
        }

        @Override
        public void update(float delta) {
            this.board.update(delta);
        }

        @Override
        public void keyPressed(int keycode) {
            switch (keycode) {
                case KeyEvent.VK_F2 -> GameContext.get()
                                                  .setGame(new GameFactory().create());
                case KeyEvent.VK_ESCAPE -> this.getBoard()
                                               .setPaused(!this.board.isPaused());
            }
        }

        @Override
        public void mousePressed(int button,
                                 float x,
                                 float y) {
            if (this.boardBounds.contains(x, y)) {
                this.board.mousePressed(button, x - this.boardBounds.x, y - this.boardBounds.y);
            }
        }

        @Override
        public Board getBoard() {
            return board;
        }

        @Override
        public Toolbox getToolbox() {
            return toolbox;
        }

        @Override
        public Dimension getDimension() {
            return new Dimension(this.toolboxBounds.width + this.boardBounds.width, this.toolboxBounds.height);
        }

        @Override
        public void dispose() {
            GameContext.get()
                       .getInputController()
                       .unregisterInputObserver(this);
        }
    }
}
