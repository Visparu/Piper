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

    private final JFrame    frame;
    private final JMenuBar  menubar;
    private final JMenu     menuGame;
    private final JMenuItem menuGameNewgame;
    private final JMenuItem menuGameSettings;
    private final JMenuItem menuGameExit;
    private final JMenu     menuDifficulty;
    private final JMenuItem menuDifficultyEasy;
    private final JMenuItem menuDifficultyMedium;
    private final JMenuItem menuDifficultyHard;
    private final JMenuItem menuDifficultyWtf;
    private final Canvas canvasToolbox;
    private final Canvas canvasBoard;

    public GameWindow() {
        this.frame                  = new JFrame();
        this.menubar           = new JMenuBar();
        this.menuGame           = new JMenu();
        this.menuGameNewgame  = new JMenuItem();
        this.menuGameSettings = new JMenuItem();
        this.menuGameExit         = new JMenuItem();
        this.menuDifficulty         = new JMenu();
        this.menuDifficultyEasy   = new JMenuItem();
        this.menuDifficultyMedium = new JMenuItem();
        this.menuDifficultyHard = new JMenuItem();
        this.menuDifficultyWtf = new JMenuItem();
        this.canvasToolbox = new Canvas();
        this.canvasBoard   = new Canvas();

        this.configureComponents();
    }

    private void configureComponents() {
        this.frame.setTitle("Piper");
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menuGame.setText("Game");
        this.menuGame.setMnemonic('g');

        this.menuGameNewgame.setText("New Game");
        this.menuGameNewgame.setMnemonic('n');

        this.menuGameSettings.setText("Settings");
        this.menuGameSettings.setMnemonic('s');
        this.menuGameSettings.setEnabled(false);

        this.menuGameExit.setText("Exit");
        this.menuGameExit.setMnemonic('e');

        this.menuDifficulty.setText("Difficulty");
        this.menuDifficulty.setMnemonic('d');

        this.menuDifficultyEasy.setText("Easy");
        this.menuDifficultyEasy.setMnemonic('e');

        this.menuDifficultyMedium.setText("Medium");
        this.menuDifficultyMedium.setMnemonic('m');

        this.menuDifficultyHard.setText("Hard");
        this.menuDifficultyHard.setMnemonic('h');

        this.menuDifficultyWtf.setText("...wtf?");
        this.menuDifficultyWtf.setMnemonic('w');

        this.frame.setLayout(new BorderLayout());
        this.frame.add(this.menubar, BorderLayout.NORTH);
        this.frame.add(this.canvasToolbox, BorderLayout.WEST);
        this.frame.add(this.canvasBoard, BorderLayout.EAST);

        this.menubar.add(this.menuGame);
        this.menubar.add(this.menuDifficulty);

        this.menuGame.add(this.menuGameNewgame);
        this.menuGame.add(this.menuGameSettings);
        this.menuGame.add(this.menuGameExit);

        this.menuDifficulty.add(this.menuDifficultyEasy);
        this.menuDifficulty.add(this.menuDifficultyMedium);
        this.menuDifficulty.add(this.menuDifficultyHard);
        this.menuDifficulty.add(this.menuDifficultyWtf);

        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public void initialize() {
        this.initializeListeners();
    }

    private void initializeListeners() {
        this.canvasBoard.addMouseListener(GameContext.get().getInput().getMouseAdapter());
        this.canvasBoard.addKeyListener(GameContext.get().getInput().getKeyAdapter());

        this.menuGameNewgame.addActionListener(e -> GameContext.get().getGame().newGame());
        this.menuGameSettings.addActionListener(e -> {

        });
        this.menuGameExit.addActionListener(e -> System.exit(0));

        this.menuDifficultyEasy.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.EASY);
            GameContext.get().getGame().newGame();
        });
        this.menuDifficultyMedium.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.MEDIUM);
            GameContext.get().getGame().newGame();
        });
        this.menuDifficultyHard.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.HARD);
            GameContext.get().getGame().newGame();
        });
        this.menuDifficultyWtf.addActionListener(e -> {
            Settings.setDifficulty(Difficulty.WTF);
            GameContext.get().getGame().newGame();
        });
    }

    public BufferStrategy getToolboxCanvasBufferStrategy() {
        BufferStrategy bs = this.canvasToolbox.getBufferStrategy();
        if (bs == null) {
            this.canvasToolbox.createBufferStrategy(GameWindow.CANVAS_BUFFERS);
            bs = this.canvasToolbox.getBufferStrategy();
        }
        return bs;
    }

    public BufferStrategy getBoardCanvasBufferStrategy() {
        BufferStrategy bs = this.canvasBoard.getBufferStrategy();
        if (bs == null) {
            this.canvasBoard.createBufferStrategy(GameWindow.CANVAS_BUFFERS);
            bs = this.canvasBoard.getBufferStrategy();
        }
        return bs;
    }

    public int getToolboxCanvasWidth() {
        return this.canvasToolbox.getWidth();
    }

    public int getToolboxCanvasHeight() {
        return this.canvasToolbox.getHeight();
    }

    public int getBoardCanvasWidth() {
        return this.canvasBoard.getWidth();
    }

    public int getBoardCanvasHeight() {
        return this.canvasBoard.getHeight();
    }

    private void setToolboxCanvasSize(int width,
                                      int height) {
        this.canvasToolbox.setSize(width, height);
    }

    private void setBoardCanvasSize(int width,
                                    int height) {
        this.canvasBoard.setSize(width, height);
    }

    public void resize() {
        this.setToolboxCanvasSize(GameContext.get().getGame().getToolbox()
                                           .getCanvasWidth(), GameContext.get().getGame().getBoard()
                                                                       .getCanvasHeight());
        this.setBoardCanvasSize(GameContext.get().getGame().getBoard()
                                         .getCanvasWidth(), GameContext.get().getGame().getBoard()
                                                                     .getCanvasHeight());
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }
}
