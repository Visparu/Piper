package de.visparu.piper.framework;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.ui.GameWindow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public final class Framework {
    private boolean running;

    public Framework() {
        this.running = false;
    }

    public void start() {
        if (this.running) {
            throw new IllegalStateException();
        }
        this.running = true;
        this.run();
    }

    private void run() {
        long lastTick = System.nanoTime();

        while (this.running) {
            long now = System.nanoTime();

            this.tick((float) ((now - lastTick) / 1_000_000_000.0));
            lastTick = now;

            this.render();
        }
    }

    private void tick(float delta) {
        GameContext.get()
                   .getGame()
                   .update(delta);
    }

    private void render() {
        GameWindow gameWindow = GameContext.get()
                                           .getGameWindow();

        BufferStrategy bs  = gameWindow.getCanvasBufferStrategy();
        Graphics2D     g2d = (Graphics2D) bs.getDrawGraphics();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, gameWindow.getCanvasWidth(), gameWindow.getCanvasHeight());

        GameContext.get()
                   .getGame()
                   .render(g2d);

        bs.show();
    }
}
