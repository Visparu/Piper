package de.visparu.piper.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.structures.fields.Field;

public class Input {
    private final GameContext context;

    private static Keys  keyAdapter;
    private static Mouse mouseAdapter;

    private static final Set<Integer> keys = new HashSet<>();

    public Input(GameContext context) {
        this.context = context;
    }

    public Keys getKeyAdapter() {
        if (Input.keyAdapter == null) {
            Input.keyAdapter = new Keys();
        }
        return Input.keyAdapter;
    }

    public Mouse getMouseAdapter() {
        if (Input.mouseAdapter == null) {
            Input.mouseAdapter = new Mouse();
        }
        return Input.mouseAdapter;
    }

    public boolean isKeyDown(int keyCode) {
        return Input.keys.contains(keyCode);
    }

    public class Keys extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            Input.keys.add(e.getKeyCode());
            switch (e.getKeyCode()) {
                case KeyEvent.VK_F2 -> Input.this.context.getPiper()
                                                         .newGame();
                case KeyEvent.VK_ESCAPE -> Input.this.context.getPiper()
                                                             .getBoard()
                                                             .setPaused(!Input.this.context.getPiper()
                                                                                           .getBoard()
                                                                                           .isPaused());
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Input.keys.remove(e.getKeyCode());
        }
    }

    public class Mouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int xf = e.getX() / Field.SIZE;
            int yf = e.getY() / Field.SIZE;
            if (e.getButton() == 1) {
                if (Input.this.context.getPiper()
                                      .getBoard()
                                      .hasWon() || Input.this.context.getPiper()
                                                                     .getBoard()
                                                                     .hasLost()) {
                    return;
                }
                if (Input.this.context.getPiper()
                                      .getBoard()
                                      .getField(xf, yf)
                                      .getPipe() == null) {
                    Input.this.context.getPiper()
                                      .getBoard()
                                      .addPipe(xf, yf, Input.this.context.getPiper()
                                                                         .getToolbox()
                                                                         .pollNextPipe());
                } else {
                    Input.this.context.getPiper()
                                      .getBoard()
                                      .rotatePipe(xf, yf);
                }
            }
        }
    }
}
