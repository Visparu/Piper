package de.visparu.piper;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.root.Framework;
import de.visparu.piper.root.Piper;
import de.visparu.piper.ui.GameWindow;

public class Main {
    public static void main(String[] args) {
        GameContext context = new GameContext();
        context.initialize();
        context.start();
    }
}
