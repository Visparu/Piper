package de.visparu.piper;

import de.visparu.piper.context.GameContext;

public class Main {
    public static void main(String[] args) {
        GameContext.get().initialize();
        GameContext.get().getGame().newGame();
        GameContext.get().start();
    }
}
