package de.visparu.piper.root;

import de.visparu.piper.context.GameContext;

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
                   .getBoard()
                   .tick(delta);
    }

    private void render() {
        BufferStrategy bs_toolbox = GameContext.get()
                                               .getGameWindow()
                                               .getToolboxCanvasBufferStrategy();
        Graphics2D g2d_toolbox = (Graphics2D) bs_toolbox.getDrawGraphics();

        g2d_toolbox.setColor(Color.BLACK);
        g2d_toolbox.fillRect(0, 0, GameContext.get()
                                              .getGameWindow()
                                              .getToolboxCanvasWidth(), GameContext.get()
                                                                                   .getGameWindow()
                                                                                   .getToolboxCanvasHeight());
        GameContext.get()
                   .getGame()
                   .getToolbox()
                   .render(g2d_toolbox);

        BufferStrategy bs_board = GameContext.get()
                                             .getGameWindow()
                                             .getBoardCanvasBufferStrategy();
        Graphics2D g2d_board = (Graphics2D) bs_board.getDrawGraphics();

        g2d_board.setColor(Color.BLACK);
        g2d_board.fillRect(0, 0, GameContext.get()
                                            .getGameWindow()
                                            .getBoardCanvasWidth(), GameContext.get()
                                                                               .getGameWindow()
                                                                               .getBoardCanvasHeight());
        GameContext.get()
                   .getGame()
                   .getBoard()
                   .render(g2d_board);

        bs_toolbox.show();
        bs_board.show();
    }
}
