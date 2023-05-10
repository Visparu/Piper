package de.visparu.piper.ui.io;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class InputController {
    private final Keys  keyAdapter;
    private final Mouse mouseAdapter;

    private final Set<InputObserver> inputObservers;

    private final Set<Integer> keys;

    public InputController() {
        this.keyAdapter = new Keys();
        this.mouseAdapter = new Mouse();
        this.inputObservers = new HashSet<>();
        this.keys = new HashSet<>();
    }

    public void registerInputObserver(InputObserver inputObserver) {
        this.inputObservers.add(inputObserver);
    }

    public void unregisterInputObserver(InputObserver inputObserver) {
        this.inputObservers.remove(inputObserver);
    }

    public Keys getKeyAdapter() {
        return this.keyAdapter;
    }

    public Mouse getMouseAdapter() {
        return this.mouseAdapter;
    }

    public boolean isKeyDown(int keycode) {
        return this.keys.contains(keycode);
    }

    public class Keys extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            InputController.this.keys.add(e.getKeyCode());
            InputController.this.inputObservers.forEach(inputObserver -> inputObserver.keyPressed(e.getKeyCode()));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            InputController.this.keys.remove(e.getKeyCode());
            InputController.this.inputObservers.forEach(inputObserver -> inputObserver.keyReleased(e.getKeyCode()));
        }
    }

    public class Mouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            InputController.this.inputObservers.forEach(inputObserver -> inputObserver.mousePressed(e.getButton(), e.getX(), e.getY()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            InputController.this.inputObservers.forEach(inputObserver -> inputObserver.mouseReleased(e.getButton(), e.getX(), e.getY()));
        }
    }
}
