package de.visparu.piper.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.game.GameFactory;
import de.visparu.piper.game.settings.Difficulty;
import de.visparu.piper.game.settings.Settings;

public final class GameWindow {
    private static final int CANVAS_BUFFERS = 3;

    private final JFrame    frame;
    private final JMenuBar  menubar;
    private final JMenu     menuGame;
    private final JMenuItem menuGameNewGame;
    private final JMenuItem menuGameSettings;
    private final JMenuItem menuGameExit;
    private final JMenu     menuDifficulty;
    private final JMenuItem menuDifficultyEasy;
    private final JMenuItem menuDifficultyMedium;
    private final JMenuItem menuDifficultyHard;
    private final JMenuItem menuDifficultyWtf;
    private final Canvas    canvas;

    public GameWindow() {
        this.frame                = new JFrame();
        this.menubar              = new JMenuBar();
        this.menuGame             = new JMenu();
        this.menuGameNewGame      = new JMenuItem();
        this.menuGameSettings     = new JMenuItem();
        this.menuGameExit         = new JMenuItem();
        this.menuDifficulty       = new JMenu();
        this.menuDifficultyEasy   = new JMenuItem();
        this.menuDifficultyMedium = new JMenuItem();
        this.menuDifficultyHard   = new JMenuItem();
        this.menuDifficultyWtf    = new JMenuItem();
        this.canvas          = new Canvas();

        this.configureComponents();
    }

    private void configureComponents() {
        this.frame.setTitle("Piper");
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.menuGame.setText("Game");
        this.menuGame.setMnemonic('g');

        this.menuGameNewGame.setText("New Game");
        this.menuGameNewGame.setMnemonic('n');

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
        this.frame.add(this.canvas, BorderLayout.CENTER);

        this.menubar.add(this.menuGame);
        this.menubar.add(this.menuDifficulty);

        this.menuGame.add(this.menuGameNewGame);
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
        this.canvas.addMouseListener(GameContext.get().getInputController().getMouseAdapter());
        this.canvas.addKeyListener(GameContext.get().getInputController().getKeyAdapter());

        this.menuGameNewGame.addActionListener(e -> GameContext.get().setGame(new GameFactory().create(Settings.forDifficulty(Difficulty.EASY))));
        this.menuGameSettings.addActionListener(e -> {
            // TODO: create settings menu
        });
        this.menuGameExit.addActionListener(e -> System.exit(0));

        this.menuDifficultyEasy.addActionListener(e -> GameContext.get().setGame(new GameFactory().create(Settings.forDifficulty(Difficulty.EASY))));
        this.menuDifficultyMedium.addActionListener(e -> GameContext.get().setGame(new GameFactory().create(Settings.forDifficulty(Difficulty.MEDIUM))));
        this.menuDifficultyHard.addActionListener(e -> GameContext.get().setGame(new GameFactory().create(Settings.forDifficulty(Difficulty.HARD))));
        this.menuDifficultyWtf.addActionListener(e -> GameContext.get().setGame(new GameFactory().create(Settings.forDifficulty(Difficulty.WTF))));
    }

    public BufferStrategy getCanvasBufferStrategy() {
        BufferStrategy bs = this.canvas.getBufferStrategy();
        if (bs == null) {
            this.canvas.createBufferStrategy(GameWindow.CANVAS_BUFFERS);
            bs = this.canvas.getBufferStrategy();
        }
        return bs;
    }

    public int getCanvasWidth() {
        return this.canvas.getWidth();
    }

    public int getCanvasHeight() {
        return this.canvas.getHeight();
    }

    public void resize(final Dimension dimension) {
        this.canvas.setSize(dimension);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }
}
