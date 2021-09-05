package de.visparu.piper.root;

import de.visparu.piper.ui.GameWindow;

public class Main
{
	
	public static void main(String[] args)
	{
		GameWindow.init();
		Piper.init();
		Framework.init();
		Framework.start();
	}
	
}
