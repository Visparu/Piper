package de.visparu.piper.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.visparu.piper.root.Piper;
import de.visparu.piper.settings.Difficulty;
import de.visparu.piper.settings.Settings;

public final class GameWindow
{
	
	public static final int CANVAS_BUFFERS = 3;
	
	private static boolean init = false;
	
	private static JFrame		frame					= new JFrame();
	private static JMenuBar		menubar					= new JMenuBar();
	private static JMenu		menu_game				= new JMenu();
	private static JMenuItem	menu_game_newgame		= new JMenuItem();
	private static JMenuItem	menu_game_settings		= new JMenuItem();
	private static JMenuItem	menu_game_exit			= new JMenuItem();
	private static JMenu		menu_difficulty			= new JMenu();
	private static JMenuItem	menu_difficulty_easy	= new JMenuItem();
	private static JMenuItem	menu_difficulty_medium	= new JMenuItem();
	private static JMenuItem	menu_difficulty_hard	= new JMenuItem();
	private static JMenuItem	menu_difficulty_wtf		= new JMenuItem();
	private static Canvas		canvas_toolbox			= new Canvas();
	private static Canvas		canvas_board			= new Canvas();
	
	private GameWindow()
	{}
	
	public static void init()
	{
		if (GameWindow.init)
		{
			throw new IllegalStateException();
		}
		GameWindow.init = true;
		
		GameWindow.init_configure();
		GameWindow.init_connect();
		GameWindow.init_listen();
		GameWindow.init_present();
	}
	
	private static void init_configure()
	{
		GameWindow.frame.setTitle("Piper");
		GameWindow.frame.setResizable(false);
		GameWindow.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GameWindow.menu_game.setText("Game");
		GameWindow.menu_game.setMnemonic('g');
		
		GameWindow.menu_game_newgame.setText("New Game");
		GameWindow.menu_game_newgame.setMnemonic('n');
		
		GameWindow.menu_game_settings.setText("Settings");
		GameWindow.menu_game_settings.setMnemonic('s');
		GameWindow.menu_game_settings.setEnabled(false);
		
		GameWindow.menu_game_exit.setText("Exit");
		GameWindow.menu_game_exit.setMnemonic('e');
		
		GameWindow.menu_difficulty.setText("Difficulty");
		GameWindow.menu_difficulty.setMnemonic('d');
		
		GameWindow.menu_difficulty_easy.setText("Easy");
		GameWindow.menu_difficulty_easy.setMnemonic('e');
		
		GameWindow.menu_difficulty_medium.setText("Medium");
		GameWindow.menu_difficulty_medium.setMnemonic('m');
		
		GameWindow.menu_difficulty_hard.setText("Hard");
		GameWindow.menu_difficulty_hard.setMnemonic('h');
		
		GameWindow.menu_difficulty_wtf.setText("...wtf?");
		GameWindow.menu_difficulty_wtf.setMnemonic('w');
	}
	
	private static void init_connect()
	{
		GameWindow.frame.setLayout(new BorderLayout());
		GameWindow.frame.add(GameWindow.menubar, BorderLayout.NORTH);
		GameWindow.frame.add(GameWindow.canvas_toolbox, BorderLayout.WEST);
		GameWindow.frame.add(GameWindow.canvas_board, BorderLayout.EAST);
		
		GameWindow.menubar.add(GameWindow.menu_game);
		GameWindow.menubar.add(GameWindow.menu_difficulty);
		
		GameWindow.menu_game.add(GameWindow.menu_game_newgame);
		GameWindow.menu_game.add(GameWindow.menu_game_settings);
		GameWindow.menu_game.add(GameWindow.menu_game_exit);
		
		GameWindow.menu_difficulty.add(GameWindow.menu_difficulty_easy);
		GameWindow.menu_difficulty.add(GameWindow.menu_difficulty_medium);
		GameWindow.menu_difficulty.add(GameWindow.menu_difficulty_hard);
		GameWindow.menu_difficulty.add(GameWindow.menu_difficulty_wtf);
	}
	
	private static void init_listen()
	{
		GameWindow.canvas_board.addMouseListener(Input.getMouseAdapter());
		GameWindow.canvas_board.addKeyListener(Input.getKeyAdapter());
		
		GameWindow.menu_game_newgame.addActionListener(e ->
		{
			Piper.newGame();
		});
		GameWindow.menu_game_settings.addActionListener(e ->
		{
			
		});
		GameWindow.menu_game_exit.addActionListener(e ->
		{
			System.exit(0);
		});
		
		GameWindow.menu_difficulty_easy.addActionListener(e ->
		{
			Settings.setDifficulty(Difficulty.EASY);
			Piper.newGame();
		});
		GameWindow.menu_difficulty_medium.addActionListener(e ->
		{
			Settings.setDifficulty(Difficulty.MEDIUM);
			Piper.newGame();
		});
		GameWindow.menu_difficulty_hard.addActionListener(e ->
		{
			Settings.setDifficulty(Difficulty.HARD);
			Piper.newGame();
		});
		GameWindow.menu_difficulty_wtf.addActionListener(e ->
		{
			Settings.setDifficulty(Difficulty.WTF);
			Piper.newGame();
		});
	}
	
	private static void init_present()
	{
		GameWindow.frame.pack();
		GameWindow.frame.setLocationRelativeTo(null);
		GameWindow.frame.setVisible(true);
	}
	
	public static BufferStrategy getToolboxCanvasBufferStrategy()
	{
		BufferStrategy bs = GameWindow.canvas_toolbox.getBufferStrategy();
		if (bs == null)
		{
			GameWindow.canvas_toolbox.createBufferStrategy(GameWindow.CANVAS_BUFFERS);
			bs = GameWindow.canvas_toolbox.getBufferStrategy();
		}
		return bs;
	}
	
	public static BufferStrategy getBoardCanvasBufferStrategy()
	{
		BufferStrategy bs = GameWindow.canvas_board.getBufferStrategy();
		if (bs == null)
		{
			GameWindow.canvas_board.createBufferStrategy(GameWindow.CANVAS_BUFFERS);
			bs = GameWindow.canvas_board.getBufferStrategy();
		}
		return bs;
	}
	
	public static int getToolboxCanvasWidth()
	{
		return GameWindow.canvas_toolbox.getWidth();
	}
	
	public static int getToolboxCanvasHeight()
	{
		return GameWindow.canvas_toolbox.getHeight();
	}
	
	public static int getBoardCanvasWidth()
	{
		return GameWindow.canvas_board.getWidth();
	}
	
	public static int getBoardCanvasHeight()
	{
		return GameWindow.canvas_board.getHeight();
	}
	
	private static void setToolboxCanvasSize(int width, int height)
	{
		GameWindow.canvas_toolbox.setSize(width, height);
	}
	
	private static void setBoardCanvasSize(int width, int height)
	{
		GameWindow.canvas_board.setSize(width, height);
	}
	
	public static void resize()
	{
		GameWindow.setToolboxCanvasSize(Piper.getToolbox().getCanvasWidth(), Piper.getBoard().getCanvasHeight());
		GameWindow.setBoardCanvasSize(Piper.getBoard().getCanvasWidth(), Piper.getBoard().getCanvasHeight());
		GameWindow.frame.pack();
		GameWindow.frame.setLocationRelativeTo(null);
	}
	
}
