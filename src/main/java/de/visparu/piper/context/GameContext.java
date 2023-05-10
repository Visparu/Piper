package de.visparu.piper.context;

import de.visparu.piper.framework.Framework;
import de.visparu.piper.game.Game;
import de.visparu.piper.ui.GameWindow;
import de.visparu.piper.ui.io.InputController;

public class GameContext {
    private static GameContext instance;

    public static GameContext get() {
        if (GameContext.instance == null) {
            GameContext.instance = new GameContext();
        }
        return GameContext.instance;
    }

    private final GameWindow      gameWindow;
    private final InputController inputController;
    private final Framework       framework;
    private Game  game;

    private GameContext() {
        this.gameWindow      = new GameWindow();
        this.inputController = new InputController();
        this.framework       = new Framework();
    }

    public void initialize() {
        this.gameWindow.initialize();
    }

    public void start() {
        this.framework.start();
    }

    public void setGame(Game game) {
        if (this.game != null) {
            this.game.dispose();
        }
        this.game = game;
        this.game.initialize();
        this.gameWindow.resize(this.game.getDimension());
    }

    public GameWindow getGameWindow() {
        return this.gameWindow;
    }

    public InputController getInputController() {
        return this.inputController;
    }

    public Game getGame() {
        return this.game;
    }
}
