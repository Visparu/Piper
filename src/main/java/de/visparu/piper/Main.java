package de.visparu.piper;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.game.GameFactory;

public class Main {
    public static void main(String[] args) {
        GameContext.get().setGame(new GameFactory().create());
        GameContext.get().initialize();
        GameContext.get().start();
    }
}
