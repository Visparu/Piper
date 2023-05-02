package de.visparu.piper.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import de.visparu.piper.root.Piper;
import de.visparu.piper.structures.boards.Field;

public class Input
{
	
	private static Keys		keyAdapter;
	private static Mouse	mouseAdapter;
	
	private static Set<Integer>	keys	= new HashSet<>();
	private static Set<Integer>	buttons	= new HashSet<>();
	
	public static Keys getKeyAdapter()
	{
		if (Input.keyAdapter == null)
		{
			Input.keyAdapter = new Keys();
		}
		return Input.keyAdapter;
	}
	
	public static Mouse getMouseAdapter()
	{
		if (Input.mouseAdapter == null)
		{
			Input.mouseAdapter = new Mouse();
		}
		return Input.mouseAdapter;
	}
	
	public static boolean isKeyDown(int keyCode)
	{
		return Input.keys.contains(keyCode);
	}
	
	public static boolean isButtonDown(int button)
	{
		return Input.buttons.contains(button);
	}
	
	public static class Keys extends KeyAdapter
	{
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			Input.keys.add(e.getKeyCode());
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_F2:
				{
					Piper.newGame();
					break;
				}
				case KeyEvent.VK_ESCAPE:
				{
					Piper.getBoard().setPaused(!Piper.getBoard().isPaused());
					break;
				}
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
			Input.keys.remove(e.getKeyCode());
		}
		
	}
	
	public static class Mouse extends MouseAdapter
	{
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			Input.buttons.add(e.getButton());
			int xf = e.getX() / Field.SIZE;
			int yf = e.getY() / Field.SIZE;
			switch (e.getButton())
			{
				case 1:
				{
					if (Piper.getBoard().hasWon() || Piper.getBoard().hasLost())
					{
						return;
					}
					if (Piper.getBoard().getField(xf, yf).getPipe() == null)
					{
						Piper.getBoard().addPipe(xf, yf, Piper.getToolbox().pollNextPipe());
					} else
					{
						Piper.getBoard().rotatePipe(xf, yf);
					}
					break;
				}
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			Input.buttons.add(e.getButton());
		}
		
	}
	
}
