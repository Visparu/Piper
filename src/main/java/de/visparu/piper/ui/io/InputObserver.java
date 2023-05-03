package de.visparu.piper.ui.io;

public interface InputObserver {
    default void keyPressed(int keycode) {

    }

    @SuppressWarnings("unused")
    default void keyReleased(int keycode) {

    }

    default void mousePressed(int button, float x, float y) {

    }

    @SuppressWarnings("unused")
    default void mouseReleased(int button, float x, float y) {

    }
}
