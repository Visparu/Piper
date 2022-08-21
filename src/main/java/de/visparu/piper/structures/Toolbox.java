package de.visparu.piper.structures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import de.visparu.piper.structures.pipes.standard.CrossPipe;
import de.visparu.piper.structures.pipes.standard.CurvedPipe;
import de.visparu.piper.structures.pipes.standard.DeadEndPipe;
import de.visparu.piper.structures.pipes.standard.SplitPipe;
import de.visparu.piper.structures.pipes.standard.StraightPipe;
import de.visparu.piper.ui.GameWindow;

public class Toolbox
{
	
	public static final Color COLOR_SELECTED_TILE = Color.GREEN;
	
	public static final int	BORDER_SIZE		= 2;
	public static final int	PANEL_SPACING_H	= 10;
	public static final int	PANEL_WIDTH		= Field.SIZE + Toolbox.PANEL_SPACING_H * 2;
	
	public static final int PIPE_PREVIEW_AMOUNT = 5;
	
	private Random rand;
	
	private List<Field>	fields	= new ArrayList<>();
	private Queue<Pipe>	pipes	= new ArrayDeque<>();
	
	private Map<Class<? extends Pipe>, Integer> percentiles = new HashMap<>();
	
	public Toolbox(Random rand)
	{
		this.rand = rand;
		for (int i = 0; i < Toolbox.PIPE_PREVIEW_AMOUNT; i++)
		{
			this.fields.add(new Field());
		}
		this.populatePercentiles();
		this.reinitializeList();
	}
	
	private void populatePercentiles()
	{
		this.percentiles.put(StraightPipe.class, 10);
		this.percentiles.put(CurvedPipe.class, 30);
		this.percentiles.put(SplitPipe.class, 70);
		this.percentiles.put(CrossPipe.class, 85);
		this.percentiles.put(DeadEndPipe.class, 100);
	}
	
	private void reinitializeList()
	{
		int sizeBefore = this.pipes.size();
		for (int i = 0; i < Toolbox.PIPE_PREVIEW_AMOUNT - sizeBefore; i++)
		{
			this.pipes.add(this.createNewPipe());
		}
		int i = 0;
		for (Pipe pipe : this.pipes)
		{
			Field field = this.fields.get(i++);
			field.setPipe(pipe);
		}
	}
	
	public void render(Graphics2D g2d)
	{
		float completeSpacingV = GameWindow.getToolboxCanvasHeight() - this.fields.size() * Field.SIZE;
		float singularSpacingV = completeSpacingV / (this.fields.size() + 1);
		for (int i = 0; i < this.fields.size(); i++)
		{
			int x = Toolbox.PANEL_SPACING_H;
			int y = (int) (singularSpacingV * (i + 1) + Field.SIZE * i);
			int w = Field.SIZE;
			int h = Field.SIZE;
			if (i == 0)
			{
				g2d.setColor(Toolbox.COLOR_SELECTED_TILE);
				g2d.fillRect(x - Toolbox.BORDER_SIZE, y - Toolbox.BORDER_SIZE, w + 2 * Toolbox.BORDER_SIZE,
						h + 2 * Toolbox.BORDER_SIZE);
			}
			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d_img = img.createGraphics();
			this.fields.get(i).render(g2d_img);
			g2d_img.dispose();
			g2d.drawImage(img, x, y, null);
		}
	}
	
	public Pipe createNewPipe()
	{
		int perc = this.rand.nextInt(100);
		if (perc < this.percentiles.get(StraightPipe.class))
		{
			return new StraightPipe();
		} else if (perc < this.percentiles.get(CurvedPipe.class))
		{
			return new CurvedPipe();
		} else if (perc < this.percentiles.get(SplitPipe.class))
		{
			return new SplitPipe();
		} else if (perc < this.percentiles.get(CrossPipe.class))
		{
			return new CrossPipe();
		} else
		{
			return new DeadEndPipe();
		}
	}
	
	public Pipe pollNextPipe()
	{
		Pipe pipe = this.pipes.poll();
		this.reinitializeList();
		return pipe;
	}
	
	public int getCanvasWidth()
	{
		return Toolbox.PANEL_SPACING_H * 2 + Field.SIZE;
	}
	
}
