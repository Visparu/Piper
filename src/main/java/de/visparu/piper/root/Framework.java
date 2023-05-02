package de.visparu.piper.root;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import de.visparu.piper.context.GameContext;

public final class Framework {
    public static final boolean DEBUG = true;

    private final GameContext context;

    public final double IDL = 1.5;

    private boolean running;

    public Framework(GameContext context) {
        this.context = context;
    }

    public void init() {
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
        if (Framework.DEBUG) {
            System.out.println("DEBUG MODE ENABLED");
        }

        long startTime = System.nanoTime();

        long lastTick  = startTime;
        long lastInfo  = startTime;

        double deltaInfo  = 1000000000 * this.IDL;

        int ticks  = 0;
        int frames = 0;

        while (this.running) {
            long now = System.nanoTime();

            this.tick((float) ((now - lastTick) / 1_000_000_000.0));
            lastTick = now;
            ticks++;

            this.render();
            frames++;

            if (now > lastInfo + deltaInfo) {
                this.info((int) (ticks / this.IDL), (int) (frames / this.IDL));
                lastInfo = now;
                ticks  = 0;
                frames = 0;
            }
        }
    }

    private void tick(float delta) {
        this.context.getPiper()
                    .getBoard()
                    .tick(delta);
    }

    private void render() {
        BufferStrategy bs_toolbox = this.context.getGameWindow()
                                                .getToolboxCanvasBufferStrategy();
        Graphics2D g2d_toolbox = (Graphics2D) bs_toolbox.getDrawGraphics();

        g2d_toolbox.setColor(Color.BLACK);
        g2d_toolbox.fillRect(0, 0, this.context.getGameWindow()
                                               .getToolboxCanvasWidth(), this.context.getGameWindow()
                                                                                     .getToolboxCanvasHeight());
        this.context.getPiper()
                    .getToolbox()
                    .render(g2d_toolbox);

        BufferStrategy bs_board = this.context.getGameWindow()
                                              .getBoardCanvasBufferStrategy();
        Graphics2D g2d_board = (Graphics2D) bs_board.getDrawGraphics();

        g2d_board.setColor(Color.BLACK);
        g2d_board.fillRect(0, 0, this.context.getGameWindow()
                                             .getBoardCanvasWidth(), this.context.getGameWindow()
                                                                                 .getBoardCanvasHeight());
        this.context.getPiper()
                    .getBoard()
                    .render(g2d_board);

        bs_toolbox.show();
        bs_board.show();
    }

    private void info(int ticks,
                      int frames) {
        if (Framework.DEBUG) {
            System.out.printf("%d Ticks, %d Frames\n", ticks, frames);
        }
    }
}
