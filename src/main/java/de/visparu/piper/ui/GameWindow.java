package de.visparu.piper.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.settings.Difficulty;
import de.visparu.piper.settings.Settings;

public final class GameWindow {
    private static final int CANVAS_BUFFERS = 3;

    private final GameContext context;

    private final JFrame    frame;
    private final JMenuBar  menubar;
    private final JMenu     menu_game;
    private final JMenuItem menu_game_newgame;
    private final JMenuItem menu_game_settings;
    private final JMenuItem menu_game_exit;
    private final JMenu     menu_difficulty;
    private final JMenuItem menu_difficulty_easy;
    private final JMenuItem menu_difficulty_medium;
    private final JMenuItem menu_difficulty_hard;
    private final JMenuItem menu_difficulty_wtf;
    private final Canvas    canvas_toolbox;
    private final Canvas    canvas_board;

    public GameWindow(GameContext context) {
        this.context = context;

        this.frame                  = new JFrame();
        this.menubar                = new JMenuBar();
        this.menu_game              = new JMenu();
        this.menu_game_newgame      = new JMenuItem();
        this.menu_game_settings     = new JMenuItem();
        this.menu_game_exit         = new JMenuItem();
        this.menu_difficulty        = new JMenu();
        this.menu_difficulty_easy   = new JMenuItem();
        this.menu_difficulty_medium = new JMenuItem();
        this.menu_difficulty_hard   = new JMenuItem();
        this.menu_difficulty_wtf    = new JMenuItem();
        this.canvas_toolbox         = new Canvas();
        this.canvas_board           = new Canvas();
    }

    public void init() {
        this.init_configure();
        this.init_connect();
        this.init_listen();
        this.init_present();
    }

    private void init_configure() {
        this.frame.setTitle("Piper");
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menu_game.setText("Game");
        this.menu_game.setMnemonic('g');

        this.menu_game_newgame.setText("New Game");
        this.menu_game_newgame.setMnemonic('n');

        this.menu_game_settings.setText("Settings");
        this.menu_game_settings.setMnemonic('s');
        this.menu_game_settings.setEnabled(false);

        this.menu_game_exit.setText("Exit");
        this.menu_game_exit.setMnemonic('e');

        this.menu_difficulty.setText("Difficulty");
        this.menu_difficulty.setMnemonic('d');

        this.menu_difficulty_easy.setText("Easy");
        this.menu_difficulty_easy.setMnemonic('e');

        this.menu_difficulty_medium.setText("Medium");
        this.menu_difficulty_medium.setMnemonic('m');

        this.menu_difficulty_hard.setText("Hard");
        this.menu_difficulty_hard.setMnemonic('h');

        this.menu_difficulty_wtf.setText("...wtf?");
        this.menu_difficulty_wtf.setMnemonic('w');
    }

    private void init_connect() {
        this.frame.setLayout(new BorderLayout());
        this.frame.add(this.menubar, BorderLayout.NORTH);
        this.frame.add(this.canvas_toolbox, BorderLayout.WEST);
        this.frame.add(this.canvas_board, BorderLayout.EAST);

        this.menubar.add(this.menu_game);
        this.menubar.add(this.menu_difficulty);

        this.menu_game.add(this.menu_game_newgame);
        this.menu_game.add(this.menu_game_settings);
        this.menu_game.add(this.menu_game_exit);

        this.menu_difficulty.add(this.menu_difficulty_easy);
        this.menu_difficulty.add(this.menu_difficulty_medium);
        this.menu_difficulty.add(this.menu_difficulty_hard);
        this.menu_difficulty.add(this.menu_difficulty_wtf);
    }

    private void init_listen() {
        this.canvas_board.addMouseListener(this.context.getInput().getMouseAdapter());
        this.canvas_board.addKeyListener(this.context.getInput().getKeyAdapter());

        this.menu_game_newgame.addActionListener(e -> this.context.getPiper()
                                                              .newGame());
        this.menu_game_settings.addActionListener(e -> {

        });
        this.menu_game_exit.addActionListener(e -> System.exit(0));

        this.menu_difficulty_easy.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.EASY);
            this.context.getPiper()
                        .newGame();
        });
        this.menu_difficulty_medium.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.MEDIUM);
            this.context.getPiper()
                        .newGame();
        });
        this.menu_difficulty_hard.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.HARD);
            this.context.getPiper()
                        .newGame();
        });
        this.menu_difficulty_wtf.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.WTF);
            this.context.getPiper()
                        .newGame();
        });
    }

    private void init_present() {
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public BufferStrategy getToolboxCanvasBufferStrategy() {
        BufferStrategy bs = this.canvas_toolbox.getBufferStrategy();
        if (bs == null) {
            this.canvas_toolbox.createBufferStrategy(GameWindow.CANVAS_BUFFERS);
            bs = this.canvas_toolbox.getBufferStrategy();
        }
        return bs;
    }

    public BufferStrategy getBoardCanvasBufferStrategy() {
        BufferStrategy bs = this.canvas_board.getBufferStrategy();
        if (bs == null) {
            this.canvas_board.createBufferStrategy(GameWindow.CANVAS_BUFFERS);
            bs = this.canvas_board.getBufferStrategy();
        }
        return bs;
    }

    public int getToolboxCanvasWidth() {
        return this.canvas_toolbox.getWidth();
    }

    public int getToolboxCanvasHeight() {
        return this.canvas_toolbox.getHeight();
    }

    public int getBoardCanvasWidth() {
        return this.canvas_board.getWidth();
    }

    public int getBoardCanvasHeight() {
        return this.canvas_board.getHeight();
    }

    private void setToolboxCanvasSize(int width,
                                      int height) {
        this.canvas_toolbox.setSize(width, height);
    }

    private void setBoardCanvasSize(int width,
                                    int height) {
        this.canvas_board.setSize(width, height);
    }

    public void resize() {
        this.setToolboxCanvasSize(this.context.getPiper()
                                              .getToolbox()
                                              .getCanvasWidth(), this.context.getPiper()
                                                                             .getBoard()
                                                                             .getCanvasHeight());
        this.setBoardCanvasSize(this.context.getPiper()
                                            .getBoard()
                                            .getCanvasWidth(), this.context.getPiper()
                                                                           .getBoard()
                                                                           .getCanvasHeight());
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }
}
