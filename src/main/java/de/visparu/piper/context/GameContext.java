package de.visparu.piper.context;

import de.visparu.piper.root.Framework;
import de.visparu.piper.root.Piper;
import de.visparu.piper.ui.GameWindow;
import de.visparu.piper.ui.Input;

public class GameContext {
    private final GameWindow gameWindow;
    private final Input      input;
    private final Piper      piper;
    private final Framework  framework;

    public GameContext() {
        this.gameWindow = new GameWindow(this);
        this.input = new Input(this);
        this.piper = new Piper(this);
        this.framework = new Framework(this);
    }

    public void initialize() {
        this.gameWindow.init();
        this.piper.init();
        this.framework.init();
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

    public Piper getPiper() {
        return this.piper;
    }

    public Framework getFramework() {
        return this.framework;
    }
}
