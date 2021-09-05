package de.visparu.piper.root;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import de.visparu.piper.ui.GameWindow;

public final class Framework
{
	
	public static final boolean DEBUG = false;
	
	public static final double	TPS	= 120.0;
	public static final double	FPS	= 120.0;
	public static final double	IDL	= 1.5;
	
	private static boolean	init	= false;
	private static boolean	running;
	
	private Framework()
	{}
	
	public static final void init()
	{
		if (Framework.init)
			throw new IllegalStateException();
		Framework.init = true;
		Framework.running = false;
	}
	
	public static final void start()
	{
		if (!Framework.init)
			throw new IllegalStateException();
		if (Framework.running)
			throw new IllegalStateException();
		Framework.running = true;
		Framework.run();
	}
	
	private static final void run()
	{
		if (Framework.DEBUG)
		{
			System.out.println("DEBUG MODE ENABLED");
		}
		
		long startTime = System.nanoTime();
		
		long lastTick = startTime;
		long lastFrame = startTime;
		long lastInfo = startTime;
		
		double deltaTick = 1000000000 / Framework.TPS;
		double deltaFrame = 1000000000 / Framework.FPS;
		double deltaInfo = 1000000000 * Framework.IDL;
		
		int ticks = 0;
		int frames = 0;
		
		while (Framework.running)
		{
			long now = System.nanoTime();
			
			if (now > lastTick + deltaTick)
			{
				lastTick = now;
				Framework.tick();
				ticks++;
			}
			
			if (now > lastFrame + deltaFrame)
			{
				lastFrame = now;
				Framework.render();
				frames++;
			}
			
			if (now > lastInfo + deltaInfo)
			{
				lastInfo = now;
				Framework.info((int) (ticks / Framework.IDL), (int) (frames / Framework.IDL));
				ticks = 0;
				frames = 0;
			}
		}
	}
	
	public static final void stop()
	{
		if (!Framework.running)
			throw new IllegalStateException();
		Framework.running = false;
	}
	
	private static final void tick()
	{
		Piper.getBoard().tick();
	}
	
	private static final void render()
	{
		BufferStrategy bs_toolbox = GameWindow.getToolboxCanvasBufferStrategy();
		Graphics2D g2d_toolbox = (Graphics2D) bs_toolbox.getDrawGraphics();
		
		g2d_toolbox.setColor(Color.BLACK);
		g2d_toolbox.fillRect(0, 0, GameWindow.getToolboxCanvasWidth(), GameWindow.getToolboxCanvasHeight());
		Piper.getToolbox().render(g2d_toolbox);
		
		BufferStrategy bs_board = GameWindow.getBoardCanvasBufferStrategy();
		Graphics2D g2d_board = (Graphics2D) bs_board.getDrawGraphics();
		
		g2d_board.setColor(Color.BLACK);
		g2d_board.fillRect(0, 0, GameWindow.getBoardCanvasWidth(), GameWindow.getBoardCanvasHeight());
		Piper.getBoard().render(g2d_board);
		
		bs_toolbox.show();
		bs_board.show();
	}
	
	private static final void info(int ticks, int frames)
	{
		if (Framework.DEBUG)
		{
			System.out.printf("%d Ticks, %d Frames\n", ticks, frames);
		}
	}
	
}
