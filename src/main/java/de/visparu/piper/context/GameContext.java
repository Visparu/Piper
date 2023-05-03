package de.visparu.piper.context;

import de.visparu.piper.root.Framework;
import de.visparu.piper.root.Game;
import de.visparu.piper.ui.GameWindow;
import de.visparu.piper.ui.Input;

public class GameContext {
    private static GameContext instance;

    public static GameContext get() {
        if (GameContext.instance == null) {
            GameContext.instance = new GameContext();
        }
        return GameContext.instance;
    }

    private final GameWindow gameWindow;
    private final Input     input;
    private final Game      game;
    private final Framework framework;

    private GameContext() {
        this.gameWindow = new GameWindow();
        this.input     = new Input();
        this.game      = new Game();
        this.framework = new Framework();
    }

    public void initialize() {
        this.gameWindow.initialize();
        this.game.initialize();
    }

    public void start() {
        this.framework.start();
    }

    public GameWindow getGameWindow() {
        return this.gameWindow;
    }

    public Input getInput() {
        return this.input;
    }

    public Game getGame() {
        return this.game;
    }
}
